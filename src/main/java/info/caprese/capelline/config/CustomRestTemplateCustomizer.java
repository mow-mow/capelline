package info.caprese.capelline.config;

import info.caprese.capelline.RestTemplateLoggingInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;



@Slf4j
public class CustomRestTemplateCustomizer implements RestTemplateCustomizer {
    @Autowired
    private RestTemplateLoggingInterceptor restTemplateLoggingInterceptor;

    @Override
    public void customize(RestTemplate restTemplate) {
      log.info("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
        restTemplate.getInterceptors().add(restTemplateLoggingInterceptor);
    }
}
