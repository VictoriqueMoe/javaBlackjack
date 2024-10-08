package moe.victorique.blackjack.engine;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import moe.victorique.blackjack.annotations.RequestIp;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class RequestIpArgumentResolverEngine implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(@NonNull final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestIp.class);
    }

    @Override
    public Object resolveArgument(
            @NonNull final MethodParameter parameter,
            @Nullable final ModelAndViewContainer mavContainer,
            @NonNull final NativeWebRequest webRequest,
            @Nullable final WebDataBinderFactory binderFactory
    ) {
        final var request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            return null;
        }

        var ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }

        return ipAddress;
    }
}
