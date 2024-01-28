package ru.otus.hw13.models;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "authorities")
public class Authority implements GrantedAuthority {
    @EmbeddedId
    private AuthorityId authorityId;

    @Override
    public String getAuthority() {
        return authorityId.getAuthority();
    }
}

