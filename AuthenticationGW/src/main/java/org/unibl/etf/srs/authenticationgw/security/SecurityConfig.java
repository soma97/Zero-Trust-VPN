package org.unibl.etf.srs.authenticationgw.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import java.net.Inet4Address;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
		http.requiresChannel()
				.anyRequest()
				.requiresSecure();

    	http.authorizeRequests()
        .antMatchers("/auth/**").hasIpAddress(Inet4Address.getByName("access.ctr").getHostAddress())
        .antMatchers("/**").permitAll();
    	
    	http.csrf().disable();
    }

}