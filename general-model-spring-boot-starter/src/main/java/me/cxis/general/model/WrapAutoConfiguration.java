package me.cxis.general.model;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(WrapProperties.class)
public class WrapAutoConfiguration {
}
