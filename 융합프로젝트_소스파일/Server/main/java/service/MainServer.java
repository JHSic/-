package service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {

    public static void main(String[] args) {

        try {
            // 최초 connect
            ServerSocket sSocket = new ServerSocket(8888);
            sSocket.setReuseAddress(true);

            System.out.println("=====\tServer start!\t=====");
            System.out.println("Waiting connect...\n");

            while (true) {
                Socket socket = sSocket.accept();   // 클라이언트의 접속을 대기

                System.out.println("\t********************\tClient Connected!\t********************");

                // 연결된 Socket을 쓰레드화. 다중 클라이언트 접속 가능
                ClientThread cthread = new ClientThread(socket);
                Thread thread = new Thread(cthread);
                thread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}