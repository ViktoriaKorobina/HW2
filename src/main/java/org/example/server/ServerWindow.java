package org.example.server;

import org.example.client.ClientGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;


public class ServerWindow extends JFrame {
    public static final int WIDTH = 400;
    public static final int HEIGHT = 300;
    public static final String LOG_PATH = "src/org.example/log.txt";

    List<ClientGUI> clientGUIList;

    JButton btnStart, btnStop;
    JTextArea log;
    boolean work;

    public ServerWindow(){
        clientGUIList = new ArrayList<>();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Chat server");
        setLocationRelativeTo(null);

        createPanel();

        setVisible(true);
    }



    public void message(String text){
        if (!work){
            return;
        }
        appendLog(text);
        answerAll(text);
        saveInLog(text);
    }


    private void answerAll(String text){
        for (ClientGUI clientGUI: clientGUIList){
            clientGUI.answer(text);
        }
    }

    void saveInLog(String text){
        try (FileWriter writer = new FileWriter(LOG_PATH, true)){
            writer.write(text);
            writer.write("\n");
        } catch (Exception e){
            e.printStackTrace();
        }
    }



    void appendLog(String text){
        log.append(text + "\n");
    }

    private void createPanel() {
        log = new JTextArea();
        add(log);
        add(createButtons(), BorderLayout.SOUTH);
    }

    private Component createButtons() {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        btnStart = new JButton("Start");
        btnStop = new JButton("Stop");

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (work){
                    appendLog("Сервер уже был запущен");
                } else {
                    work = true;
                    appendLog("Сервер запущен!");
                }
            }
        });

        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!work){
                    appendLog("Сервер уже был остановлен");
                } else {
                    work = false;
                    while (!clientGUIList.isEmpty()){
                        for(ClientGUI el: clientGUIList){
                            el.clientController.disconnectFromServer();
                        }

                    }
                    appendLog("Сервер остановлен!");
                }
            }
        });

        panel.add(btnStart);
        panel.add(btnStop);
        return panel;
    }

    public ServerController setServerController(ServerController serverController) {
        return serverController;
    }

    public void showHistory(List<ClientGUI> clientGUIList){
        for (ClientGUI el: clientGUIList){
            el.clientController.answerFromServer();
        }
    }
}
