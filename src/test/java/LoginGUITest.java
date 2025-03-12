import org.example.view.LoginGUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class LoginGUITest {

    private LoginGUI loginGUI;

    @BeforeEach
    void setUp() {
        loginGUI = new LoginGUI();
    }

    @Test
    void testInitialAdminStatus() {
        assertFalse(loginGUI.isAdmin(), "Admin status should be false initially.");
    }

    @Test
    void testSetAndGetCurrentUsername() {
        LoginGUI.setCurrentUsername("testUser");
        assertEquals("testUser", LoginGUI.getCurrentUsername(), "Username should be set and retrieved correctly.");
    }
}
