package com.tdtu.newsfeed_service.mapper.request;



import com.tdtu.newsfeed_service.dtos.request.UpdatePostContentRequest;
import com.tdtu.newsfeed_service.enums.EFileType;
import com.tdtu.newsfeed_service.models.Post;
import com.tdtu.newsfeed_service.services.FileService;
import com.tdtu.newsfeed_service.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UpdatePostRequestMapper {
    private final FileService fileService;
    public void bindToObject(UpdatePostContentRequest dto, Post object){
        object.setContent(dto.getContent());
        object.setNormalizedContent(StringUtils.toSlug(object.getContent()));

        List<String> needDelVidUrls = new ArrayList<>(object.getVideoUrls());
        needDelVidUrls.removeAll(dto.getVideoUrls());

        List<String> needDelImgUrls = new ArrayList<>(object.getImageUrls());
        needDelImgUrls.removeAll(dto.getImageUrls());

        if(!needDelImgUrls.isEmpty())
            needDelImgUrls.forEach(url -> {
                fileService.delete(url, EFileType.TYPE_IMG);
            });
        if(!needDelVidUrls.isEmpty())
            needDelVidUrls.forEach(url -> {
                fileService.delete(url, EFileType.TYPE_VIDEO);
            });

        object.setVideoUrls(dto.getVideoUrls());
        object.setImageUrls(dto.getImageUrls());

        if(!object.getPrivacy().equals(dto.getPrivacy())){
            object.setPrivacy(dto.getPrivacy());
        }

        object.setUpdatedAt(LocalDateTime.now());
    }
}