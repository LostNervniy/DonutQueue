package com.example.DonutQueue;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@SpringBootApplication
public class DonutQueueApplication  {
	@Value("${datasource.primary.url}")
	String primaryDSUrl;
	
	@Value("${datasource.primary.driver}")
	String primaryDSDriver;
	
	@Value("${datasource.primary.username}")
	String primaryDSUsername;
	
	@Value("${datasource.primary.password}")
	String primaryDSPassword;
	
	@Value("${datasource.secondary.url}")
	String secondaryDSUrl;
	
	@Value("${datasource.secondary.driver}")
	String secondaryDSDriver;
	
	@Value("${datasource.secondary.username}")
	String secondaryDSUsername;
	
	@Value("${datasource.secondary.password}")
	String secondaryDSPassword;
	
	public static void main(String[] args) {
		new SpringApplicationBuilder( DonutQueueApplication.class )
				.run( args );
		//SpringApplication.run( DonutQueueApplication.class, args );
	}
	
	//Thanks to: https://stackoverflow.com/questions/51205957/multiple-datasources-fallback
	@Primary
	@Bean
	public DataSource getDataSource(
			@Qualifier("first") DataSourceProperties first,
			@Qualifier("second") DataSourceProperties second
			){
		int i = 0;
		final DataSource firstDataSource = first.initializeDataSourceBuilder().build();
		final DataSource secondDataSource = second.initializeDataSourceBuilder().build();
		try{
			firstDataSource.getConnection();
			return firstDataSource;
		}catch (Exception e){
			return secondDataSource;
		}
	}
	
	@Primary
	@Bean("first")
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSourceProperties primaryDataSource(){
		final DataSourceProperties dataSourceProperties = new DataSourceProperties();
		dataSourceProperties.setUrl( primaryDSUrl );
		dataSourceProperties.setDriverClassName( primaryDSDriver);
		dataSourceProperties.setUsername(primaryDSUsername);
		dataSourceProperties.setPassword( primaryDSPassword);
		return dataSourceProperties;
		
	}
	
	@Bean("second")
	public DataSourceProperties secondaryDataSource(){
		final DataSourceProperties dataSourceProperties = new DataSourceProperties();
		dataSourceProperties.setUrl( secondaryDSUrl );
		dataSourceProperties.setDriverClassName( secondaryDSDriver );
		dataSourceProperties.setUsername( secondaryDSUsername);
		dataSourceProperties.setPassword( secondaryDSPassword);
		return dataSourceProperties;
	}
}
