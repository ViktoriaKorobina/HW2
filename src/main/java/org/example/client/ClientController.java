package org.example.client;

import org.example.server.ServerController;

import java.io.FileReader;

/**
 * класс содержащий логику работы клиента
 *
 * @clientView абстракция графического интерфейса
 * @server объект для связи с сервером
 */
public class ClientController {
    private boolean connected;
    private String name;
    private ClientView clientView;
    private ServerController server;
    public ClientGUI clientGUI;
    public static final String LOG_PATH = "src/org.example/log.txt";

    public void setClientView(ClientView clientView) {
        this.clientView = clientView;
    }

    public void setServer(ServerController server) {
        this.server = server;
    }

    public boolean connectToServer(String name) {
        this.name = name;
        if (!server.connectUser(this.clientGUI)) {
            showOnWindow("Подключение не удалось");
            return false;
        } else {
            showOnWindow("Вы успешно подключились!\n");
            connected = true;
            String log = server.getHistory();
            if (log != null){
                showOnWindow(log);
            }
            return true;
        }
    }

    /**
     * Метод отключения от сервера инициализированное сервером
     */
    public void disconnectedFromServer() {
        if (connected) {
            connected = false;
            clientView.disconnectedFromServer();
            showOnWindow("Вы были отключены от сервера!");
        }
    }

    /**
     * Метод отключения от сервера инициализированное клиентом (например закрыто GUI)
     */
    public void disconnectFromServer() {
        server.disconnectUser(this.clientGUI);
    }

    /**
     * Метод, с помощью которого сервер передает клиенту сообщения
     */
    public void answerFromServer() {
        StringBuilder stringBuilder = new StringBuilder();
        try (FileReader reader = new FileReader(LOG_PATH)){
            int c;
            while ((c = reader.read()) != -1){
                stringBuilder.append((char) c);
            }
            //stringBuilder.delete(stringBuilder.length()-1, stringBuilder.length());
            showOnWindow(stringBuilder.toString());
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Метод для передачи сообщения на сервер
     * @param text текст передаваемого сообщения
     */
    public void message(String text) {
        if (connected) {
            if (!text.isEmpty()) {
                server.message(name + ": " + text);
            }
        } else {
            showOnWindow("Нет подключения к серверу");
        }
    }

    /**
     * Метод вывода сообщения на GUI
     * @param text текст, который требуется вывести на экран
     */
    private void showOnWindow(String text) {
        clientView.showMessage(text);
    }
}
