package account.configuration;

import account.exception.DefaultExceptionHandler;
import account.mapper.impl.ModelMapperImpl;
import account.service.impl.EventServiceImpl;
import account.utils.impl.EventCreatorImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
public class BeansConfig {
    public static final int STRENGTH = 13;

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder(STRENGTH);
    }

    @Bean
    ModelMapperImpl modelMappers() {
        return new ModelMapperImpl();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new DefaultExceptionHandler(
                eventCreator(),
                eventService()
        );
    }

    @Bean
    public EventCreatorImpl eventCreator() { return new EventCreatorImpl(); }

    @Bean
    public EventServiceImpl eventService() { return new EventServiceImpl(); }
}
