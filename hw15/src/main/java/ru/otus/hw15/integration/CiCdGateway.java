package ru.otus.hw15.integration;

import java.util.List;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw15.models.DeployResult;
import ru.otus.hw15.models.SourceCodeRepository;

@MessagingGateway
public interface CiCdGateway {

	@Gateway(requestChannel = "ciCdInput", replyChannel = "ciCdOutput")
	List<DeployResult> process(List<SourceCodeRepository> source);
}
