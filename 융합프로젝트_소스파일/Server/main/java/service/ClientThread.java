package service;

import java.net.Socket;

public class ClientThread implements Runnable {

    private Socket socket;

    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        // 메인 서비스를 시작
        MainService ms = new MainService(socket);
        ms.run();
    }
}