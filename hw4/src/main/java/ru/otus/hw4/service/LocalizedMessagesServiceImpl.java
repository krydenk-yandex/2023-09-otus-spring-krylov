package ru.otus.hw4.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.otus.hw4.config.LocaleConfig;

@RequiredArgsConstructor
@Service()
@Primary
public class LocalizedMessagesServiceImpl implements LocalizedMessagesService {
    private final MessageSource messageSource;

    private final LocaleConfig localeConfig;

    @Override
    public String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, localeConfig.getLocale());
    }
}
