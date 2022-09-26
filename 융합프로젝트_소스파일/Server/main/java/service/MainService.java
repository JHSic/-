package service;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

import network.Protocol;
import network.ProtocolLibrary;
import view.ServerNetworkView;

public class MainService implements Runnable {
    /*
        MainService로 요청에 대한 분기판단
        SubService로 각각의 기능에 대한 구현. 사용하던 stream과 buffer, protocol을 넘겨줌.
        Service 관련 클래스들은 View를 통해 클라이언트와의 통신 과정들을 출력함.
     */
    private Protocol protocol;
    private byte[] buf;
    private boolean isLogined;  // 로그인이 되었는지 판단하는 변수
    private String[] userInfo;  // 로그인시, 로그인된 유저의 정보를 서버가 기억하기 위한 변수
    private Socket socket;
    private OutputStream os;
    private InputStream is;
    private ServerNetworkView sv;   // 클라이언트와의 통신 로그를 출력해주는 클래스

    public MainService(Socket socket) {
        // 프로토콜 및 버퍼 생성
        this.socket = socket;

        protocol = new Protocol();
        buf = new byte[Protocol.최대길이];
        userInfo = new String[2];   // ID and PW
        isLogined = false;
        os = null;
        is = null;
    }

    public void run() {
        try {
            startCommunication();   // 클라이언트의 요청을 받는 것으로 통신 시작
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startCommunication() throws IOException, ClassNotFoundException, InterruptedException {

        try {
            // 최초 연결된 클라이언트의 정보를 출력
            sv = new ServerNetworkView(socket, userInfo);
            sv.printConnectInfo();

            // 로그인이 되기 전까지, 로그인 or 관리자 계정 생성에 대한 서비스만 허용
            while (!isLogined) {
                os = socket.getOutputStream();
                is = socket.getInputStream();

                protocol = new Protocol();
                buf = new byte[Protocol.최대길이];

                // 클라이언트의 요청을 대기
                is.read(buf);
                protocol.setPacket(buf);
                int type = protocol.getType();

                // type 및 code에 따라 각 서브 Service를 선택하게됨
                switch (type) {

                    case Protocol.타입_요청_공통:
                        int code = protocol.getCode();
                        if (code == Protocol.코드_공통요청_로그인) {
                            // 로그인 서비스 실행 후, 로그인 성공 or 실패 여부에 대한 판단을 위해 boolean값을 반환받음
                            RequestByCommonService requestByCommonService = new RequestByCommonService(os, is, socket, buf, userInfo);
                            isLogined = requestByCommonService.login(protocol);

                        } else if (code == Protocol.코드_공통요청_관리자계정생성) {
                            // 관리자계정 생성을 위한 서비스 실행
                            RequestByCommonService requestByCommonService = new RequestByCommonService(os, is, socket, buf, userInfo);
                            requestByCommonService.run(protocol);

                        } else {
                            // 옳지 못한 통신
                            ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                        }
                        break;

                    case Protocol.타입_종료:
                        // 클라이언트의 요청으로 통신 종료. Stream과 socket을 close해주면서 서비스가 종료된다.
                        System.out.println("\t********************\tClient Disconnected!\t********************");
                        System.out.println("Disconnected UserID:\t" + userInfo[0]);
                        is.close();
                        os.close();
                        socket.close();
                        return;

                    default:
                        // 옳지 못한 통신
                        ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                        break;
                }
            }

            // 로그인 성공 시, 클라이언트의 지속적인 요청을 받으며 서비스를 제공. 로그인이 되어 있지 않다면 실행되지 않는 반복문
            while (isLogined) {
                Protocol protocol = new Protocol();
                byte[] buf = new byte[Protocol.최대길이];

                // 클라이언트의 요청을 대기
                is.read(buf);
                protocol.setPacket(buf);
                int type = protocol.getType();

                // type을 통해 유저 유형에 따른 서브 서비스를 구현한 클래스를 선택하게 됨
                switch (type) {

                    case Protocol.타입_요청_공통:
                        RequestByCommonService requestByCommonService = new RequestByCommonService(os, is, socket, buf, userInfo);
                        requestByCommonService.run(protocol);
                        break;

                    case Protocol.타입_요청_관리자:
                        RequestByAdminService requestByAdminService = new RequestByAdminService(os, is, socket, buf, userInfo);
                        requestByAdminService.run(protocol);
                        break;

                    case Protocol.타입_요청_교수:
                        RequestByProfessorService requestByProfessorService = new RequestByProfessorService(os, is, socket, buf, userInfo);
                        requestByProfessorService.run(protocol);
                        break;

                    case Protocol.타입_요청_학생:
                        RequestByStudentService requestByStudentService = new RequestByStudentService(os, is, socket, buf, userInfo);
                        requestByStudentService.run(protocol);
                        break;

                    case Protocol.타입_요청_교수학생공통:
                        RequestByProfessorAndStudentService requestByProfessorAndStudentService = new RequestByProfessorAndStudentService(os, is, socket, buf, userInfo);
                        requestByProfessorAndStudentService.run(protocol);
                        break;

                    case Protocol.타입_종료:
                        // 클라이언트의 요청으로 통신 종료. Stream과 socket을 close해주면서 서비스가 종료된다.
                        System.out.println("\t********************\tClient Disconnected!\t********************");
                        System.out.println("Disconnected UserID:\t" + userInfo[0]);
                        is.close();
                        os.close();
                        socket.close();
                        return;

                    default:
                        // 옳지 못한 통신
                        sv.printReceiveProtocol(protocol);
                        ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                        break;
                }
            }

        } catch (SocketException e) {
            // 클라이언트의 강제 종료 등 예외로 인한 통신 종료. 예외 메세지 출력 후, Stream과 socket을 close해주면서 서비스가 종료된다.
            e.getMessage();
            System.out.println("\t********************\tClient Disconnected!\t********************");
            System.out.println("Disconnected UserID:\t" + userInfo[0]);
            is.close();
            os.close();
            socket.close();
            return;
        }
    }
}