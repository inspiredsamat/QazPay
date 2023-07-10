package kz.inspiredsamat.qazpay.security;

import kz.inspiredsamat.qazpay.filter.CustomAuthenticationFilter;
import kz.inspiredsamat.qazpay.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@SuppressWarnings("ALL")
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(
                authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/qazpay/api/login");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers("/qazpay/api/login/**").permitAll();
        http.authorizeRequests().antMatchers("/qazpay/api/users/register/**").permitAll();
        http.authorizeRequests().antMatchers(POST, "/qazpay/api/transfer/**").hasAnyAuthority("USER");
        http.authorizeRequests().antMatchers(POST, "/qazpay/api/accounts/**").hasAuthority("USER");
        http.authorizeRequests().antMatchers(GET, "/qazpay/api/accounts/{id}").hasAnyAuthority("USER", "ADMIN");
        http.authorizeRequests().antMatchers(GET, "/qazpay/api/users/{id}").hasAnyAuthority("ADMIN", "USER");
        http.authorizeRequests().antMatchers(GET, "/qazpay/api/transfers/{id}").hasAnyAuthority("ADMIN", "USER");
        http.authorizeRequests().antMatchers(GET, "/qazpay/api/users/**").hasAuthority("ADMIN");
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
