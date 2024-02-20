package ru.otus.hw15.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw15.models.Notification;

@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {
    @Override
    public boolean notifyUser(Notification notification) {
        System.out.println(String.format(
                "Sending notification '%s' to user '%s'",
                notification.getMessage(),
                notification.getUserName()
        ));

        return true;
    }
}
