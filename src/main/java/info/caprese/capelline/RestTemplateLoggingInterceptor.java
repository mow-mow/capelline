package info.caprese.capelline;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.UUID;

@Slf4j
public class RestTemplateLoggingInterceptor implements ClientHttpRequestInterceptor {


    @Override
    public ClientHttpResponse intercept(HttpRequest request,
                                        byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        val requestUuid = UUID.randomUUID();
        logRequest(requestUuid, request, body);
        val response = execution.execute(request, body);
        logResponse(requestUuid, response);
        return response;
    }

    private void logRequest(UUID requestUuid, HttpRequest request, byte[] body) {
        if (log.isInfoEnabled()) {
            log.info("[API:Request({})] Request=[{}:{}], Headers=[{}], Body=[{}]",
                    requestUuid,
                    request.getMethod(),
                    request.getURI(),
                    request.getHeaders(),
                    new String(body)
            );
        }
    }

    private void logResponse(UUID requestUuid, ClientHttpResponse response) throws IOException {
        if (log.isInfoEnabled()) {
            log.info("[API:Response({})] Status=[{}:{}], Headers=[{}], Body=[{}]",
                    requestUuid,
                    response.getStatusCode().value(),
                    response.getStatusText(),
                    response.getHeaders(),
                    StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8)
            );
        }
    }
}