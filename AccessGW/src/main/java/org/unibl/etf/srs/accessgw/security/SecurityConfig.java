package org.unibl.etf.srs.accessgw.security;

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
        	.anyRequest().hasIpAddress(Inet4Address.getByName("access.ctr").getHostAddress());
    	
    	http.csrf().disable();
    }

}