package ru.otus.hw14.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import ru.otus.hw14.models.BookJdbcTest;

public class BookRowMapper implements RowMapper<BookJdbcTest> {
    @Override
    public BookJdbcTest mapRow(ResultSet rs, int rowNum) throws SQLException {
        var book = new BookJdbcTest();
        book.setId(rs.getLong("id"));
        book.setTitle(rs.getString("title"));
        book.setAuthorName(rs.getString("author_name"));
        book.setGenreName(rs.getString("genre_name"));

        return book;
    }
}