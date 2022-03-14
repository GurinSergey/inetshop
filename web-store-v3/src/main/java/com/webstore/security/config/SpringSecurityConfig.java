package com.webstore.security.config;

import com.webstore.metrics.ActiveUserStore;
import com.webstore.security.services.TokenAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthProvider authProvider;
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;
    @Autowired
    private TokenAuthService tokenAuthService;
    @Autowired
    private CorsConfig corsConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .addFilterBefore(new StatelessAuthenticationFilter(tokenAuthService), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and()
                .authorizeRequests().antMatchers("/login", "/confirm_mail", "/logoutMe", "/registry", "/sendpass", "/basket/**","checkout/**",
                "/product/**", "/{\\d+}/product/**", "/sessionStorage/**", "/orders/addOrder", "/templates/**", "/catalog/**", "/compare/**", "/search/**").permitAll().and()
                .authorizeRequests().mvcMatchers("**/**").fullyAuthenticated().and()
                .logout().logoutSuccessHandler((new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))).and()
                .authenticationProvider(authProvider)
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsFilter corsFilter() {
        corsConfig.setAllowedCredentials(true);
        corsConfig.setAllowedMethods(new String[]{"*"});
        corsConfig.setAllowedHeaders(new String[]{"*"});
        corsConfig.setAllowedOrigin(new String[]{"*"});

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(corsConfig.isAllowedCredentials());
        for (String allowedHeader : corsConfig.getAllowedOrigin()) {
            config.addAllowedOrigin(allowedHeader);
        }
        for (String allowedHeader : corsConfig.getAllowedHeaders()) {
            config.addAllowedHeader(allowedHeader);
        }
        for (String alloweMethod : corsConfig.getAllowedMethods()) {
            config.addAllowedMethod(alloweMethod);
        }
        source.registerCorsConfiguration("/logoutMe", config);
        return new CorsFilter(source);
    }

    @Bean
    public ProviderManager authenticationManager() {
        List<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(authProvider);
        return new ProviderManager(providers);
    }

    @Bean
    public ActiveUserStore activeUserStore(){
        return new ActiveUserStore();
    }
}