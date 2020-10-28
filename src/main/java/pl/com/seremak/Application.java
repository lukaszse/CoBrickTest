package pl.com.seremak;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.internal.MongoClientImpl;
import io.micronaut.runtime.Micronaut;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.inject.Inject;

public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);

        MongoClient client = MongoClients.create();

        DbInit dbInit = new DbInit(client);
        dbInit.createCollection();

    }

}
