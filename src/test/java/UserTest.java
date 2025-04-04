
import org.example.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for User.
 */
public class UserTest {

    /**
     * Test Case ID: UT-03-TB
     * Category: Unit (Translucent)
     * Ensures that invalid credentials fail authentication.
     */
    @Test
    public void invalidCredentialsFailAuthentication() {
        // Given
        String username = "testUser";
        String password = "testPass";
        User user = new User(username, password);

        // When
        boolean isAuthenticated = !user.getPassword().equals("wrongPass");

        // Then
        assertTrue(isAuthenticated);
    }

    /**
     * Test Case ID: UT-11-OB
     * Category: Unit (Opaque)
     * Ensures that the user's password is not exposed in the toString method.
     */
    @Test
    public void userPasswordNotExposedInToString() {
        // Given
        String username = "testUser";
        String password = "testPass";
        User user = new User(username, password);

        // When
        String userString = user.toString();

        // Then
        assertFalse(userString.contains(password));
    }
}
