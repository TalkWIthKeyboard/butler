package com.okjike.data.configurations;

import com.okjike.data.models.AzkabanConfig;
import com.okjike.kejiva.config.ConfigurationFactory;
import com.okjike.kejiva.config.EnvConfigProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {

    private static final EnvConfigProvider CONFIG_PROVIDER = EnvConfigProvider.newInstance();

    @Bean
    AzkabanConfig provideJudgeConfig() {
        return new ConfigurationFactory(CONFIG_PROVIDER).initialize(AzkabanConfig.class);
    }
}
