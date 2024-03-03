package ru.otus.library.utils;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class AuthUtils {
    public static Optional<String> getJwtToken() {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest servletRequest = requestAttributes.getRequest();

        return Optional.ofNullable(servletRequest.getHeader("Authorization"))
                .map(headerValue -> headerValue.replaceFirst("Bearer ", ""));
    }
}
