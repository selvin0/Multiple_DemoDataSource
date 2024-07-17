package Multiple_DataSouce.Multiple_DemoDataSource.DS1Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "firstEntityManagerFactoryBean",basePackages = {"Multiple_DataSouce.Multiple_DemoDataSource.DB1_Repository"},transactionManagerRef="firstTransactionManager")
public class DataSourceConfig1
{
    @Autowired
    private Environment environment;

    @Bean(name="firstDataSource")
    @Primary
    public DataSource dataSource()
    {
        DriverManagerDataSource dataSource=new DriverManagerDataSource();
                    dataSource.setUrl(environment.getProperty("spring.datasource.url"));
                    dataSource.setDriverClassName(environment.getProperty("spring.datasource.driver-class-name"));
                    dataSource.setUsername(environment.getProperty("spring.datasource.username"));
                    dataSource.setPassword(environment.getProperty("spring.datasource.password"));
                    return dataSource;
    }
    @Primary
    @Bean(name="firstEntityManagerFactoryBean")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean()
    {
        LocalContainerEntityManagerFactoryBean bean=new LocalContainerEntityManagerFactoryBean();
                    bean.setDataSource(dataSource());
                    bean.setPackagesToScan("Multiple_DataSouce.Multiple_DemoDataSource.DB1.MODEL");

        JpaVendorAdapter adapter=new HibernateJpaVendorAdapter();
                bean.setJpaVendorAdapter(adapter);

        Map<String,String> props=new HashMap<>();
                           props.put("hibernate.dialect","org.hibernate.dialect.MySQLDialect");
                           props.put("hibernate.show_sql","true");
                           props.put("hibernate.hbm2ddl.auto","update");

                           bean.setJpaPropertyMap(props);
                           return bean;
    }

    @Bean("firstTransactionManager")
    @Primary
    public PlatformTransactionManager transactionManager()
    {
        JpaTransactionManager manager=new JpaTransactionManager();
                 manager.setEntityManagerFactory(entityManagerFactoryBean().getObject());
                 return manager;
    }
}
