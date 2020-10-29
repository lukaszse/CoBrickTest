package pl.com.seremak;

import com.mongodb.client.MongoClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class UserControllerTest {

    @Test
    void findUser() {
    }

    // TODO - to be reviewed
    @Test
    @DisplayName("Password was successfully hidden")
    void hidePassword_passwd_was_hidden_properly() {

        // given
        String password = "somePassword";
        StringBuilder stringBuilder = new StringBuilder();
        for (char ch : password.toCharArray()) {
            stringBuilder.append("*");
        }

        var mockMongoClient = mock(MongoClient.class);
        var userToTest = new User();
        userToTest.setPassword(password);


        System.out.println(stringBuilder);

        // SUT
        var toTest = new UserController(mockMongoClient);
        var hidePassword = toTest.hidePassword(userToTest);

        // when
        var testResult = hidePassword.getPassword();

        // then
        assertThat(testResult)
                .isInstanceOf(String.class)
                .isEqualTo(stringBuilder.toString());
    }
}