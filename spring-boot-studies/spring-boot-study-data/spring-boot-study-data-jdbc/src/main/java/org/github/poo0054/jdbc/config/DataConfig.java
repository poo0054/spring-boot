package org.github.poo0054.jdbc.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.TransactionManager;

/**
 * @author zhangzhi
 */
@Configuration(proxyBeanMethods = false)
@EnableJdbcRepositories("org.github.poo0054.jdbc.dao")
@EnableJdbcAuditing
public class DataConfig extends AbstractJdbcConfiguration {
	@Bean
	NamedParameterJdbcOperations namedParameterJdbcOperations(DataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}

	@Bean
	@ConditionalOnMissingBean(TransactionManager.class)
	DataSourceTransactionManager transactionManager(Environment environment, DataSource dataSource,
			ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
		DataSourceTransactionManager transactionManager = createTransactionManager(environment, dataSource);
		transactionManagerCustomizers.ifAvailable((customizers) -> customizers.customize(transactionManager));
		return transactionManager;
	}

	private DataSourceTransactionManager createTransactionManager(Environment environment, DataSource dataSource) {
		return environment.getProperty("spring.dao.exceptiontranslation.enabled", Boolean.class, Boolean.TRUE)
				? new JdbcTransactionManager(dataSource) : new DataSourceTransactionManager(dataSource);
	}

}
