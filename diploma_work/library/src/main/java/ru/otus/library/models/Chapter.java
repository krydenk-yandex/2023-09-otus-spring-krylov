package ru.otus.library.models;

import java.util.UUID;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "chapters")
public class Chapter implements Persistable<UUID> {

    @Id
    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "order_number", nullable = false)
    private Integer orderNumber;

    @ManyToOne(targetEntity = Book.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;


    @Nullable
    @OneToOne(targetEntity = Chapter.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "next_chapter_uuid", referencedColumnName = "uuid")
    private Chapter nextChapter;

    @Nullable
    @OneToOne(targetEntity = Chapter.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "prev_chapter_uuid", referencedColumnName = "uuid")
    private Chapter prevChapter;

    @Transient
    private boolean isNew = true;

    public Chapter(
            UUID uuid,
            Integer orderNumber,
            String title,
            String text,
            Book book,
            @Nullable Chapter prevChapter,
            @Nullable Chapter nextChapter
    ) {
        super();
        this.uuid = uuid;
        this.title = title;
        this.orderNumber = orderNumber;
        this.text = text;
        this.book = book;
        this.prevChapter = prevChapter;
        this.nextChapter = nextChapter;
    }

    public Chapter(UUID uuid, Integer orderNumber, String title, String text, Book book) {
        super();
        this.uuid = uuid;
        this.orderNumber = orderNumber;
        this.title = title;
        this.text = text;
        this.book = book;
    }

    @Override
    public UUID getId() {
        return isNew ? null : uuid;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    public void setIsNew(Boolean value) {
        isNew = value;
    }

    @PrePersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }
}
