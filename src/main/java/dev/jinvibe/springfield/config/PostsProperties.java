package dev.jinvibe.springfield.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring-field.posts")
public record PostsProperties(String location) {
}
