package kr.co.fitpet.health.config

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.CacheKeyPrefix
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration


@Configuration
@EnableCaching
class CustomRedisCacheConfiguration {

    val log = KotlinLogging.logger {}

    @Value("\${spring.cash.default_expire_sec}")
    private var DEFAULT_EXPIRE_SEC: Long = 60*60*12 // 12Hour

    @Bean
    fun cacheManagerCustomizer(redisConnectionFactory: RedisConnectionFactory): RedisCacheManager {

        log.info { "DEFAULT_EXPIRE_SEC: $DEFAULT_EXPIRE_SEC" }

        val configuration: RedisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
            .disableCachingNullValues()
            .entryTtl(Duration.ofSeconds(DEFAULT_EXPIRE_SEC))
            .computePrefixWith(CacheKeyPrefix.simple())
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair
                    .fromSerializer(StringRedisSerializer())
            )
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair
                    .fromSerializer(GenericJackson2JsonRedisSerializer())
            )

//        var cacheConfiguration= mutableMapOf<String, RedisCacheConfiguration>()
//
//        cacheConfiguration[CacheKey.ZONE] = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(CacheKey.ZONE_EXPIRE_SEC))

        return RedisCacheManager.RedisCacheManagerBuilder
            .fromConnectionFactory(redisConnectionFactory)
            .cacheDefaults(configuration)
//            .withInitialCacheConfigurations(cacheConfiguration)
            .build()
    }
}

