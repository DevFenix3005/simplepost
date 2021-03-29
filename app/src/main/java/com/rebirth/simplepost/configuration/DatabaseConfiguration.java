package com.rebirth.simplepost.configuration;

import com.rebirth.simplepost.domain.tables.repositories.CommentRepository;
import com.rebirth.simplepost.domain.tables.repositories.PostRepository;
import com.rebirth.simplepost.domain.tables.repositories.PostsTagRepository;
import com.rebirth.simplepost.domain.tables.repositories.TagRepository;
import com.rebirth.simplepost.utils.listeners.AuditListener;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.jooq.SQLDialect;
import org.jooq.impl.*;
import org.springframework.boot.autoconfigure.jooq.JooqExceptionTranslator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

@Configuration
@Slf4j
public class DatabaseConfiguration {

    private final AppConfiguration appConfiguration;

    public DatabaseConfiguration(AppConfiguration appConfiguration) {
        this.appConfiguration = appConfiguration;
    }

    @Bean
    public HikariDataSource hikariDataSource() {
        AppConfiguration.Database database = appConfiguration.getDatabase();
        log.info(database.toString());
        String jdbc = database.getJdbc();
        String driverClassName = database.getDriver();
        String username = database.getUsername();
        String password = database.getPassword();

        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driverClassName);
        config.setJdbcUrl(jdbc);
        config.setUsername(username);
        config.setPassword(password);

        return new HikariDataSource(config);
    }

    @Bean
    public DataSourceConnectionProvider connectionProvider(HikariDataSource hikariDataSource) {
        return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(hikariDataSource));
    }

    @Bean
    public org.jooq.Configuration configuration(DataSourceConnectionProvider connectionProvider) {
        return new DefaultConfiguration()
                .set(connectionProvider)
                .set(new DefaultExecuteListenerProvider(new JooqExceptionTranslator()))
                .set(SQLDialect.POSTGRES)
                .set(new DefaultRecordListenerProvider(new AuditListener()));

    }

    @Bean
    public DefaultDSLContext dslContext(org.jooq.Configuration configuration) {
        return new DefaultDSLContext(configuration);
    }

    @Bean
    public TagRepository tagRepository(org.jooq.Configuration configuration) {
        TagRepository tagRepository = new TagRepository();
        tagRepository.setConfiguration(configuration);
        return tagRepository;
    }

    @Bean
    public PostRepository postRepository(org.jooq.Configuration configuration) {
        PostRepository postRepository = new PostRepository();
        postRepository.setConfiguration(configuration);
        return postRepository;
    }

    @Bean
    public PostsTagRepository postsTagRepository(org.jooq.Configuration configuration) {
        PostsTagRepository postsTagRepository = new PostsTagRepository();
        postsTagRepository.setConfiguration(configuration);
        return postsTagRepository;
    }

    @Bean
    public CommentRepository commentRepository(org.jooq.Configuration configuration) {
        CommentRepository commentRepository = new CommentRepository();
        commentRepository.setConfiguration(configuration);
        return commentRepository;
    }


}
