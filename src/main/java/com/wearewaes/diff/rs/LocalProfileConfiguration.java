package com.wearewaes.diff.rs;

//import com.mongodb.MongoClient;
//import cz.jirutka.spring.embedmongo.EmbeddedMongoFactoryBean;
//import java.io.IOException;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.data.mongodb.core.MongoTemplate;

public class LocalProfileConfiguration {

  private static final String MONGO_DB_URL = "localhost";

  private static final String MONGO_DB_NAME = "waes-diff-db";

//  @Bean
//  public MongoTemplate mongoTemplate() throws IOException {
//    EmbeddedMongoFactoryBean mongo = new EmbeddedMongoFactoryBean();
//    mongo.setBindIp(MONGO_DB_URL);
//    MongoClient mongoClient = mongo.getObject();
//    MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, MONGO_DB_NAME);
//    return mongoTemplate;
//  }
}