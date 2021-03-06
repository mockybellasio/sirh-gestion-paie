package dev.paie.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Import(DataSourceMySQLConfig.class)
@EnableTransactionManagement
@Configuration
public class JpaConfig {
	
	 @Bean
     public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
       JpaTransactionManager txManager = new JpaTransactionManager();
       txManager.setEntityManagerFactory(emf);
       return txManager;
     }

	@Bean
	// Cette configuration nécessite une source de données configurée.
	// Elle s'utilise donc en association avec un autre fichier de configuration
	// d éfinissant un bean DataSource.
	public EntityManagerFactory entityManagerFactory(DataSource dataSource) {
		
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

		// vendorAdapter.setGenerateDdl(true); // <1>

		// activer les logs SQL
		vendorAdapter.setShowSql(true);
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		// alternative au persistence.xml
		factory.setPackagesToScan("dev.paie.entite");
		factory.setDataSource(dataSource);

		Properties jpaProperties = new Properties(); // <2>
		//jpaProperties.setProperty("javax.persistence.schema-generation.database.action", "drop-and-create"); // <2>
		//jpaProperties.setProperty("javax.persistence.sql-load-script-source", "data.sql"); // <3>

		factory.setJpaProperties(jpaProperties); // <2> <3>

		factory.afterPropertiesSet();
		return factory.getObject();
	}

}
