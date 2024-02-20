package ru.otus.hw15.services;

import ru.otus.hw15.models.Notification;

public interface NotificationService {
    boolean notifyUser(Notification notification);
}
