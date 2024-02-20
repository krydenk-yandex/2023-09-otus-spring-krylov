package ru.otus.hw15.integration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import ru.otus.hw15.models.DeployResult;
import ru.otus.hw15.models.Notification;
import ru.otus.hw15.models.TestResult;
import ru.otus.hw15.services.CiCdService;
import ru.otus.hw15.services.NotificationService;

@Configuration
public class CiCdPipeline {

    @Bean
    public MessageChannelSpec<?, ?> ciCdInput() {
        return MessageChannels.direct();
    }

    @Bean
    public MessageChannelSpec<?, ?> ciCdOutput() {
        return MessageChannels.direct();
    }

    @Bean
    public MessageChannelSpec<?, ?> notificationsChannel() {
        return MessageChannels.direct();
    }

    @Bean
    public IntegrationFlow notificationsFlow(NotificationService notificationService) {
        return IntegrationFlow.from(notificationsChannel())
                .<DeployResult>filter(res -> !res.isSuccess())
                .<DeployResult, List<Notification>>transform(deployResult -> deployResult
                        .getArtifact().getSource().getOwners().stream().map(user ->
                            new Notification(
                                user,
                                deployResult.getOutput()
                            )).toList()
                    )
                .split()
                .handle(notificationService, "notifyUser")
                .get();
    }

    @Bean
    public IntegrationFlow ciCdFlow(CiCdService ciCdService) {
        return IntegrationFlow.from(ciCdInput())
                .split()
                .handle(ciCdService, "build")
                .handle(ciCdService, "test")
                .<TestResult>filter(testResult -> {
                    var areTestsPassed = testResult.isPassed();
                    System.out.println(areTestsPassed
                        ? "Tests have been passed" : "Tests haven't been passed, cancelling deploy");
                    return areTestsPassed;
                })
                .transform(TestResult::getArtifact)
                .handle(ciCdService, "deploy")
                .routeToRecipients(route -> {
                    route.recipient("notificationsChannel");
                    route.recipient("ciCdOutput");
                })
                .get();
    }
}

