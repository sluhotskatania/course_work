package com.example.course_work.resolver;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

@Component
public class MultiLevelCacheResolver implements CacheResolver {
    private final CacheManager localCacheManager;

    public MultiLevelCacheResolver(CacheManager localCacheManager) {
        this.localCacheManager = localCacheManager;
    }

    @Override
    public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
        return Stream.of(
                        localCacheManager.getCache("clients"),
                        localCacheManager.getCache("accommodations"),
                        localCacheManager.getCache("bookings"),
                        localCacheManager.getCache("guides"),
                        localCacheManager.getCache("tours")
                )
                .filter(Objects::nonNull)
                .toList();
    }
}
