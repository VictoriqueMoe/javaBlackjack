package moe.victorique.blackjack.engine;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import moe.victorique.blackjack.annotations.DeviceId;
import moe.victorique.blackjack.utils.Utils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

import java.security.NoSuchAlgorithmException;

public class RequestIpArgumentResolverEngine implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(@NonNull final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(DeviceId.class);
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

        final var ipAddress = getIpAddress(request);
        final var ua = request.getHeader(HttpHeaders.USER_AGENT);
        return getDeviceId(ipAddress, ua);
    }

    private String getIpAddress(final @NonNull HttpServletRequest request) {
        var ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    private String getDeviceId(final @NonNull String ip, final @NonNull String userAgent) {
        try {
            return Utils.deviceHash(ip, userAgent);
        } catch (final NoSuchAlgorithmException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }
}
