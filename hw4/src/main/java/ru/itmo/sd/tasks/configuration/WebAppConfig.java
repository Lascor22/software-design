package ru.itmo.sd.tasks.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import ru.itmo.sd.tasks.db.DBAccess;

import java.sql.SQLException;

@Configuration
public class WebAppConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new ToStringConverter());
    }

    @Bean
    public DBAccess initDBAccess() throws SQLException {
        return new DBAccess();
    }

}

