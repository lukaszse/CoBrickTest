package pl.com.seremak;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
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
     *  Get method, returns all user as as a json.
     *  This is version without pagination.
     */
    @Get("/xxx/{?sort}")
    @Produces(MediaType.APPLICATION_JSON)
    public Iterable<User> findUsers(Optional<String> sort) {
            final FindIterable<User> iterable = getCollection().find();
            List<User> users = StreamSupport
                    .stream(getCollection().find().spliterator(), false)
                    .map(this::hidePassword)
                    .sorted((u1, u2) -> {
                        if(sort.orElse("asc").equals("desc")){
                            logger.info("Sorting descending");
                            return u2.getUsername().compareTo(u1.getUsername());
                        } else {
                            logger.info("Sorting ascending");
                            return u1.getUsername().compareTo(u2.getUsername());
                        }
                    })
                    .collect(Collectors.toList());
            return users;
    }


    /**
     *  Get method, returns all user as as a json.
     *  With pagination and sorting.
     */
    @Get("/paginated")
    @Produces(MediaType.APPLICATION_JSON)
    public Page<User> paginatedUsers (Pageable pageable) {

        final FindIterable<User> iterable = getCollection().find();

        List<User> users = StreamSupport
                .stream(getCollection().find().spliterator(), false)
                .map(this::hidePassword)
                .collect(Collectors.toList());
        logger.info("pageable {}", pageable);
        return Page.of(users, pageable, 3);
    }


    /**
     *  Get Method returns one selected user as a json.
     *  Parameter is taking from path variable
     */
    @Get("users/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public User user(@PathVariable @NotNull String username) {

        // getting user from collection by username
        User user = getCollection()
                        .find(Filters.eq("username", username))
                        .first();

        // TODO - to simplify, eliminate duplicated code
        // Password hiding. Replacing all character in password with *.
        return hidePassword(user);
    }


    @Post
    public HttpResponse<User> save(@Body @Valid User user) {
        getCollection().insertOne(user);
        return HttpResponse.created(user);
    }

    @Delete("/{name}")

    public HttpResponse delete(String name) {
        Bson filter = Filters.eq("name", name);
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
     *  This method return User object with hidden password.
     */
    private User hidePassword(User user) {
        user.setPassword(user.getPassword().replaceAll(".", "*"));
        return user;
    }

}
