package com.adaptive.quiz.config;

import com.adaptive.quiz.controller.Query;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import static java.nio.charset.StandardCharsets.UTF_8;

@Configuration
public class QueriesConfig {

    @Value("classpath:queries.json")
    private Resource resource;

    @Bean(name = "allQueries")
    public Query[] queries() {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return new Gson().fromJson(reader, Query[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
