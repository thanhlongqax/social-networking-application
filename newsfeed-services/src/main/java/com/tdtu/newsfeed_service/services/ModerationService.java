package com.tdtu.newsfeed_service.services;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdtu.newsfeed_service.dtos.respone.ModerationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;

import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Service
@RequiredArgsConstructor
@Slf4j
public class ModerationService {
    @Value("${sight.engine.api.key.user}")
    private String userKey;
    @Value("${sight.engine.api.key.secret}")
    private String secretKey;
    @Value("${sight.engine.get.url}")
    private String requestUrl;
    @Value("${sight.engine.workflow}")
    private String workflow;

    public boolean moderateImage(String mediaUrl){
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(requestUrl);

            URI uri = fromUriString(requestUrl)
                    .queryParam("url", mediaUrl)
                    .queryParam("workflow", workflow)
                    .queryParam("api_user", userKey)
                    .queryParam("api_secret", secretKey)
                    .build()
                    .encode()
                    .toUri();

            httpGet.setURI(uri);

            CloseableHttpResponse response = httpClient.execute(httpGet);
            String responseBody = EntityUtils.toString(response.getEntity());

            if(response.getStatusLine().getStatusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    ModerationResponse moderationResponse = mapper.readValue(responseBody, ModerationResponse.class);
                    if(moderationResponse.getSummary().getAction().equals("accept")){
                        return true;
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }else {
                log.info(responseBody + "status code: " + response.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
            log.info(e.getMessage());
        }

        return false;
    }
}
