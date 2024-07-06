package il.cshaifasweng.OCSFMediatorExample.server.coreLogic;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.IOException;

public interface RequestHandler {
    void handle(Message message, ConnectionToClient client) throws IOException;
}