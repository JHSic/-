package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class InitialMenu { // 초기화면
    public static void main(String[] args) throws IOException {
        int input = -1;

        while(true){
            Socket socket = new Socket(args[0], Integer.parseInt(args[1])); // 서버와 소켓 연결
            OutputStream oos = socket.getOutputStream();
            InputStream ois = socket.getInputStream();

            Protocol protocol = new Protocol(); // 프로토콜 객체 생성
            byte[] buf = protocol.getPacket(); // 패킷이 담길 바이트 배열 생성


            초기접속화면.수강신청프로그램초기메뉴();
            input = NecessaryFunction.getInput(input); // 유저의 입력을 받음

            switch(input){
                case 1:
                    InitialMainService.login(ois, oos, protocol, socket); // 로그인
                    NecessaryFunction.exit(oos, protocol); // 로그인 화면에서 잘못된 입력시 소켓 종료
                    break;
                case 2:
                    InitialMainService.createAdmin(ois, oos, protocol); // 관리자 생성
                    NecessaryFunction.exit(oos, protocol); // 관리자 생성 화면에서 잘못된 입력시 소켓 종료
                    break;
                case 0:
                    메세지메뉴.연결종료();
                    NecessaryFunction.exit(oos, protocol); // 소켓 종료
                    return;
                default:
                    메세지메뉴.오류로인한종료();
                    NecessaryFunction.exit(oos, protocol); // 소켓 종료
                    return;
            }
        }
    }
}
