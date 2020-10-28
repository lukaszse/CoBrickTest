package pl.com.seremak;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.annotation.QueryValue;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Introspected
public class User {

    @NotBlank
    @QueryValue
    private String username;

    @NotBlank
    private String password;

    @PositiveOrZero
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
}