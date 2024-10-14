package moe.victorique.blackjack.config;

import lombok.NonNull;
import moe.victorique.blackjack.engine.RequestDeviceIdArgumentResolverEngine;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(final @NonNull List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new RequestDeviceIdArgumentResolverEngine());
    }
}
