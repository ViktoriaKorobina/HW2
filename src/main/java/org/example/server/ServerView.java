package org.example.server;

import org.example.client.ClientGUI;


public interface ServerView {

    boolean connectUser(ClientGUI clientGUI);


    String getLog();

    void disconnectUser(ClientGUI clientGUI);

    void message(String text);


}
