package account.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;

import static account.enums.Role.*;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final BeansConfig config;
    private final UserDetailsService service;

    @Qualifier("delegatedAuthenticationEntryPoint")
    private final AuthenticationEntryPoint entryPoint;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(service)
                .passwordEncoder(config.getEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/actuator/shutdown", "/h2/**").permitAll()
                .antMatchers(HttpMethod.POST, "/actuator/shutdown", "/h2/**", "/api/auth/signup").permitAll()
                .antMatchers(HttpMethod.POST, "/api/auth/changepass").hasAnyAuthority(ROLE_USER.name(), ROLE_ACCOUNTANT.name(), ROLE_ADMINISTRATOR.name())
                .antMatchers(HttpMethod.GET, "/api/empl/payment").hasAnyAuthority(ROLE_USER.name(), ROLE_ACCOUNTANT.name())
                .antMatchers(HttpMethod.POST, "/api/acct/payments").hasAuthority(ROLE_ACCOUNTANT.name())
                .antMatchers(HttpMethod.PUT, "/api/acct/payments").hasAuthority(ROLE_ACCOUNTANT.name())
                .antMatchers(HttpMethod.GET, "/api/admin/user").hasAuthority(ROLE_ADMINISTRATOR.name())
                .antMatchers(HttpMethod.DELETE, "/api/admin/user/**").hasAuthority(ROLE_ADMINISTRATOR.name())
                .antMatchers(HttpMethod.PUT, "/api/admin/user/**").hasAuthority(ROLE_ADMINISTRATOR.name())
                .antMatchers(HttpMethod.GET, "/api/security/events").hasAuthority(ROLE_AUDITOR.name())
                .antMatchers("/**").authenticated()
                .and()
                .httpBasic()
                .authenticationEntryPoint(entryPoint)
                .and()
                .csrf().disable().headers().frameOptions().disable()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().accessDeniedHandler(config.accessDeniedHandler());
    }
}
