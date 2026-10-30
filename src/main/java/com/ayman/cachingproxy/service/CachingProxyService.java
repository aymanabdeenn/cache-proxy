package com.ayman.cachingproxy.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CachingProxyService {
    private final Map<String, ResponseEntity<byte[]>> cache;

    public CachingProxyService() {
        this.cache = new ConcurrentHashMap<>();
    }

    public ResponseEntity<byte[]> checkCache(String key) {
        return cache.get(key);
    }

    public void addToCache(String key, ResponseEntity<byte[]> response) {
        cache.put(key, response);
    }

    public String clearCache() {
        cache.clear();
        return "Cache has been cleared successfully.";
    }

    public String buildKey(String origin, String path, String query) {
        return origin + path + query;
    }
}
