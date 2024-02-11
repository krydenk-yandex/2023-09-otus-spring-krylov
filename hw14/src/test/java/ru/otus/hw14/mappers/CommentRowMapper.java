package ru.otus.hw14.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import ru.otus.hw14.models.CommentJdbcTest;

public class CommentRowMapper implements RowMapper<CommentJdbcTest> {
    @Override
    public CommentJdbcTest mapRow(ResultSet rs, int rowNum) throws SQLException {
        var comment = new CommentJdbcTest();
        comment.setId(rs.getLong("id"));
        comment.setText(rs.getString("text"));
        comment.setAuthorName(rs.getString("author_name"));
        comment.setBookTitle(rs.getString("book_title"));

        return comment;
    }
}