package service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import network.Protocol;
import network.ProtocolLibrary;
import persistence.dao.*;
import persistence.dto.관리자DTO;
import persistence.dto.교수DTO;
import persistence.dto.사용자DTO;
import persistence.dto.학생DTO;
import view.ServerNetworkView;

public class RequestByCommonService {
    /*
        공통 유저 타입의 요청에 대한 세부 기능들을 메소드로 구현한 클래스
     */
    private OutputStream os;
    private InputStream is;
    private Socket socket;
    private byte[] buf;
    private String[] userInfo;
    private ServerNetworkView sv;

    public RequestByCommonService(OutputStream os, InputStream is, Socket socket, byte[] buf, String[] userInfo) {
        this.os = os;
        this.is = is;
        this.socket = socket;
        this.buf = buf;
        this.userInfo = userInfo;
        sv = new ServerNetworkView(socket, userInfo);
    }

    public void run(Protocol protocol) throws IOException, ClassNotFoundException, InterruptedException {

        // 코드를 통해 특정 서비스를 실행하게 됨
        int code = protocol.getCode();

        // 전송받은 메세지에 대한 정보를 서버에 출력
        sv.printReceiveProtocol(protocol);

        if (code == Protocol.코드_공통요청_관리자계정생성) {
            관리자DTO 관리자DTO = new 관리자DTO();
            관리자DAO 관리자DAO = new 관리자DAO();
            사용자DAO 사용자DAO = new 사용자DAO();

            // 계정 생성 응답
            ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            // 계정 정보 전송 받음
            if (!ProtocolLibrary.isValidReceiveProtocol(is, protocol, buf, sv, Protocol.타입_정보전송_계정정보, Protocol.코드_정보전송_계정정보_관리자)) {
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                return;
            }
            // 관리자 계정 정보를 저장
            String[] adminInfo = protocol.getDatas();

            // 계정 생성
            if (사용자DAO.선택사용자출력(adminInfo[0]).get사용자ID() == null) { // 중복 판단
                관리자DTO.set사용자ID(adminInfo[0]);
                관리자DTO.set사용자명(adminInfo[1]);
                관리자DTO.set사용자PW(adminInfo[2]);
                관리자DTO.set주소(adminInfo[3]);
                관리자DTO.set주민번호(adminInfo[4]);
                관리자DTO.set전화번호(adminInfo[5]);
                관리자DTO.set학과명(adminInfo[6]);
                관리자DAO.관리자추가(관리자DTO);
                // 계정 생성 성공 메시지 전송
                ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
                return;
            } else {
                // 계정 생성 실패 메시지 전송
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getRedundantKeyMessage());
                return;
            }

        } else if (code == Protocol.코드_공통요청_개인정보수정) {
            // 개인 정보 수정 응답
            ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            // 확인용 PW 전송 받음
            if (!ProtocolLibrary.isValidReceiveProtocolType(is, protocol, buf, sv, Protocol.타입_정보전송_PW)) {
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                return;
            }
            // PW 저장
            String userPW = protocol.getData();
            // PW 정보가 일치 판단
            if (userInfo[1].equals(userPW))
                ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            else {
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, "Messasge: 비밀번호가 옳지 않습니다.");
                return;
            }
            // 수정할 정보 전송 받음
            if (!ProtocolLibrary.isValidReceiveProtocolType(is, protocol, buf, sv, Protocol.타입_정보전송_계정정보)) {
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                return;
            }
            // 수정 정보 저장
            String[] updateUserInfo = protocol.getDatas();

            // 계정 수정. code에 따라 사용자 유형에 대한 판단을 함
            if (protocol.getCode() == Protocol.코드_정보전송_계정정보_관리자) {
                관리자DAO 관리자DAO = new 관리자DAO();
                관리자DTO 관리자DTO = 관리자DAO.선택관리자출력(userInfo[0]);
                if (!updateUserInfo[0].equals(""))
                    관리자DTO.set사용자명(updateUserInfo[0]);
                if (!updateUserInfo[1].equals(""))
                    관리자DTO.set사용자PW(updateUserInfo[1]);
                if (!updateUserInfo[2].equals(""))
                    관리자DTO.set주소(updateUserInfo[2]);
                if (!updateUserInfo[3].equals(""))
                    관리자DTO.set주민번호(updateUserInfo[3]);
                if (!updateUserInfo[4].equals(""))
                    관리자DTO.set전화번호(updateUserInfo[4]);
                if (!updateUserInfo[5].equals(""))
                    관리자DTO.set학과명(updateUserInfo[5]);

                사용자DAO 사용자DAO = new 사용자DAO();
                사용자DAO.사용자정보수정(관리자DTO);

            } else if (protocol.getCode() == Protocol.코드_정보전송_계정정보_교수) {
                교수DAO 교수DAO = new 교수DAO();
                교수DTO 교수DTO = 교수DAO.선택교수출력(userInfo[0]);
                if (!updateUserInfo[0].equals(""))
                    교수DTO.set사용자명(updateUserInfo[0]);
                if (!updateUserInfo[1].equals(""))
                    교수DTO.set사용자PW(updateUserInfo[1]);
                if (!updateUserInfo[2].equals(""))
                    교수DTO.set주소(updateUserInfo[2]);
                if (!updateUserInfo[3].equals(""))
                    교수DTO.set주민번호(updateUserInfo[3]);
                if (!updateUserInfo[4].equals(""))
                    교수DTO.set전화번호(updateUserInfo[4]);
                if (!updateUserInfo[5].equals(""))
                    교수DTO.set학과명(updateUserInfo[5]);
                if (!updateUserInfo[6].equals(""))
                    교수DTO.set교수실(updateUserInfo[6]);
                if (!updateUserInfo[7].equals(""))
                    교수DTO.set교수실전화번호(updateUserInfo[7]);

                사용자DAO 사용자DAO = new 사용자DAO();
                사용자DAO.사용자정보수정(교수DTO);
                교수DAO.교수실수정(교수DTO);
                교수DAO.교수실전화번호수정(교수DTO);

            } else if (protocol.getCode() == Protocol.코드_정보전송_계정정보_학생) {
                // 학생DTO에 정보를 담음
                학생DAO 학생DAO = new 학생DAO();
                학생DTO 학생DTO = 학생DAO.선택학생출력(userInfo[0]);
                if (!updateUserInfo[0].equals(""))
                    학생DTO.set사용자명(updateUserInfo[0]);
                if (!updateUserInfo[1].equals(""))
                    학생DTO.set사용자PW(updateUserInfo[1]);
                if (!updateUserInfo[2].equals(""))
                    학생DTO.set주소(updateUserInfo[2]);
                if (!updateUserInfo[3].equals(""))
                    학생DTO.set주민번호(updateUserInfo[3]);
                if (!updateUserInfo[4].equals(""))
                    학생DTO.set전화번호(updateUserInfo[4]);
                if (!updateUserInfo[5].equals(""))
                    학생DTO.set학과명(updateUserInfo[5]);
                if (!updateUserInfo[6].equals(""))
                    학생DTO.set학년(Integer.parseInt(updateUserInfo[6]));

                사용자DAO 사용자DAO = new 사용자DAO();
                사용자DAO.사용자정보수정(학생DTO);
                학생DAO.학년수정(학생DTO);
            } else {
                // 계정 수정 실패 메시지 전송
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
            }
            // 계정 수정 성공 메시지 전송
            ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
        } else {
            // 옳지 않은 통신
            ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
        }
    }

    /*
        login 관련 메소드
        return: 로그인 성공 및 실패 판단
     */
    public boolean login(Protocol protocol) throws IOException, ClassNotFoundException, InterruptedException {
        int code = protocol.getCode();
        사용자DAO 사용자DAO = new 사용자DAO();
        사용자DTO 선택사용자DTO = new 사용자DTO();

        if (code == Protocol.코드_공통요청_로그인) {
            sv.printReceiveProtocol(protocol);
            int count = 0;
            boolean correctID = false;
            // id 요청하기
            protocol = new Protocol(Protocol.타입_요청_공통, Protocol.코드_공통요청_로그인_ID);
            os.write(protocol.getPacket());
            sv.printSendMessage(protocol);
            while (count < 3) { // ID 요청은 3번 이루어짐
                count++;

                // ID 전송받기
                if (!ProtocolLibrary.isValidReceiveProtocol(is, protocol, buf, sv, Protocol.타입_정보전송_ID, Protocol.코드_응답_데이터전송)) {
                    ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                    return false;
                }
                // ID 저장
                String userID = protocol.getData();
                // ID 존재 판단
                선택사용자DTO.set사용자ID(userID);
                선택사용자DTO = 사용자DAO.선택사용자출력(userID);

                if (선택사용자DTO.get사용자ID() != null) {
                    // userInfo에 로그인ID 기억
                    userInfo[0] = userID;
                    correctID = true;
                    break;
                } else if (count < 3) {
                    // 존재하지 않는 ID에 대한 실패 메시지를 통해 ID 재전송 유도
                    ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, "Message: 존재하지 않는 ID입니다.");
                }
            }

             if (count >= 3 && !correctID) {    // 재전송 횟수 3회 초과 시
                // 로그인 실패 메시지 전송
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, "Message: ID 입력에 3번 실패했습니다. ");
                return false;
            }

            // PW확인 용 count 초기화
            count = 0;
            boolean correctPW = false;

            // PW 요청하기
            protocol = new Protocol(Protocol.타입_요청_공통, Protocol.코드_공통요청_로그인_PW);
            os.write(protocol.getPacket());
            sv.printSendMessage(protocol);
            while (count < 3) { // PW 요청은 3번 이루어짐
                count++;

                // PW 전송받기
                if (!ProtocolLibrary.isValidReceiveProtocol(is, protocol, buf, sv, Protocol.타입_정보전송_PW, Protocol.코드_응답_데이터전송)) {
                    ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                    return false;
                }
                // PW 저장
                String userPW = protocol.getData();
                // PW 일치 판단
                if (선택사용자DTO.get사용자PW().equals(userPW)) {
                    // userInfo에 PW 기억
                    userInfo[1] = userPW;
                    correctPW = true;
                    break;
                } else if (count < 3) {
                    // 일치하지 않는 PW에 대한 실패 메시지 전송을 통해 PW 재전송 유도
                    ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, "Message: 비밀번호가 옳지 않습니다.");
                }
            }
            if (count >= 3 && !correctPW) {    // 재전송 횟수 3회 초과 시
                // 로그인 실패 메시지 전송
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, "Message: 비밀번호를 3회 틀렸습니다.");
                return false;
            }
            // 로그인 성공 메시지 전송 하기
            ProtocolLibrary.sendSuccessProtocolWithData(os, protocol, buf, sv, String.valueOf(선택사용자DTO.get사용자유형()));
            return true;

        } else {
            // 옳지 않은 통신
            ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
            return false;
        }
    }
}
