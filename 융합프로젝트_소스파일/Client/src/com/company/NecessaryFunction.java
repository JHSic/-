package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.InputMismatchException;
import java.util.Scanner;

public class NecessaryFunction {

    static Scanner sc = new Scanner(System.in);

    public static void Read(InputStream ois, Protocol protocol) throws IOException { // 패킷을 읽는 함수
        byte[] buf = new byte[Protocol.최대길이];
        ois.read(buf);
        protocol.setPacket(buf);
    }

    public static boolean isType(int packetType) { // 타입이 옳은지 확인하는 함수
        if (packetType == Protocol.타입_응답) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isCode(int packetCode) { // 코드가 옳은지 확인하는 함수
        if (packetCode == Protocol.코드_응답_성공) {
            return true;
        } else if (packetCode == Protocol.코드_응답_데이터전송) {
            return true;
        } else if (packetCode == Protocol.코드_응답_실패) {
            return true;
        } else {
            return false;
        }
    }

    public static void updateInfo(Protocol protocol, OutputStream oos, int type, int code, int size) throws IOException { // 교수및학생 정보 수정 함수
        String[] info = new String[size];
        for (int i = 0; i < info.length; i++) {
            if(i == 0) {
                공통메뉴.사용자명수정메뉴();
            } else if(i == 1) {
                공통메뉴.사용자PW수정메뉴();
            }  else if(i == 2) {
                공통메뉴.사용자주소수정메뉴();
            } else if(i == 3) {
                공통메뉴.사용자주민번호수정메뉴();
            } else if(i == 4) {
                공통메뉴.사용자전화번호수정메뉴();
            } else if(i == 5) {
                공통메뉴.사용자학과명수정메뉴();
            }
            if(code == Protocol.코드_정보전송_계정정보_교수) {
                if(i == 6) {
                    교수메뉴.교수실수정메뉴();
                } else if(i == 7) {
                    교수메뉴.교수실전화번호수정메뉴();
                }
            } else {
                if(i == 6) {
                    학생메뉴.학년변경메뉴();
                }
            }
            info[i] = sc.nextLine();
        }
        protocol = new Protocol(type, code);
        protocol.setData(info);
        oos.write(protocol.getPacket());
        oos.flush();
    }

    public static void updateSubjectInfo(Protocol protocol, OutputStream oos, int type, int size) throws IOException { // 교과목 및 개설교과목 정보 수정 함수
        String[] info = new String[size];

        for (int i = 0; i < info.length; i++) {
            if(type == Protocol.타입_정보전송_교과목정보) {
                if (i == 0) {
                    관리자메뉴.교과목이름수정메뉴();
                } else if (i == 1) {
                    관리자메뉴.대상학년수정메뉴();
                } else if (i == 2) {
                    관리자메뉴.교육과정수정메뉴();
                } else if (i == 3) {
                    관리자메뉴.이수구분수정메뉴();
                } else if (i == 4) {
                    관리자메뉴.학점수정메뉴();
                } else if (i == 5) {
                    관리자메뉴.설계과목여부수정메뉴();
                }
            }
            if(type == Protocol.타입_정보전송_개설교과목정보) {
                if(i == 0) {
                    관리자메뉴.최대수강인원수정메뉴();
                } else if(i == 1) {
                    관리자메뉴.강의실수정메뉴();
                } else if(i == 2) {
                    관리자메뉴.분반수정메뉴();
                }
            }
            info[i] = sc.nextLine();
        }

        protocol = new Protocol(type);
        protocol.setData(info);
        oos.write(protocol.getPacket());
        oos.flush();
    }

    public static void multyInput(Protocol protocol, OutputStream oos, int type, int code, int size) throws IOException { // 여러 정보를 전송하는 함수
        String[] info = new String[size];
        for (int i = 0; i < info.length; i++) {
            info[i] = sc.next();
        }
        sc.nextLine();

        protocol = new Protocol(type, code);
        protocol.setData(info);
        oos.write(protocol.getPacket());
        oos.flush();
    }

    public static void multyInput(Protocol protocol, OutputStream oos, int type, int size) throws IOException { //여러 정보를 전송하는 함수
        String[] info = new String[size];
        for (int i = 0; i < info.length; i++) {
            info[i] = sc.next();
        }
        sc.nextLine();

        protocol = new Protocol(type);
        protocol.setData(info);
        oos.write(protocol.getPacket());
        oos.flush();
    }

    public static void singleInput(Protocol protocol, OutputStream oos, int type, int code) throws IOException { // 한개의 정보를 전송하는 함수
        String info = sc.nextLine();
        protocol = new Protocol(type, code);
        protocol.setData(info);
        oos.write(protocol.getPacket());
        oos.flush();
    }

    public static void singleInput(Protocol protocol, OutputStream oos, int type) throws IOException { // 한개의 정보를 전송하는 함수
        String info = sc.nextLine();
        protocol = new Protocol(type);
        protocol.setData(info);
        oos.write(protocol.getPacket());
        oos.flush();
    }

    public static int check(int packetType, int packetCode) { // 응답에 대한 코드를 확인하는 함수
        if (packetType == Protocol.타입_응답) {
            if (packetCode == Protocol.코드_응답_성공) {
                return 1;
            } else if (packetCode == Protocol.코드_응답_실패) {
                return 2;
            } else if (packetCode == Protocol.코드_응답_데이터전송) {
                return 3;
            }
        } else {
            return 0;
        }
        return 0;
    }

    public static void exit(OutputStream oos, Protocol protocol) throws IOException { // 종료하는 함수
        protocol = new Protocol(Protocol.타입_종료);
        oos.write(protocol.getPacket());

    }

    public static int getInput(int input) { // 유저의 입력을 받는 함수
        try{
            input = sc.nextInt();
            sc.nextLine();
        } catch(InputMismatchException e){
            System.out.println("잘못된 입력입니다.");
            sc.nextLine();
            return -1;
        }
        return input;
    }

}
