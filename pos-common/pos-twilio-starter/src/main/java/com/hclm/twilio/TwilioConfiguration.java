package com.hclm.twilio;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfiguration {
    @ConfigurationProperties(prefix = "twilio")
    @Bean
    public TwilioProperties twilioProperties() {
        return new TwilioProperties();
    }

    /**
     * twilio工具
     *
     * @return {@link TwilioUtil }
     */
    @Bean
    public TwilioUtil twilioUtil(TwilioProperties twilioProperties) {
        return new TwilioUtil(twilioProperties);
    }
}
