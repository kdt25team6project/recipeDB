package com.bestprice.bestprice_back.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/test_db"); // JDBC URL
        config.setUsername("root"); // MySQL 사용자 이름
        config.setPassword("0000"); // MySQL 비밀번호
        config.setDriverClassName("com.mysql.cj.jdbc.Driver"); // 드라이버 클래스

        // HikariCP 추가 설정 (선택 사항)
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(20000);
        config.setMaxLifetime(1800000);

        return new HikariDataSource(config);
    }
}
