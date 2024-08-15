package org.example.server;

import org.example.client.ClientGUI;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class ServerController implements ServerView {
    public List<ClientGUI> clientGUIList;
    private ServerWindow serverWindow;


    private final String LOG_PATH = "src/org.example/log.txt";



    public void setServerView(ServerWindow serverWindow) {
        this.serverWindow = serverWindow;
    }

    public boolean connectUser(ClientGUI clientGUI){
        if (!this.serverWindow.work){
            return false;
        }
        clientGUIList.add(clientGUI);
        return true;
    }

    @Override
    public String getLog() {
        return this.readLog();
    }

    @Override
    public void disconnectUser(ClientGUI clientGUI) {
        clientGUIList.remove(clientGUI);
        if (clientGUI != null) {
            clientGUI.disconnectedFromServer();
        }
    }

    @Override
    public void message(String text) {
        if (!serverWindow.work) {
            return;
        }
        serverWindow.appendLog(text);
        answerAll(text);
        serverWindow.saveInLog(text);
    }


    private void answerAll(String text) {
        for (ClientGUI clientGUI : clientGUIList) {
            clientGUI.answer(text);
        }
    }

    String readLog(){
        StringBuilder stringBuilder = new StringBuilder();
        try (FileReader reader = new FileReader(LOG_PATH)){
            int c;
            while ((c = reader.read()) != -1){
                stringBuilder.append((char) c);
            }
            return stringBuilder.toString();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String getHistory() {
        try (FileReader reader = new FileReader("log.txt")) {
            StringBuilder content = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                content.append((char) c);
            }
            return content.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
