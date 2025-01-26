package com.tdtu.newsfeed_service.services;



import com.tdtu.newsfeed_service.dtos.ResDTO;
import com.tdtu.newsfeed_service.dtos.request.*;
import com.tdtu.newsfeed_service.dtos.respone.InteractNotification;
import com.tdtu.newsfeed_service.dtos.respone.PostResponse;
import com.tdtu.newsfeed_service.dtos.respone.ShareInfo;
import com.tdtu.newsfeed_service.enums.EFileType;
import com.tdtu.newsfeed_service.enums.EPostType;
import com.tdtu.newsfeed_service.enums.EPrivacy;
import com.tdtu.newsfeed_service.mapper.request.PostPostRequestMapper;
import com.tdtu.newsfeed_service.mapper.request.UpdatePostRequestMapper;
import com.tdtu.newsfeed_service.mapper.respone.PostResponseMapper;
import com.tdtu.newsfeed_service.models.*;
import com.tdtu.newsfeed_service.repositories.BannedWordRepository;
import com.tdtu.newsfeed_service.repositories.CustomPostRepository;
import com.tdtu.newsfeed_service.repositories.PostRepository;
import com.tdtu.newsfeed_service.repositories.ReportRepository;
import com.tdtu.newsfeed_service.utils.DateUtils;
import com.tdtu.newsfeed_service.utils.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final PostResponseMapper postResponseMapper;
    private final PostPostRequestMapper postPostRequestMapper;
    private final UpdatePostRequestMapper updatePostRequestMapper;
    private final CustomPostRepository customPostRepository;
    private final JwtUtils jwtUtils;
    private final FileService fileService;
    private final UserService userService;
    private final ReportRepository reportRepository;
    private final PostShareService postShareService;
    private final BannedWordRepository bannedWordRepository;
    private final SendKafkaMsgService kafkaMsgService;
    private final ModerationService moderationService;

    public Post findPostById(String postId){
        return postRepository.findById(postId).orElse(null);
    }

    public ResDTO<?> findPostRespById(String token, String postId){
        ResDTO<PostResponse> response = new ResDTO<>();

        postRepository.findById(postId)
                .ifPresentOrElse(
                        post -> {
                            response.setMessage("success");
                            response.setData(postResponseMapper.mapToDto(token, post));
                            response.setCode(HttpServletResponse.SC_OK);
                        }, () -> {
                            response.setMessage("post not found with id: " + postId);
                            response.setData(null);
                            response.setCode(HttpServletResponse.SC_OK);
                        }
                );

        return response;
    }

    public ResDTO<?> getUserIdByPostId(String postId){
        ResDTO<Map<String, String>> response = new ResDTO<>();
        Map<String, String> data = new HashMap<>();
        data.put("userId", "");
        postRepository.findById(postId)
                .ifPresentOrElse(
                        post -> {
                            data.put("userId", post.getUserId());
                            response.setMessage("success");
                            response.setData(data);
                            response.setCode(HttpServletResponse.SC_OK);
                        }, () -> {
                            response.setMessage("post not found with id: " + postId);
                            response.setData(data);
                            response.setCode(HttpServletResponse.SC_OK);
                        }
                );

        return response;
    }

    @Cacheable(key = "T(java.util.Objects).hash(#a1, #a2, #a3)", value = "news-feed", unless = "#result.data['posts'].isEmpty() or #result.data['posts'] == null")
    public ResDTO<?> getNewsFeed(String token, int page, int size, String startTime) {
        FetchNewsFeedReq req = new FetchNewsFeedReq();
        req.setPage(page);
        req.setSize(size);
        if (startTime == null ) {
            LocalDateTime  start = LocalDateTime.now().minusDays(1);
            startTime = DateUtils.localDateTimeToString(start);

        }
        req.setStartTime(DateUtils.stringToLocalDate(startTime));

        int startIndex = (req.getPage() - 1) * req.getSize();

        ResDTO<Map<String, Object>> response = new ResDTO<>();

        List<String> friendIds = userService.findUserFriendIdsByUserToken(token)
                .stream()
                .map(User::getId)
                .toList();

        String userId = jwtUtils.getUserIdFromJwtToken(token);

        List<PostResponse> posts = customPostRepository.findNewsFeed(userId, friendIds, req.getStartTime())
                .stream().map(
                        post -> {
                            PostResponse postResponse = postResponseMapper.mapToDto(token, post);
                            postResponse.setMine(post.getUserId().equals(userId));
                            return postResponse;
                        }
                ).toList();

        //Todo: Find shared posts then combine all of them with pagination

        List<PostResponse> sharedPosts = postShareService.findSharedPostByFriendIds(friendIds, userId, req.getStartTime())
                .stream().map(
                        postShare -> {
                            Post foundPost = findPostById(postShare.getSharedPostId());
                            if(foundPost != null){
                                PostResponse postResponse = postResponseMapper.mapToDto(token, foundPost);
                                postResponse.setMine(foundPost.getUserId().equals(userId));
                                postResponse.setType(EPostType.SHARE);

                                ShareInfo shareInfo = new ShareInfo();
                                shareInfo.setStatus(postShare.getStatus());
                                shareInfo.setSharedUser(userService.findById(postShare.getSharedUserId()));
                                shareInfo.setSharedAt(DateUtils.localDateTimeToDate(postShare.getSharedAt()));
                                shareInfo.setId(postShare.getId());

                                postResponse.setShareInfo(shareInfo);

                                return postResponse;
                            }
                            return null;
                        }
                )
                .filter(p -> p != null && p.getPrivacy().equals(EPrivacy.PUBLIC))
                .toList();

        List<PostResponse> combinedPosts = Stream.concat(posts.stream(), sharedPosts.stream())
                .sorted(Comparator.comparing(PostResponse::getCreatedAt).reversed())
                .toList();

        Map<String, Object> data = new HashMap<>();

        data.put("totalPages", (int) Math.ceil((double) combinedPosts.size() / req.getSize()));
        data.put("posts", combinedPosts.stream().skip(startIndex)
                .limit(req.getSize()).toList());

        response.setData(data);
        response.setCode(HttpServletResponse.SC_OK);
        response.setMessage("success");

        return response;
    }

    public ResDTO<?> findByContentContaining(String token, String key){
        ResDTO<List<PostResponse>> response = new ResDTO<>();
        response.setData(postRepository.findByContent(key).stream().map(
                p -> postResponseMapper.mapToDto(token, p)
        ).toList());
        response.setMessage("success");
        response.setCode(HttpServletResponse.SC_OK);

        return response;
    }

    public ResDTO<?> savePost(String token, CreatePostRequest postRequest){
        Post post = postPostRequestMapper.mapToObject(postRequest);

        Set<String> bannedWords = bannedWordRepository.findAll().stream()
                .map(bannedWord -> bannedWord.getWord().toLowerCase())
                .collect(Collectors.toSet());

        String content = post.getContent().toLowerCase();

        for (String word : bannedWords) {
            if (content.contains(word)) {
                throw new IllegalArgumentException("Bài viết của bạn chứa một số từ bị cấm!");
            }
        }

        post.getImageUrls().forEach(img -> {
            if(!moderationService.moderateImage(img)){
                fileService.delete(img, EFileType.TYPE_IMG);
                throw new IllegalArgumentException("Bài viết của bạn chứa một số chứa hình ảnh không phù hợp!");
            }
        });

        List<PostTagReqDTO> postTags = postRequest.getPostTags() != null
                ? postRequest.getPostTags()
                : new ArrayList<>(); // Danh sách mặc định nếu null

        List<String> userIds = postTags.stream()
                .map(PostTagReqDTO::getTaggedUserId)
                .filter(Objects::nonNull) // Loại bỏ ID null
                .toList();

        List<User> taggedUser = userService.findByIds(userIds);

        if (taggedUser == null) {
            taggedUser = new ArrayList<>(); // Đảm bảo danh sách không null
        }

        post.setPostTags(
                taggedUser.stream()
                        .map(user -> {
                            PostTag postTag = new PostTag();
                            postTag.setId(UUID.randomUUID().toString());
                            postTag.setCreatedAt(LocalDateTime.now());
                            postTag.setTaggedUser(user);
                            return postTag;
                        })
                        .toList()
        );

        post.setUserId(jwtUtils.getUserIdFromJwtToken(token));
        post = postRepository.save(post);

        PostResponse postResponse = postResponseMapper.mapToDto(token, post);
        postResponse.setMine(post.getUserId().equals(jwtUtils.getUserIdFromJwtToken(token)));

        ResDTO<PostResponse> response = new ResDTO<>();
        response.setMessage("Đã đăng bài viết");
        response.setCode(HttpServletResponse.SC_OK);
        response.setData(postResponse);

        return response;
    }

    public ResDTO<?> updatePostContent(String token, UpdatePostContentRequest request){
        String userId = jwtUtils.getUserIdFromJwtToken(token);

        Post foundPost = findPostById(request.getId());
        ResDTO<PostResponse> response = new ResDTO<>();

        if(foundPost != null){
            if(foundPost.getUserId().equals(userId)){
                updatePostRequestMapper.bindToObject(request, foundPost);

                List<User> taggedUser = userService.findByIds(request.getTaggingUsers());

                foundPost.setPostTags(
                        taggedUser.stream().map(
                                user -> {
                                    PostTag postTag = new PostTag();
                                    postTag.setId(UUID.randomUUID().toString());
                                    postTag.setCreatedAt(LocalDateTime.now());
                                    postTag.setTaggedUser(user);
                                    return postTag;
                                }
                        ).toList()
                );

                postRepository.save(foundPost);

                response.setMessage("Đã cập nhật bài viết");
                response.setCode(HttpServletResponse.SC_OK);
                response.setData(postResponseMapper.mapToDto(token, foundPost));

                return response;
            }
            response.setMessage("Không thể cập nhật bài viết của người khác");
            response.setCode(HttpServletResponse.SC_BAD_REQUEST);
            response.setData(null);

            return response;
        }

        response.setMessage("Không tìm thấy bài viết có id: " + request.getId());
        response.setCode(HttpServletResponse.SC_BAD_REQUEST);
        response.setData(null);

        return response;
    }


    public ResDTO<?> deletePost(String token, String postId){
        String userId = jwtUtils.getUserIdFromJwtToken(token);

        Post foundPost = findPostById(postId);
        ResDTO<Map<String, String>> response = new ResDTO<>();
        Map<String, String> data = new HashMap<>();

        if(foundPost != null){
            if(foundPost.getUserId().equals(userId)){
                postRepository.delete(foundPost);

                data.put("deletedId", foundPost.getId());

                response.setMessage("Đã xoá bài viết");
                response.setCode(HttpServletResponse.SC_OK);
                response.setData(data);

                return response;

            }

            response.setMessage("Không thể xóa bài viết của người khác");
            response.setCode(HttpServletResponse.SC_BAD_REQUEST);
            response.setData(null);

            return response;
        }

        response.setMessage("Không tìm thấy bài viết có id: " + postId);
        response.setCode(HttpServletResponse.SC_BAD_REQUEST);
        response.setData(null);

        return response;
    }

    public ResDTO<?> sharePost(String token, SharePostRequest request){
        ResDTO<PostResponse> response = new ResDTO<>();

        postRepository.findById(request.getPostId())
                .ifPresentOrElse(
                        (post) -> {
                            String userId = jwtUtils.getUserIdFromJwtToken(token);
                            User foundUser = userService.findById(userId);

                            if(foundUser != null) {
                                PostShare postShare = new PostShare();

                                postShare.setId(UUID.randomUUID().toString());
                                postShare.setSharedAt(LocalDateTime.now());
                                postShare.setSharedUserId(foundUser.getId());
                                postShare.setStatus(request.getStatus());
                                postShare.setSharedPostId(post.getId());

                                postShareService.save(postShare);

                                InteractNotification notification = new InteractNotification();
                                notification.setAvatarUrl(foundUser.getProfilePicture());
                                notification.setUserFullName(String.join(" ", foundUser.getFirstName(), foundUser.getMiddleName(), foundUser.getLastName()));
                                notification.setContent(notification.getUserFullName() + " đã chia sẻ bài viết của bạn");
                                notification.setPostId(request.getPostId());
                                notification.setTitle("Có người tương tác nè!");

                                kafkaMsgService.pushSharePostMessage(notification);

                                response.setMessage("Đã chia sẻ bài viết");
                                response.setCode(HttpServletResponse.SC_OK);
                                response.setData(postResponseMapper.mapToDto(token, post));
                            }else{
                                response.setMessage("Không tìm thấy người dùng");
                                response.setCode(HttpServletResponse.SC_BAD_REQUEST);
                                response.setData(null);
                            }

                        }, () -> {
                            response.setMessage("Không tìm thấy bài viết");
                            response.setCode(HttpServletResponse.SC_BAD_REQUEST);
                            response.setData(null);
                        }
                );

        return response;
    }

    public ResDTO<?> findMyPosts(String token){
        String userId = jwtUtils.getUserIdFromJwtToken(token);
        List<Post> posts = postRepository.findByUserIdOrPostTagsTaggedUserId(userId, userId);

        List<PostResponse> myPosts = posts.stream().map(post -> {
            PostResponse postResponse = postResponseMapper.mapToDto(token, post);
            postResponse.setMine(post.getUserId().equals(userId));

            return postResponse;

        }).toList();

        //TODO: Find my share post

        List<PostResponse> sharedPosts = postShareService.findSharedPostByUserId(userId)
                .stream().map(
                        postShare -> {
                            Post foundPost = findPostById(postShare.getSharedPostId());
                            if(foundPost != null){
                                PostResponse postResponse = postResponseMapper.mapToDto(token, foundPost);
                                postResponse.setMine(foundPost.getUserId().equals(userId));
                                postResponse.setType(EPostType.SHARE);
                                postResponse.setCreatedAt(DateUtils.localDateTimeToDate(postShare.getSharedAt()));

                                ShareInfo shareInfo = new ShareInfo();
                                shareInfo.setStatus(postShare.getStatus());
                                shareInfo.setSharedUser(userService.findById(postShare.getSharedUserId()));
                                shareInfo.setSharedAt(DateUtils.localDateTimeToDate(postShare.getSharedAt()));
                                shareInfo.setId(postShare.getId());

                                postResponse.setShareInfo(shareInfo);

                                return postResponse;
                            }
                            return null;
                        }
                )
                .filter(p -> p != null && p.getPrivacy().equals(EPrivacy.PUBLIC))
                .toList();

        List<PostResponse> combinedPosts = Stream.concat(myPosts.stream(), sharedPosts.stream())
                .sorted(Comparator.comparing(PostResponse::getCreatedAt).reversed())
                .toList();

        ResDTO<List<PostResponse>> response = new ResDTO<>();
        response.setData(combinedPosts);

        response.setMessage("success");
        response.setCode(HttpServletResponse.SC_OK);

        return response;
    }

    public ResDTO<?> reportPost(String token, ReportRequest request){
        String userId = jwtUtils.getUserIdFromJwtToken(token);

        ResDTO<Map<String, String>> response = new ResDTO<>();
        Map<String, String> data = new HashMap<>();

        postRepository.findById(request.getPostId())
                .ifPresentOrElse(
                        p -> {
                            Report report = new Report();
                            report.setReason(request.getReason());
                            report.setId(UUID.randomUUID().toString());
                            report.setUserId(userId);
                            report.setPostId(p.getId());

                            reportRepository.save(report);

                            data.put("savedReport", report.getId());

                            response.setMessage("submitted");
                            response.setData(data);
                            response.setCode(200);
                        }, () -> {
                            throw new RuntimeException("post not found with id: " + request.getPostId());
                        }
                );

        return response;
    }
}