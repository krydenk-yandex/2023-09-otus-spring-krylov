package ru.otus.hw15.integration;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw15.models.BuildArtifact;
import ru.otus.hw15.models.DeployResult;
import ru.otus.hw15.models.Notification;
import ru.otus.hw15.models.SourceCodeRepository;
import ru.otus.hw15.models.TestResult;
import ru.otus.hw15.services.CiCdService;
import ru.otus.hw15.services.NotificationService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@SpringBootTest
@DisplayName("Флоу CI/CD ")
public class CiCdPipelineTest {
    @MockBean
    CiCdService ciCdService;

    @MockBean
    NotificationService notificationService;

    @Autowired
    CiCdGateway gateway;

    @Test
    @DisplayName(" должен завершиться корректно")
    void success() {
        var source =  new SourceCodeRepository(
                "testRepository",
                List.of("user1", "user2")
        );
        var artifact = new BuildArtifact(
               source,
                "testFile"
        );
        var testResult = new TestResult(true, artifact);
        var deployResult = new DeployResult(true, artifact, "ok");

        given(ciCdService.build(any())).willReturn(artifact);
        given(ciCdService.test(any())).willReturn(testResult);
        given(ciCdService.deploy(any())).willReturn(deployResult);

        gateway.process(List.of(source));

        verify(ciCdService, times(1)).build(source);
        verify(ciCdService, times(1)).test(artifact);
        verify(ciCdService, times(1)).deploy(artifact);
        verifyNoInteractions(notificationService);
    }

    @Test
    @DisplayName(" должен завершиться c ошибкой - не прошли тесты")
    void testsFailed() {
        var source =  new SourceCodeRepository(
                "testRepository",
                List.of("user1", "user2")
        );
        var artifact = new BuildArtifact(
               source,
                "testFile"
        );
        var testResult = new TestResult(false, artifact);

        given(ciCdService.build(any())).willReturn(artifact);
        given(ciCdService.test(any())).willReturn(testResult);

        gateway.process(List.of(source));

        verify(ciCdService, times(1)).build(source);
        verify(ciCdService, times(1)).test(artifact);
        verify(ciCdService, times(0)).deploy(artifact);
        verifyNoInteractions(notificationService);
    }

    @Test
    @DisplayName(" должен завершиться c ошибкой - проблемы с деплоем")
    void deployFailed() {
        var source =  new SourceCodeRepository(
                "testRepository",
                List.of("user1", "user2")
        );
        var artifact = new BuildArtifact(
               source,
                "testFile"
        );
        var testResult = new TestResult(true, artifact);
        var deployResult = new DeployResult(false, artifact, "fail");
        var notification1 = new Notification("user1", "fail");
        var notification2 = new Notification("user2", "fail");

        given(ciCdService.build(any())).willReturn(artifact);
        given(ciCdService.test(any())).willReturn(testResult);
        given(ciCdService.deploy(any())).willReturn(deployResult);
        given(notificationService.notifyUser(any())).willReturn(true);

        gateway.process(List.of(source));

        verify(ciCdService, times(1)).build(source);
        verify(ciCdService, times(1)).test(artifact);
        verify(ciCdService, times(1)).deploy(artifact);
        verify(notificationService, times(1)).notifyUser(argThat(
                n -> n.getUserName().equals(notification1.getUserName())
                    && n.getMessage().equals(notification1.getMessage())
        ));
        verify(notificationService, times(1)).notifyUser(argThat(
                n -> n.getUserName().equals(notification2.getUserName())
                    && n.getMessage().equals(notification2.getMessage())
        ));
    }
}

