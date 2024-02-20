package ru.otus.hw15.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw15.models.BuildArtifact;
import ru.otus.hw15.models.DeployResult;

import java.util.Random;
import ru.otus.hw15.models.SourceCodeRepository;
import ru.otus.hw15.models.TestResult;

@RequiredArgsConstructor
@Service
public class CiCdServiceImpl implements CiCdService {
    @Override
    public BuildArtifact build(SourceCodeRepository repository) {
        System.out.println(String.format("Building source from %s", repository.getUrl()));
        return new BuildArtifact(
                repository,
                "artifact" + (new Random()).nextLong()
        );
    }

    @Override
    public TestResult test(BuildArtifact artifact) {
        System.out.println(String.format("Testing build artifact %s", artifact.getFileName()));

        var isPassed = (new Random()).nextBoolean();

        return new TestResult(
                isPassed,
                artifact
        );
    }

    @Override
    public DeployResult deploy(BuildArtifact artifact) {
        System.out.println(String.format("Deploying artifact %s", artifact.getFileName()));

        var isDeploySuccessful = (new Random()).nextBoolean();

        return new DeployResult(
                isDeploySuccessful,
                artifact,
                isDeploySuccessful ? "All right, everything has been deployed"
                        : "Something went wrong, an error occurred on deploy stage"
        );
    }
}
