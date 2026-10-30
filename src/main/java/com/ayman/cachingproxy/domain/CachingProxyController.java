package com.ayman.cachingproxy.domain;

import com.ayman.cachingproxy.service.CachingProxyService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping
public class CachingProxyController {
    private final CachingProxyService cachingProxyService;

    private final RestClient restClient;
    private final String origin;

    public CachingProxyController(CachingProxyService cachingProxyService, @Qualifier("restClientBuilder") RestClient.Builder restClientBuilder, @Value("${args.origin}") String origin) {
        this.cachingProxyService = cachingProxyService;
        this.restClient = restClientBuilder.build();
        this.origin = origin;
    }

    @GetMapping("/_proxy/clear-cache")
    public ResponseEntity<String> clearCache() {
        return ResponseEntity.ok(cachingProxyService.clearCache());
    }

    @GetMapping("/**")
    public ResponseEntity<byte[]> sendRequest(
            @RequestHeader HttpHeaders headers,
            HttpServletRequest request
    ) {
        String queryString = request.getQueryString() != null ? "?" + request.getQueryString() : "";
        String key = cachingProxyService.buildKey(origin, request.getRequestURI(), queryString);

        ResponseEntity<byte[]> cachedResponse = cachingProxyService.checkCache(key);
        if (cachedResponse != null) {
            return ResponseEntity.status(cachedResponse.getStatusCode())
                    .headers(cachedResponse.getHeaders())
                    .header("X-Cache", "HIT")
                    .body(cachedResponse.getBody());
        }
        ResponseEntity<byte[]> response = restClient.get()
                .uri(origin + request.getRequestURI() + queryString)
                .headers(h -> h.addAll(headers))
                .retrieve()
                .toEntity(byte[].class);
        cachingProxyService.addToCache(key, response);

        return ResponseEntity.status(response.getStatusCode())
                .headers(response.getHeaders())
                .header("X-Cache", "MISS")
                .body(response.getBody());
    }
}
