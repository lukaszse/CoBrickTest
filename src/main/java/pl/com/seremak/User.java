package pl.com.seremak;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.http.annotation.QueryValue;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Introspected
public class User implements Comparable<User> {

    @NotBlank
    @QueryValue
    private String username;

    @NotBlank
    private String password;

    private Integer age;



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public int compareTo(User u) {
        return this.getUsername().compareTo(u.getUsername());
    }

    @Override
    public int hashCode() {
        return getUsername().hashCode();
    }
}