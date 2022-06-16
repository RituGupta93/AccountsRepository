package com.bank.accountdata.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableCaching
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationEntryPointHandling authenticationEntryPoint;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Value("${user1.name}")
	private String userName;

	@Value("${admin.name}")
	private String adminName;

	@Value("${user1.password}")
	private String userPassword;

	@Value("${admin.password}")
	private String adminPassword;

	@Value("${user1.role}")
	private String userRole;

	@Value("${admin.role}")
	private String adminRole;

	@Bean("jwtUserDetailsService")
	public UserDetailsService users() {
		UserDetails user = User.builder().username(userName).password(userPassword).roles(userRole).build();
		UserDetails admin = User.builder().username(adminName).password(adminPassword).roles(adminRole).build();
		return new InMemoryUserDetailsManager(user, admin);
	}

	@Bean
	public CacheManager cacheManager() {
		return new ConcurrentMapCacheManager("userToken");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeRequests().antMatchers("/bank/login").permitAll().antMatchers("/bank/logout").permitAll()
				.antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**").permitAll()
				.anyRequest().authenticated().and().exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().formLogin().disable().httpBasic()
				.disable().logout().disable().csrf().ignoringAntMatchers("/bank/**");

		// Add a filter to validate the tokens with every request
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}

	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(users()).passwordEncoder(passwordEncoder());
	}

}
