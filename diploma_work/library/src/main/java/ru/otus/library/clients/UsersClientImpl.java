package ru.otus.library.clients;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import jakarta.annotation.Nullable;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.otus.library.dto.LoginPasswordDto;
import ru.otus.library.dto.UserDto;

@Component
public class UsersClientImpl implements UsersClient {

    private final RestOperations rest = new RestTemplate();

    @Value("${services.users.url}")
    @Setter
    private String usersUrl;

    @Override
    public Optional<UserDto> getUser(String token) {
        try {
            var response = this.<UserDto, Void>request(
                    token,
                    HttpMethod.GET,
                    null,
                    "/api/users/info",
                    UserDto.class
            );

            return response.getStatusCode().is2xxSuccessful()
                    ? Optional.ofNullable(response.getBody())
                    : Optional.empty();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                return Optional.empty();
            } else {
                throw e;
            }
        }
    }

    @Override
    public Optional<String> login(LoginPasswordDto dto) {
        var response = this.<String, LoginPasswordDto>request(
                null,
                HttpMethod.POST,
                dto,
                "/api/auth/token",
                String.class
        );

        return response.getStatusCode().is2xxSuccessful()
                ? Optional.ofNullable(response.getBody())
                : Optional.empty();
    }

    private <R, B> ResponseEntity<R> request(
            String token,
            HttpMethod httpMethod,
            @Nullable B body,
            String path,
            Class<R> responseType
    ) {
        URI uri = UriComponentsBuilder.fromHttpUrl(usersUrl)
                .path(path)
                .build().toUri();

        var headers = new HttpHeaders();
        if (token != null) {
            headers.put(HttpHeaders.AUTHORIZATION, List.of("Bearer " + token));
        }

        RequestEntity<B> requestEntity = new RequestEntity<B>(
                body,
                headers,
                httpMethod,
                uri
        );

        return rest.<R>exchange(
                requestEntity,
                responseType
        );
    }
}
