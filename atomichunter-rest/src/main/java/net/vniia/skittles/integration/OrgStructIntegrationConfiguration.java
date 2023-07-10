package net.vniia.skittles.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrgStructIntegrationConfiguration {

    @Bean
    IOrgStructIntegrationService orgStructIntegrationService() {
        return new DummyOrgStructIntegrationService();
    }

}
