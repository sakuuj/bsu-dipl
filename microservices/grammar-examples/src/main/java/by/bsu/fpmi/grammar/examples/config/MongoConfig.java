package by.bsu.fpmi.grammar.examples.config;

import com.mongodb.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Objects;

@Configuration(proxyBeanMethods = false)
@EnableMongoRepositories(basePackages = "by.bsu.fpmi.grammar.examples.repository")
public class MongoConfig {

    @Bean
    public MongoClientSettings mongoClientSettings() {

        return MongoClientSettings.builder()
                .readConcern(ReadConcern.MAJORITY)
                .writeConcern(WriteConcern.MAJORITY)
                .readPreference(ReadPreference.primaryPreferred())
                .build();
    }

    @Bean
    public MongoClientFactoryBean mongoClient(Environment env, MongoClientSettings mongoClientSettings) {

        String connectionString = Objects.requireNonNull(env.getProperty("spring.datasource.url"));
        String username = Objects.requireNonNull(env.getProperty("spring.datasource.username"));
        String password = Objects.requireNonNull(env.getProperty("spring.datasource.password"));

        var mongoFactory = new MongoClientFactoryBean();
        mongoFactory.setConnectionString(new ConnectionString(connectionString));


        mongoFactory.setCredential(new MongoCredential[]{MongoCredential.createCredential(username, "admin", password.toCharArray())});
        mongoFactory.setMongoClientSettings(mongoClientSettings);

        return mongoFactory;
    }

    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory mongoDbFactory, MongoMappingContext mongoMappingContext) {

        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));

        return converter;
    }
}
