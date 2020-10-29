package pl.com.seremak;

import com.mongodb.BasicDBObject;
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
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller("/users")
public class UserController {

    private final MongoClient client;
    Logger logger = LoggerFactory.getLogger(UserController.class);


    UserController(MongoClient client) {
        this.client = client;
    }

    /**
     * Get method. This is just classic "Hello World" on beginning work with Micronaut.
     */
    @Get("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String index() {
        return "Hello World";
    }


    /**
     *  Get Method returns all users as a json.
     *  Pagination and sorting implemented
     */

    @Get("/{?sort}{?size}{?page}")
    @Produces(MediaType.APPLICATION_JSON)
    public Iterable<User> findAll(Optional<String> sort, Optional<Integer> size, Optional<Integer> page) {
        logger.info("MongoClient is null: " + (client == null));
        List<User> users = StreamSupport
                .stream(getCollection()
                        .find()
                        .sort(setSortType(sort.orElse("asc").toLowerCase()))
                        .limit(size.orElse(20))
                        .skip((size.orElse(1)) * page.orElse(1)) // TODO - to be fixed -
                        .spliterator(), false)
                .map(this::hidePassword)
                .collect(Collectors.toList());
        return users;
    }


    /**
     *  Get Method returns one selected user as a json.
     *  Parameter is taking from path variable
     */

    @Get("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public User findUser(@PathVariable @NotNull String username) {
        User user = getCollection()
                        .find(Filters.eq("username", username))
                        .first();
        return hidePassword(user);
    }


    /**
     *  Post method allow to add and save into MongoDB new user
     *  Acceptable parameter format is json body. Validation implemented.
     */
    @Post
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<User> save(@Body @Valid User user) {
        getCollection().insertOne(user);
        return HttpResponse.created(user);
    }

    @Delete("/{username}")
    public HttpResponse delete(String username) {
        Bson filter = Filters.eq("username", username);
        getCollection().deleteOne(filter);
        return HttpResponse.noContent();
    }


    /**
     *  getCollection method returns MongoCollection object from MongoDb.
     */
    private MongoCollection<User> getCollection() {
        return client
                .getDatabase("test")
                .getCollection("users", User.class);
    }


    /**
     *  This method returns User object with hidden password.
     */
    User hidePassword(final User user) {
        user.setPassword(user.getPassword().replaceAll(".", "*"));
        return user;
    }

    /**
     *  This method returns BasicDBObjects. Is use to select sorting type: ascending or descending
     */
    BasicDBObject setSortType(String sort) {
        if(sort.equals("desc")) {
            return new BasicDBObject("username", -1);
        } else if(sort.equals("asc")) {
            return new BasicDBObject("username", 1);
        } else {
            throw new IllegalArgumentException("Wrong parameter passed to method setSortType()");
        }
    }

}
