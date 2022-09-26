package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class InitialMainService {

    public static void choices(int num, Protocol protocol, OutputStream oos) throws IOException { // 로그인과 관리자 계정 생성 요청에 대한 묶음
        switch(num){
            case 1:
                protocol = new Protocol(Protocol.타입_요청_공통, Protocol.코드_공통요청_로그인);
                oos.write(protocol.getPacket());
                break;
            case 2:
                protocol = new Protocol(Protocol.타입_요청_공통, Protocol.코드_공통요청_관리자계정생성);
                oos.write(protocol.getPacket());
                break;
        }
    }

    public static void login(InputStream ois, OutputStream oos, Protocol protocol, Socket socket) throws IOException{ // 로그인 함수
        choices(1, protocol, oos);

        protocol = new Protocol();
        int userType = -1;

        NecessaryFunction.Read(ois, protocol); // 서버로부터 패킷 수신
        int packetType = protocol.getType();
        int packetCode = protocol.getCode();

        if(packetType == Protocol.타입_요청_공통 && packetCode == Protocol.코드_공통요청_로그인_ID) { // 정상적인 타입과 코드인지 확인
            int count = 0;
            while (count < 3) { // ID를 3번까지 확인
                count++;
                초기접속화면.로그인메뉴();
                NecessaryFunction.singleInput(protocol, oos, Protocol.타입_정보전송_ID, Protocol.코드_응답_데이터전송); // ID정보 전송

                NecessaryFunction.Read(ois, protocol); // 서버로부터 패킷 수신
                packetType = protocol.getType();
                packetCode = protocol.getCode();

                if (packetType == Protocol.타입_요청_공통 && packetCode == Protocol.코드_공통요청_로그인_PW) { // 정상적인 타입과 코드인지 확인
                    count--;
                    break;
                } else if(packetType == Protocol.타입_응답 && packetCode == Protocol.코드_응답_실패) { // 응답 실패가 왔을 경우
                    String error = protocol.getData();
                    System.out.println(error);
                } else { // 잘못된 타입과 코드가 왔을 경우
                    String error = protocol.getData();
                    System.out.println(error);
                    return;
                }
            }
            if(count >= 3){ //재전송 횟수 3회 초과 시
                return;
            }
            count = 0;

            if (packetType == Protocol.타입_요청_공통 && packetCode == Protocol.코드_공통요청_로그인_PW) { // 정상적인 타입과 코드인지 확인
                while (count < 3) { // PW를 3번까지 확인
                    count++;
                    초기접속화면.비밀번호메뉴();
                    NecessaryFunction.singleInput(protocol, oos, Protocol.타입_정보전송_PW, Protocol.코드_응답_데이터전송); // PW정보 전송
                    NecessaryFunction.Read(ois, protocol); // 서버로부터 패킷 수신
                    packetType = protocol.getType();
                    packetCode = protocol.getCode();

                    if (packetType == Protocol.타입_응답 && packetCode == Protocol.코드_응답_데이터전송) { // 정상적인 타입과 코드인지 확인
                        count--;
                        break;
                    } else if(packetType == Protocol.타입_응답 && packetCode == Protocol.코드_응답_실패) { // 응답 실패가 왔을 경우
                        String error = protocol.getData();
                        System.out.println(error);
                    } else { // 잘못된 타입과 코드가 왔을 경우
                        String error = protocol.getData();
                        System.out.println(error);
                        return;
                    }
                }
            }
            if(count >= 3){ //재전송 횟수 3회 초과 시
                return;
            }

            if (packetType == Protocol.타입_응답 && packetCode == Protocol.코드_응답_데이터전송) { // 서버로부터 사용자 유형을 전달받음
                userType = Integer.parseInt(protocol.getData());
                메세지메뉴.로그인성공();
            }
        }
        else{
            //아예 이상한 코드가 들어옴
            메세지메뉴.오류로인한종료();
            return;
        }
        while(true){
            if(userType == 1) {
                AdminMenu.AdminMenuRun(ois, oos, protocol); //관리자 메뉴 실행
                break;
            }
            else if(userType == 2){
                ProfessorMenu.ProfessorMenuRun(ois, oos, protocol); //교수 메뉴 실행
                break;
            }
            else if(userType == 3){
                StudentMenu.StudentMenuRun(ois, oos, protocol); //학생 메뉴 실행
                break;
            }
            else{
                break;
            }
        }

    }

    public static void createAdmin(InputStream ois, OutputStream oos, Protocol protocol) throws IOException { // 관리자 계정 생성 함수
        choices(2, protocol, oos);

        protocol = new Protocol();

        NecessaryFunction.Read(ois, protocol); //
        int packetType = protocol.getType();
        int packetCode = protocol.getCode();

        if(!NecessaryFunction.isType(packetType) && !NecessaryFunction.isCode(packetCode)) { // 잘못된 타입과 코드가 왔는지 확인
            return;
        }

        if(NecessaryFunction.check(packetType, packetCode) == 2) { // 응답 실패가 왔을 경우
            String result = protocol.getData();
            System.out.println(result);
            return;
        }

        초기접속화면.관리자회원가입메뉴();
        NecessaryFunction.multyInput(protocol, oos, Protocol.타입_정보전송_계정정보, Protocol.코드_정보전송_계정정보_관리자, DataSize_Const.관리자정보); // 생성할 관리자 정보를 전송

        NecessaryFunction.Read(ois, protocol); // 서버로부터 패킷 수신
        packetType = protocol.getType();
        packetCode = protocol.getCode();

        if(!NecessaryFunction.isType(packetType) || !NecessaryFunction.isCode(packetCode)) { // 잘못된 타입과 코드가 왔는지 확인
            return;
        }

        if(NecessaryFunction.check(packetType, packetCode) == 2) { // 응답 실패가 왔을 경우
            String result = protocol.getData();
            System.out.println(result);
            return;
        }

        메세지메뉴.관리자계정생성성공();

    }
}
