package com.ayman.cachingproxy;

import com.ayman.cachingproxy.service.CachingProxyService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CachingProxyApplication implements ApplicationRunner {
	private final CachingProxyService cachingProxyService;

	public CachingProxyApplication(CachingProxyService cachingProxyService) {
		this.cachingProxyService = cachingProxyService;
	}

	public static void main(String[] args) {
		SpringApplication.run(CachingProxyApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) {
		if (args.containsOption("clear-cache")) cachingProxyService.clearCache();
	}

}
