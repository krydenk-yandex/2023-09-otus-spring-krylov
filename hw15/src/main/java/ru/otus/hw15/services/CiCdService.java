package ru.otus.hw15.services;

import ru.otus.hw15.models.BuildArtifact;
import ru.otus.hw15.models.DeployResult;
import ru.otus.hw15.models.SourceCodeRepository;
import ru.otus.hw15.models.TestResult;

public interface CiCdService {
    BuildArtifact build(SourceCodeRepository repository);

    TestResult test(BuildArtifact artifact);

    DeployResult deploy(BuildArtifact artifact);
}
