package il.cshaifasweng.OCSFMediatorExample.client;

/*
ALL GUI CLASSES REQUIRE TO USE THIS INTERFACE.

When implementing this class the class that derives from it MUST CONTAIN:
private SimpleClient client variable

All contact to the server is used with this client session.
The moveScene method is activating the FXMLUtil which requires ClientDependent implements.
It injects the Client session instance to provide information to the controller.
 */
public interface ClientDependent {
    void setClient(SimpleClient client);
}