package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ProfessorSubService {

    private static void choices(int num, int select, Protocol protocol, OutputStream oos) throws IOException { // 요청에 대한 묶음
        if (num == 3) {
            if (select == 1) {
                protocol = new Protocol(Protocol.타입_요청_교수, Protocol.코드_교수요청_강의계획서입력);
                oos.write(protocol.getPacket());
            } else if (select == 2) {
                protocol = new Protocol(Protocol.타입_요청_교수, Protocol.코드_교수요청_강의계획서수정);
                oos.write(protocol.getPacket());
            }
        }
        oos.flush();
    }

    private static void choice(int num, Protocol protocol, OutputStream oos) throws IOException { // 요청에 대한 묶음
        if (num == 1) {
            protocol = new Protocol(Protocol.타입_요청_공통, Protocol.코드_공통요청_개인정보수정);
            oos.write(protocol.getPacket());
        } else if (num == 2) {
            protocol = new Protocol(Protocol.타입_요청_교수학생공통, Protocol.코드_교수학생공통요청_개설교과목목록조회);
            oos.write(protocol.getPacket());
        } else if (num == 4) {
            protocol = new Protocol(Protocol.타입_요청_교수, Protocol.코드_교수요청_담당개설교과목강의계획서조회);
            oos.write(protocol.getPacket());
        } else if (num == 5) {
            protocol = new Protocol(Protocol.타입_요청_교수, Protocol.코드_교수요청_수강신청학생목록조회);
            oos.write(protocol.getPacket());
        } else if (num == 6) {
            protocol = new Protocol(Protocol.타입_요청_교수학생공통, Protocol.코드_교수학생공통요청_교과목시간표조회_교수);
            oos.write(protocol.getPacket());
        }
        oos.flush();
    }

    public static void updateProfessor(InputStream ois, OutputStream oos, Protocol protocol) throws IOException { // 교수정보 수정 함수
        choice(1, protocol, oos);

        protocol = new Protocol();

        NecessaryFunction.Read(ois, protocol); // 서버로부터 패킷 수신
        int packetType = protocol.getType();
        int packetCode = protocol.getCode();

        if(!NecessaryFunction.isType(packetType) || !NecessaryFunction.isCode(packetCode)) { // 잘못된 타입과 코드인지 확인
            return;
        }

        if(NecessaryFunction.check(packetType, packetCode) == 2) { // 응답 실패일 경우
            String result = protocol.getData();
            System.out.println(result);
            return;
        }

        NecessaryFunction.singleInput(protocol, oos, Protocol.타입_정보전송_PW); // PW정보 전송

        NecessaryFunction.Read(ois, protocol);  // 서버로부터 패킷 수신
        packetType = protocol.getType();
        packetCode = protocol.getCode();

        if(!NecessaryFunction.isType(packetType) || !NecessaryFunction.isCode(packetCode)) { // 잘못된 타입과 코드인지 확인
            return;
        }

        if(NecessaryFunction.check(packetType, packetCode) == 2) { // 응답 실패일 경우
            String result = protocol.getData();
            System.out.println(result);
            return;
        }

        NecessaryFunction.updateInfo(protocol, oos, Protocol.타입_정보전송_계정정보, Protocol.코드_정보전송_계정정보_교수, DataSize_Const.교수수정정보); // 교수수정정보 전송

        NecessaryFunction.Read(ois, protocol); // 서버로부터 패킷 수신
        packetType = protocol.getType();
        packetCode = protocol.getCode();

        if(!NecessaryFunction.isType(packetType) || !NecessaryFunction.isCode(packetCode)) { // 잘못된 타입과 코드인지 확인
            return;
        }

        if(NecessaryFunction.check(packetType, packetCode) == 2) { // 응답 실패일 경우
            String result = protocol.getData();
            System.out.println(result);
            return;
        }

        메세지메뉴.개인정보수정성공();

    }

    public static void readOpenSubject(InputStream ois, OutputStream oos, Protocol protocol) throws IOException { // 개설교과목 정보 조회 함수
        choice(2, protocol, oos);

        protocol = new Protocol();

        NecessaryFunction.Read(ois, protocol); // 서버로부터 패킷 수신
        int packetType = protocol.getType();
        int packetCode = protocol.getCode();

        if(!NecessaryFunction.isType(packetType) || !NecessaryFunction.isCode(packetCode)) { // 잘못된 타입과 코드인지 확인
            return;
        }

        if(NecessaryFunction.check(packetType, packetCode) == 2) { // 응답 실패일 경우
            String result = protocol.getData();
            System.out.println(result);
            return;
        } else if(NecessaryFunction.check(packetType, packetCode) == 3) { // 조회정보 출력 시
            String result = protocol.getData();
            System.out.println(result);
            return;
        }
    }

    public static void insertSyllabus(InputStream ois, OutputStream oos, Protocol protocol) throws IOException { // 강의계획서 입력 함수
        choices(3,1, protocol, oos);

        protocol = new Protocol();

        NecessaryFunction.Read(ois, protocol); // 서버로부터 패킷 수신
        int packetType = protocol.getType();
        int packetCode = protocol.getCode();

        if(!NecessaryFunction.isType(packetType) || !NecessaryFunction.isCode(packetCode)) { // 잘못된 타입과 코드인지 확인
            return;
        }

        if(NecessaryFunction.check(packetType, packetCode) == 2) { // 응답 실패일 경우
            String result = protocol.getData();
            System.out.println(result);
            return;
        }

        NecessaryFunction.singleInput(protocol, oos, Protocol.타입_정보전송_교과목번호); // 교과목번호 전송

        NecessaryFunction.Read(ois, protocol); // 서버로부터 패킷 수신
        packetType = protocol.getType();
        packetCode = protocol.getCode();

        if(!NecessaryFunction.isType(packetType) || !NecessaryFunction.isCode(packetCode)) { // 잘못된 타입과 코드인지 확인
            return;
        }

        if(NecessaryFunction.check(packetType, packetCode) == 2) { // 응답 실패일 경우
            String result = protocol.getData();
            System.out.println(result);
            return;
        }

        교수메뉴.강의계획서정보입력메뉴();
        NecessaryFunction.singleInput(protocol, oos, Protocol.타입_정보전송_강의계획서입력정보); // 강의계획서 정보 전송

        NecessaryFunction.Read(ois, protocol); // 서버로부터 패킷 수신
        packetType = protocol.getType();
        packetCode = protocol.getCode();

        if(!NecessaryFunction.isType(packetType) || !NecessaryFunction.isCode(packetCode)) { // 잘못된 타입과 코드인지 확인
            return;
        }

        if(NecessaryFunction.check(packetType, packetCode) == 2) { // 응답 실패일 경우
            String result = protocol.getData();
            System.out.println(result);
            return;
        }

        메세지메뉴.강의계획서입력성공();

    }

    public static void updateSyllabus(InputStream ois, OutputStream oos, Protocol protocol) throws IOException { // 강의계획서 수정 함수
        choices(3, 2, protocol, oos);

        protocol = new Protocol();

        NecessaryFunction.Read(ois, protocol); // 서버로부터 패킷 수신
        int packetType = protocol.getType();
        int packetCode = protocol.getCode();

        if(!NecessaryFunction.isType(packetType) || !NecessaryFunction.isCode(packetCode)) { // 잘못된 타입과 코드인지 확인
            return;
        }

        if(NecessaryFunction.check(packetType, packetCode) == 2) { // 응답 실패일 경우
            String result = protocol.getData();
            System.out.println(result);
            return;
        }

        NecessaryFunction.singleInput(protocol, oos, Protocol.타입_정보전송_교과목번호); // 교과목번호 전송

        NecessaryFunction.Read(ois, protocol); // 서버로부터 패킷 수신
        packetType = protocol.getType();
        packetCode = protocol.getCode();

        if(!NecessaryFunction.isType(packetType) || !NecessaryFunction.isCode(packetCode)) { // 잘못된 타입과 코드인지 확인
            return;
        }

        if(NecessaryFunction.check(packetType, packetCode) == 2) { // 응답 실패일 경우
            String result = protocol.getData();
            System.out.println(result);
            return;
        }

        교수메뉴.강의계획서정보입력메뉴();
        NecessaryFunction.singleInput(protocol, oos, Protocol.타입_정보전송_강의계획서입력정보); // 강의계획서 정보 전송

        NecessaryFunction.Read(ois, protocol); // 서버로부터 패킷 수신
        packetType = protocol.getType();
        packetCode = protocol.getCode();

        if(!NecessaryFunction.isType(packetType) || !NecessaryFunction.isCode(packetCode)) { // 잘못된 타입과 코드인지 확인
            return;
        }

        if(NecessaryFunction.check(packetType, packetCode) == 2) { // 응답 실패일 경우
            String result = protocol.getData();
            System.out.println(result);
            return;
        }

        메세지메뉴.강의계획서수정성공();

    }

    public static void readOpenSubjectSyllabus(InputStream ois, OutputStream oos, Protocol protocol) throws IOException { // 강의계획서 조회 함수
        choice(4, protocol, oos);

        protocol = new Protocol();

        NecessaryFunction.Read(ois, protocol); // 서버로부터 패킷 수신
        int packetType = protocol.getType();
        int packetCode = protocol.getCode();

        if(!NecessaryFunction.isType(packetType) || !NecessaryFunction.isCode(packetCode)) { // 잘못된 타입과 코드인지 확인
            return;
        }

        if(NecessaryFunction.check(packetType, packetCode) == 2) { // 응답 실패일 경우
            String result = protocol.getData();
            System.out.println(result);
            return;
        } else if(NecessaryFunction.check(packetType, packetCode) == 3) { // 조회정보 출력 시
            String result = protocol.getData();
            System.out.println(result);
            return;
        }
    }

    public static void readStudentListOnSubject(InputStream ois, OutputStream oos, Protocol protocol) throws IOException { // 학생목록 조회 함수
        choice(5, protocol, oos);

        protocol = new Protocol();

        NecessaryFunction.Read(ois, protocol); // 서버로부터 패킷 수신
        int packetType = protocol.getType();
        int packetCode = protocol.getCode();

        if(!NecessaryFunction.isType(packetType) || !NecessaryFunction.isCode(packetCode)) { // 잘못된 타입과 코드인지 확인
            return;
        }

        if(NecessaryFunction.check(packetType, packetCode) == 2) { // 응답 실패일 경우
            String result = protocol.getData();
            System.out.println(result);
            return;
        }

        NecessaryFunction.singleInput(protocol, oos, Protocol.타입_정보전송_교과목번호); // 교과목번호 전송

        NecessaryFunction.Read(ois, protocol); // 서버로부터 패킷 수신
        packetType = protocol.getType();
        packetCode = protocol.getCode();

        if(!NecessaryFunction.isType(packetType) || !NecessaryFunction.isCode(packetCode)) { // 잘못된 타입과 코드인지 확인
            return;
        }

        if(NecessaryFunction.check(packetType, packetCode) == 2) { // 응답 실패일 경우
            String result = protocol.getData();
            System.out.println(result);
            return;
        } else if(NecessaryFunction.check(packetType, packetCode) == 3) { // 조회정보 출력 시
            String result = protocol.getData();
            System.out.println(result);
            return;
        }
    }

    public static void readSubjectTimeTable(InputStream ois, OutputStream oos, Protocol protocol) throws IOException { // 시간표 조회 함수
        choice(6, protocol, oos);

        protocol = new Protocol();

        NecessaryFunction.Read(ois, protocol); // 서버로부터 패킷 수신
        int packetType = protocol.getType();
        int packetCode = protocol.getCode();

        if(!NecessaryFunction.isType(packetType) || !NecessaryFunction.isCode(packetCode)) { // 잘못된 타입과 코드인지 확인
            return;
        }

        if(NecessaryFunction.check(packetType, packetCode) == 2) { // 응답 실패일 경우
            String result = protocol.getData();
            System.out.println(result);
            return;
        } else if(NecessaryFunction.check(packetType, packetCode) == 3) { // 조회정보 출력 시
            String result = protocol.getData();
            System.out.println(result);
            return;
        }
    }
}