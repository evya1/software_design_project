package il.cshaifasweng.OCSFMediatorExample.server.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.MessageObject;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.IOException;

public interface RequestHandler {
    void handle(MessageObject message, ConnectionToClient client) throws IOException;
}