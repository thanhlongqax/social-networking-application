package com.tdtu.newsfeed_service.services;


import com.tdtu.newsfeed_service.dtos.ResDTO;
import com.tdtu.newsfeed_service.dtos.request.FileReq;
import com.tdtu.newsfeed_service.enums.EFileType;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {
    @Value("${service.file-service.host}")
    private String host;
    private final RestTemplate restTemplate;

    public String upload(MultipartFile file, EFileType type) throws Exception {
        String url = host + "/api/file/upload/" + type.getType();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<ResDTO> response = restTemplate.postForEntity(url, requestEntity, ResDTO.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            ResDTO<Map<String, String>> responseBody = response.getBody();
            if (responseBody != null && responseBody.getCode() == HttpServletResponse.SC_CREATED) {
                Map<String, String> data = responseBody.getData();
                if (data != null) {
                    return data.get("url");
                }
            }
        }
        throw new RuntimeException("failed to upload file to FileService");
    }

    @Async
    public void delete(String url, EFileType type) {
        String serviceUrl = host + "/api/v1/file/delete/" + type.getType();

        FileReq request = new FileReq();
        request.setUrl(url);

        ResponseEntity<ResDTO> response;
        try {
            response = restTemplate.postForEntity(serviceUrl, request, ResDTO.class);

            if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED) {
                log.error("file deleted successfully: " + response.getBody().getMessage());
            } else {
                log.error("failed to delete file: " + response.getBody().getMessage());
            }
        } catch (RestClientException e) {
            log.error("error when calling file deletion service: " + e.getMessage());
        }
    }

    public String update(String oldUrl, MultipartFile newFile, EFileType type) throws IOException {
        String updateUrl = host + "/api/v1/file/update/" + type.getType();

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("url", oldUrl);
        body.add("newFile", new FileSystemResource(convertMultiPartToFile(newFile)));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<ResDTO> response = restTemplate.postForEntity(updateUrl, requestEntity, ResDTO.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            ResDTO<Map<String, String>> responseBody = response.getBody();
            return responseBody.getData().get("url");
        } else {
            throw new IOException("Failed to update file. Status: " + response.getStatusCode());
        }
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
