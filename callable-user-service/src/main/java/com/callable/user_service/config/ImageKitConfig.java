package com.callable.user_service.config;

import io.imagekit.sdk.ImageKit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@org.springframework.context.annotation.Configuration
public class ImageKitConfig {

    @Value("${image-kit.public-key}")
    String publicKey;

    @Value("${image-kit.private-key}")
    String privateKey;

    @Value("${image-kit.url-endpoint}")
    String urlEndpoint;

    @Bean
    public ImageKit imageKit() throws IOException {
        io.imagekit.sdk.config.Configuration config = new io.imagekit.sdk.config.Configuration();
        config.setPrivateKey(privateKey);
        config.setPublicKey(publicKey);
        config.setUrlEndpoint(urlEndpoint);
        ImageKit imageKit = ImageKit.getInstance();
        imageKit.setConfig(config);
        return imageKit;
    }
}