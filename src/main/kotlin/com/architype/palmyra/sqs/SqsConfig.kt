package com.architype.palmyra.sqs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsAsyncClient
import java.net.URI

@Configuration(proxyBeanMethods = false)
class SqsConfig {

    // Test用にさっくりlocalstack設定
    @Bean
    fun sqsAsyncClient(): SqsAsyncClient =
        SqsAsyncClient.builder()
            .region(Region.AP_NORTHEAST_1)
            .credentialsProvider(DefaultCredentialsProvider.create())
            .endpointOverride(URI("http://localhost:4566"))
            .build()
}