package pl.com.seremak;

import com.mongodb.UnixServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller("/users")
public class UserController {

    private final MongoClient client;
    Logger logger = LoggerFactory.getLogger(UserController.class);


    UserController(MongoClient client) {
        this.client = client;
    }

    // this is just classic "Hello World" on beginning work with Micronaut.
    @Get("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String index() {
        return "Hello World";
    }


    @Get
    public Iterable<User> findAll() {

        logger.info("client is null: " + (client == null));
        final FindIterable<User> iterable = getCollection().find();

        return StreamSupport
                .stream(getCollection().find().spliterator(), false)
                .collect(Collectors.toList())
                .stream()
                .map(p -> {
                            String passwd = p.getPassword()
                            .replaceAll(".", "*");
                            p.setPassword(passwd);
                            return p;
                })
                .collect(Collectors.toList());
    }


    private MongoCollection<User> getCollection() {
        return client
                .getDatabase("test")
                .getCollection("users", User.class);
    }

}
