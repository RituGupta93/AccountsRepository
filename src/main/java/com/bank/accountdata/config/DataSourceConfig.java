package com.bank.accountdata.config;

import java.beans.PropertyVetoException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * DataSource configuration for MS Access DB
 * 
 */
@Configuration
public class DataSourceConfig {

	@Value("${jdbc.url}")
	String url;

	@Value("${jdbc.driver}")
	String driver;

	@Bean
	public DataSource createDataSource() throws PropertyVetoException {
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		dataSource.setJdbcUrl(url);
		dataSource.setDriverClass(driver);
		return dataSource;
	}

}