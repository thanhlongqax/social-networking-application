package com.tdtu.search_services.service.Impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.tdtu.search_services.dto.ResDTO;
import com.tdtu.search_services.dto.request.UserToElasticSearchDTO;
import com.tdtu.search_services.model.User;
import com.tdtu.search_services.model.UserDocument;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.tdtu.search_services.repository.UserSearchRepository;
import com.tdtu.search_services.service.IUserSearchService;
import com.tdtu.search_services.service.UserService;
import com.tdtu.search_services.utils.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserSearchService implements IUserSearchService {

    private final ElasticsearchClient elasticsearchClient;
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final UserSearchRepository userSearchRepository;

    public ResDTO<?> searchUsers(String token , String keyword) throws IOException {
        jwtUtils.getTokenSubject(token);
        // Xây dựng truy vấn tìm kiếm trên nhiều trường
        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index("users") // Index Elasticsearch của bạn
                .query(q -> q
                        .bool(b -> b
                                .should(m -> m.match(t -> t.field("username").query(keyword)))
                                .should(m -> m.match(t -> t.field("firstName").query(keyword)))
                                .should(m -> m.match(t -> t.field("middleName").query(keyword)))
                                .should(m -> m.match(t -> t.field("lastName").query(keyword)))
                                .should(m -> m.match(t -> t.field("userFullName").query(keyword)))
                        )
                )
        );

        // Thực thi truy vấn
        SearchResponse<UserDocument> searchResponse = elasticsearchClient.search(searchRequest, UserDocument.class);

        // Lấy kết quả từ response
        List<String> userIds = searchResponse.hits().hits().stream()
                .map(Hit::source)
                .filter(Objects::nonNull) // ✅ Bỏ qua giá trị null
                .map(UserDocument::getId) // 🔥 Lấy `idUser`
                .collect(Collectors.toList());

        List<User> users = userService.findByIds(userIds);

        // Tạo response
        ResDTO<List<User>> response = new ResDTO<>();
        response.setCode(HttpServletResponse.SC_OK);
        response.setData(users);
        response.setMessage("tìm kiếm theo danh sách thành công");
        return response;
    }
    public ResDTO<?> saveUserToElasticSearch(UserToElasticSearchDTO user) {
        UserDocument userDocument = new UserDocument();
        userDocument.setId(user.getId());
        userDocument.setUsername(user.getUsername());
        userDocument.setFirstName(user.getFirstName());
        userDocument.setMiddleName(user.getMiddleName());
        userDocument.setLastName(user.getLastName());
        userDocument.setUserFullName(user.getUserFullName());

        userSearchRepository.save(userDocument);
        return new ResDTO<>(HttpServletResponse.SC_OK , "Đã lưu thành công",null);
    }
}

