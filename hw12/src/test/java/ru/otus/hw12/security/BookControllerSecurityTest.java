package ru.otus.hw12.security;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw12.controllers.BookController;
import ru.otus.hw12.converters.BookConverter;
import ru.otus.hw12.services.AuthorService;
import ru.otus.hw12.services.BookService;
import ru.otus.hw12.services.GenreService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import(SecurityConfiguration.class)
@DisplayName("Доступ к контроллеру книг ")
public class BookControllerSecurityTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;
    @MockBean
    private GenreService genreService;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private BookConverter bookConverter;

    @ParameterizedTest()
    @MethodSource("getAuthenticatedRequestResponseList")
    @DisplayName(" должен быть у аутентифицированных пользователей по всем страницам")
    @WithMockUser(
            username = "admin",
            authorities = {"ADMIN"}
    )
    public void authenticatedAccess(Pair<Request, Response> arg) throws Exception {
        mvcPerformAndCheck(arg);
    }

    private static List<Pair<Request, Response>> getAuthenticatedRequestResponseList() {
        return List.of(
                Pair.of(new Request(HttpMethod.GET, "/books"),
                        new Response(HttpStatus.OK, null)),
                Pair.of(new Request(HttpMethod.GET, "/books/edit/123"),
                        new Response(HttpStatus.BAD_REQUEST, null)),
                Pair.of(new Request(HttpMethod.POST, "/books/edit/123"),
                        new Response(HttpStatus.BAD_REQUEST, null)),
                Pair.of(new Request(HttpMethod.GET, "/books/create"),
                        new Response(HttpStatus.OK, null)),
                Pair.of(new Request(HttpMethod.POST, "/books/create"),
                        new Response(HttpStatus.OK, null)),
                Pair.of(new Request(HttpMethod.POST, "/books/delete/123"),
                        new Response(HttpStatus.FOUND, "/books"))
        );
    }

    @ParameterizedTest()
    @MethodSource("getUnauthenticatedRequestResponseList")
    @DisplayName("должен быть закрыт по всем страницам для неаутентифицированных пользователей")
    public void notAuthenticatedAccess(Pair<Request, Response> arg) throws Exception {
        mvcPerformAndCheck(arg);
    }

    private static List<Pair<Request, Response>> getUnauthenticatedRequestResponseList() {
        return List.of(
                Pair.of(new Request(HttpMethod.GET, "/books"),
                        new Response(HttpStatus.FOUND, "http://localhost/login")),
                Pair.of(new Request(HttpMethod.GET, "/books/edit/123"),
                        new Response(HttpStatus.FOUND, "http://localhost/login")),
                Pair.of(new Request(HttpMethod.POST, "/books/edit/123"),
                        new Response(HttpStatus.FOUND, "http://localhost/login")),
                Pair.of(new Request(HttpMethod.GET, "/books/create"),
                        new Response(HttpStatus.FOUND, "http://localhost/login")),
                Pair.of(new Request(HttpMethod.POST, "/books/create"),
                        new Response(HttpStatus.FOUND, "http://localhost/login")),
                Pair.of(new Request(HttpMethod.POST, "/books/delete/123"),
                        new Response(HttpStatus.FOUND, "http://localhost/login"))
        );
    }

    private void mvcPerformAndCheck(Pair<Request, Response> pair) throws Exception {
        var request = pair.getFirst();
        var expectedResponse = pair.getSecond();

        var response = mvc.perform(MockMvcRequestBuilders.request(
                request.method,
                request.url
        ));
        response.andExpect(status().is(expectedResponse.status.value()));

        if (expectedResponse.redirectUrl != null) {
            response.andExpect(redirectedUrl(expectedResponse.redirectUrl));
        }
    }

    private record Request(HttpMethod method, String url) {}
    private record Response(HttpStatus status, String redirectUrl) {}
}
