package pl.com.seremak;

import com.mongodb.client.MongoClient;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class UserControllerTest {

    @Test
    void findUser() {
    }

    @Test
    void hidePassword_passwd_was_hidden_properly() {

        // given
        String password = "somePassword";
        StringBuilder stringBuilder = new StringBuilder();

        var mockMongoClient = mock(MongoClient.class);
        var mockUser = mock(User.class);
        when(mockUser.getPassword()).thenReturn(password);

        for (char ch : password.toCharArray()) {
            stringBuilder.append("*");
        }
        System.out.println(stringBuilder);
        
        // SUT
        var toTest = new UserController(mockMongoClient);
        var hidePassword = toTest.hidePassword(mockUser);

        // when
        var testResult = hidePassword.getPassword();
        System.out.println(testResult);

        // then
        assertThat(testResult)
                .isInstanceOf(String.class)
                .isEqualTo(stringBuilder.toString());
    }
}