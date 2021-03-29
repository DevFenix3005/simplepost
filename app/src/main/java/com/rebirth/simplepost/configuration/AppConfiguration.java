package com.rebirth.simplepost.configuration;

import com.google.common.base.MoreObjects;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("app")
public class AppConfiguration {

    private String name;
    private Database database;


    @Data
    public static class Database {
        private String jdbc;
        private String username;
        private String password;
        private String driver;

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("jdbc", jdbc)
                    .add("username", username)
                    .add("password", password)
                    .add("driver", driver)
                    .toString();
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("database", database)
                .toString();
    }
}
