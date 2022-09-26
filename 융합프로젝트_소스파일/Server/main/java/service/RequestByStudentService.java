package service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import network.Protocol;
import network.ProtocolLibrary;
import persistence.MyBatisConnectionFactory;
import persistence.dao.개설교과목DAO;
import persistence.dao.수강신청DAO;
import persistence.dto.개설교과목DTO;
import persistence.dto.수강신청DTO;
import view.ServerNetworkView;

public class RequestByStudentService {
    /*
        학생 타입의 요청에 대한 세부 기능들을 메소드로 구현한 클래스
    */
    private OutputStream os;
    private InputStream is;
    private Socket socket;
    private byte[] buf;
    private String[] userInfo;
    private ServerNetworkView sv;

    public RequestByStudentService(OutputStream os, InputStream is, Socket socket, byte[] buf, String[] userInfo) {
        this.os = os;
        this.is = is;
        this.socket = socket;
        this.buf = buf;
        this.userInfo = userInfo;
        sv = new ServerNetworkView(socket, userInfo);
    }

    public void run(Protocol protocol) throws IOException, ClassNotFoundException, InterruptedException {
        sv.printReceiveProtocol(protocol);
        int code = protocol.getCode();

        if (code == Protocol.코드_학생요청_수강신청및취소) {
            //수강 신청/취소 요청 도착 시 교과목 정보 요청
            ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);

            //교과목번호 정보 받기, 타입과 코드가 옳지 않다면 실패 메시지 전송 후 리턴
            if (!ProtocolLibrary.isValidReceiveProtocolType(is, protocol, buf, sv, Protocol.타입_정보전송_교과목번호)) {
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                return;
            }
            String subjectNumber = protocol.getData();
            String userID = userInfo[0];

            //교과목번호 확인을 위해 개설교과목 DAO 연결
            개설교과목DAO 개설교과목DAO = new 개설교과목DAO(MyBatisConnectionFactory.getSqlSessionFactory());
            개설교과목DTO 개설교과목DTO = 개설교과목DAO.개설교과목선택출력(subjectNumber);

            //수강신청/취소를 위해 수강신청 DAO 연결
            수강신청DAO 수강신청DAO = new 수강신청DAO(MyBatisConnectionFactory.getSqlSessionFactory());
            수강신청DTO 수강신청DTO = new 수강신청DTO();
            수강신청DTO.set사용자ID(userID);
            수강신청DTO.set교과목번호(subjectNumber);

            //교과목이 개설되어있지 않다면 실패메시지 전송 후 리턴
            if (개설교과목DTO == null) {
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getEmptyKeyMessage());
                return;
            }
            //받은 교과목이 실제 존재 한다면 수강 신청/취소 요청
            ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);

            //수강 신청/취소 정보 전송받기, 타입이 다르다면 실패 메시지 전송 후 리턴
            if (!ProtocolLibrary.isValidReceiveProtocolType(is, protocol, buf, sv, Protocol.타입_정보전송_수강신청및취소정보)) {
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                return;
            }
            //수강신청 및 취소에 대한 코드를 저장
            code = protocol.getCode();

            if (code == Protocol.코드_정보전송_수강신청및취소정보_신청) {
                //DAO에 해당 교과목 수강신청 이때, 동시성 제어를 위해 스레드 클래스의 동기화 적용
                synchronized (ClientThread.class) {
                    try {
                        //교과목 수강신청 후 성공 메시지 전송
                        수강신청DAO.수강신청입력(수강신청DTO);
                        ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
                    } catch (Exception e) {
                        //DAO에서 신청 도중 수강인원초과, 기간안됨, 시간표 중복의 경우의 에러 메시지를 클라이언트에게 전송
                        ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, e.getMessage());
                    }
                }

            } else if (code == Protocol.코드_정보전송_수강신청및취소정보_취소) {
                List<개설교과목DTO> myEnrollList = 수강신청DAO.개인수강신청목록출력(수강신청DTO);
                boolean isExistErollment = false;
                //해당 교과목이 수강신청 되어있는지 검사
                for(int i=0; i<myEnrollList.size(); i++) {
                    if(myEnrollList.get(i).get교과목번호().equals(subjectNumber)) {
                        isExistErollment = true;
                        break;
                    }
                }

                if(!isExistErollment)
                    ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, "ERROR: 신청되어 있지 않은 교과목입니다.");

                //DAO에 해당 교과목 수강취소
                수강신청DAO.수강신청삭제(수강신청DTO);
                //수강취소 결과 전송
                ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            }
        } else {
            //수강관련 타입이 아닌경우 조회 관련 내용 검사
            sendProcessedData(os, protocol, code, buf); //조회 관련 내용
        }
    }

    public void sendProcessedData(OutputStream os, Protocol protocol, int code, byte[] buf) throws IOException, ClassNotFoundException, InterruptedException {
        if (code == Protocol.코드_학생요청_특정개설교과목강의계획서조회) {
            //교과목 번호 요청하기
            ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);

            // 교과목 번호 가져오기, 옳지 않은 타입을 전송받은 경우 실패 메시지 전송 후 리턴
            if (!ProtocolLibrary.isValidReceiveProtocolType(is, protocol, buf, sv, Protocol.타입_정보전송_교과목번호)) {
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                return;
            }
            String subjectNumber = protocol.getData();

            //교과목번호 확인을 위해 개설교과목 DAO 연결
            개설교과목DAO 개설교과목DAO = new 개설교과목DAO(MyBatisConnectionFactory.getSqlSessionFactory());
            개설교과목DTO 개설교과목DTO = new 개설교과목DTO();
            개설교과목DTO = 개설교과목DAO.개설교과목선택출력(subjectNumber);

            //해당 교과목이 개설되어있다면
            if (개설교과목DTO != null) {
                //강의계획서 내용 가공해서 보내기
                String processedData = "강의계획 : " + 개설교과목DTO.get강의계획();
                ProtocolLibrary.sendSuccessProtocolWithData(os, protocol, buf, sv, processedData);

            } else {
                //해당 교과목이 개설되어있지 않다면 실패 메시지 전송
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getEmptyKeyMessage());
            }
        } else {
            // 옳지 못한 통신
            ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
        }
    }

}
