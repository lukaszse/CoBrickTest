package pl.com.seremak;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Configuration;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Factory;
import io.micronaut.http.annotation.Controller;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;


@Controller
public class DbInit {
    Logger logger = LoggerFactory.getLogger(DbInit.class);

    private final MongoClient client;

    DbInit(MongoClient client) {
        this.client = client;
    }

    // This is initialization method, which creates collections and put some example documents
    void createCollection() {
        logger.info("client is null: " + (client == null));
        MongoDatabase database = client.getDatabase("test");
        if(database.getCollection("users").countDocuments() == 0) {
            logger.info("creating new colection");
            database.createCollection("users");
            insertData(); // TODO - this is just for tests
        }
    }

    private void insertData() {
        insertUser("Zenek", "abc", 23);
        insertUser("Kasia", "123435", 24);
        insertUser("Franek", "qwerty", 33);
    }


    void insertUser(String username, String password, Integer age) {
        MongoDatabase database = client.getDatabase("test");
        Document document = new Document();
        document.append("username", username);
        document.append("password", password);
        document.append("age", age);
        database.getCollection("users").insertOne(document);
        logger.info("User added");
    }

}
