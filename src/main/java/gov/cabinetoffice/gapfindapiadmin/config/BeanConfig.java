package gov.cabinetoffice.gapfindapiadmin.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.apigateway.ApiGatewayClient;

@RequiredArgsConstructor
@Configuration
public class BeanConfig {

	private final AwsClientConfig awsClientConfig;

	@Bean
	public AwsCredentialsProvider awsCredentialsProvider() {
		return StaticCredentialsProvider
			.create(AwsBasicCredentials.create(awsClientConfig.getAccessKeyId(), awsClientConfig.getSecretKey()));
	}

	@Bean
	public Region region() {
		return Region.of(awsClientConfig.getRegion());
	}

	@Bean
	public ApiGatewayClient apiGatewayClient() {
		return ApiGatewayClient.builder().region(region()).credentialsProvider(awsCredentialsProvider()).build();
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

}
