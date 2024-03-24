package ru.otus.library.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "bookmarks")
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private BookmarkType type;

    @ManyToOne(targetEntity = Chapter.class, fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "chapter_uuid", referencedColumnName = "uuid", nullable = false)
    private Chapter chapter;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    public enum BookmarkType {
        MANUAL, AUTO
    }
}