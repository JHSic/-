package service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import network.Protocol;
import network.ProtocolLibrary;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import persistence.MyBatisConnectionFactory;
import persistence.dao.개설교과목DAO;
import persistence.dao.교과목DAO;
import persistence.dao.담당과목목록DAO;
import persistence.dao.수강신청DAO;
import persistence.dto.*;
import view.ServerNetworkView;

public class RequestByProfessorService {
    /*
        교수 타입의 요청에 대한 세부 기능들을 메소드로 구현한 클래스
    */
    private OutputStream os;
    private InputStream is;
    private Socket socket;
    private byte[] buf;
    private String[] userInfo;
    private ServerNetworkView sv;

    public RequestByProfessorService(OutputStream os, InputStream is, Socket socket, byte[] buf, String[] userInfo) {
        this.os = os;
        this.is = is;
        this.socket = socket;
        this.buf = buf;
        this.userInfo = userInfo;
        sv = new ServerNetworkView(socket, userInfo);
    }

    public void run(Protocol protocol) throws IOException, ClassNotFoundException, InterruptedException {
        int code = protocol.getCode();
        //로그인 시 저장한 교번
        String professorNumber = userInfo[0];
        sv.printReceiveProtocol(protocol);

        //강의계획서 입력 요청을 받으면
        if (code == Protocol.코드_교수요청_강의계획서입력) {
            // 교과목 정보 요청
            ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);

            // 교과목 정보 전송받기, 타입과 코드를 검사 옳지 않은 타입인 경우 리턴
            if (!ProtocolLibrary.isValidReceiveProtocolType(is, protocol, buf, sv, Protocol.타입_정보전송_교과목번호)) {
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                return;
            }
            String subjectNumber = protocol.getData();

            //교과목번호 확인을 위해 개설교과목 DAO 연결
            개설교과목DAO 개설교과목DAO = new 개설교과목DAO(MyBatisConnectionFactory.getSqlSessionFactory());
            개설교과목DTO 개설교과목DTO = new 개설교과목DTO();
            개설교과목DTO = 개설교과목DAO.개설교과목선택출력(subjectNumber);

            if (개설교과목DTO != null) {  // 교과목 존재 여부 검사

                담당과목목록DAO 담당과목목록DAO = new 담당과목목록DAO(MyBatisConnectionFactory.getSqlSessionFactory());
                담당과목목록DTO 담당과목목록DTO = new 담당과목목록DTO();
                담당과목목록DTO.set사용자ID(professorNumber);
                List<개설교과목DTO> myClass = 담당과목목록DAO.개인담당과목목록출력(담당과목목록DTO);

                //입력받은 교과목 번호가 자신의 담당과목목록인지 검사
                boolean isMySubject = false;
                for(int i=0; i<myClass.size(); i++) {
                    if(myClass.get(i).get교과목번호().equals(subjectNumber)) {
                        isMySubject = true;
                        break;
                    }
                }

                //자신의 담당교과목이 아닐 시 실패 메시지 전송 후 리턴
                if(!isMySubject) {
                    ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, "ERROR: 담당 교과목이 아닌 과목의 강의계획서를 입력/수정할 수 없습니다.");
                    return;
                }

                //입력이 완료되면 성공 메시지를 보내 강의계획서 정보 요청
                ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);

                //강의계획서 정보 전송받기, 타입과 코드를 검사 옳지 않은 타입인 경우 리턴
                if (!ProtocolLibrary.isValidReceiveProtocolType(is, protocol, buf, sv, Protocol.타입_정보전송_강의계획서입력정보)) {
                    ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                    return;
                }

                //DAO 강의계획서 입력
                개설교과목DTO.set교과목번호(subjectNumber);
                String lecturePlan = protocol.getData();
                개설교과목DTO.set강의계획(lecturePlan);

                try {
                    개설교과목DAO.개설교과목강의계획서입력(개설교과목DTO);
                    //강의 계획 입력 결과 전송
                    ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
                } catch (Exception e) {
                    //입력중 DAO에서 예외 발생 시 실패 메시지 전송 후 리턴
                    ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, e.getMessage());
                    return;
                }

            } else {
                //교과목이 존재 하지 않을 경우 실패 메시지 전송
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getEmptyKeyMessage());
            }

        } else if (code == Protocol.코드_교수요청_강의계획서수정) {
            // 교과목 정보 요청
            ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);

            // 교과목 정보 전송받기
            if (!ProtocolLibrary.isValidReceiveProtocolType(is, protocol, buf, sv, Protocol.타입_정보전송_교과목번호)) {
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                return;
            }
            String subjectNumber = protocol.getData();

            //교과목번호 확인을 위해 개설교과목 DAO 연결
            개설교과목DAO 개설교과목DAO = new 개설교과목DAO(MyBatisConnectionFactory.getSqlSessionFactory());
            개설교과목DTO 개설교과목DTO = new 개설교과목DTO();
            개설교과목DTO = 개설교과목DAO.개설교과목선택출력(subjectNumber);

            if (개설교과목DTO != null) { //교과목 존재 여부 검사
                ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);

                //강의계획서 수정정보 전송받기
                if (!ProtocolLibrary.isValidReceiveProtocolType(is, protocol, buf, sv, Protocol.타입_정보전송_강의계획서입력정보)) {
                    ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                    return;
                }
                //DAO 에 강의계획서 입력
                개설교과목DTO.set교과목번호(subjectNumber);
                String lecturePlan = protocol.getData();
                개설교과목DTO.set강의계획(lecturePlan);

                try {
                    개설교과목DAO.개설교과목강의계획서입력(개설교과목DTO);
                    //강의 계획 입력 결과 전송
                    ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
                } catch (Exception e) {
                    ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, e.getMessage());
                    return;
                }

                //강의계획서 수정 결과 전송
                ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            } else
                // 교과목이 존재하지 않을 경우 실패 메시지 전송
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getEmptyKeyMessage());

        } else {
            // 입력, 수정 타입이 아닌경우 조회 타입 검사
            sendProcessedData(os, protocol, code, buf); //조회 관련 내용
        }
    }

    public void sendProcessedData(OutputStream os, Protocol protocol, int code, byte[] buf) throws IOException, ClassNotFoundException, InterruptedException {
        String professorNumber = userInfo[0];

        if (code == Protocol.코드_교수요청_수강신청학생목록조회) {
            // 교과목 번호 요청하기
            ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);

            // 교과목 정보 전송받기, 옳지 않은 타입인경우 실패메시지 전송 후 리턴
            if (!ProtocolLibrary.isValidReceiveProtocolType(is, protocol, buf, sv, Protocol.타입_정보전송_교과목번호)) {
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                return;
            }
            String subjectNumber = protocol.getData();

            //교과목번호 확인을 위해 개설교과목 DAO 연결
            개설교과목DAO 개설교과목DAO = new 개설교과목DAO(MyBatisConnectionFactory.getSqlSessionFactory());
            개설교과목DTO 개설교과목DTO = new 개설교과목DTO();
            개설교과목DTO = 개설교과목DAO.개설교과목선택출력(subjectNumber);

            //수강신청목록 확인을 위해 수강신청 DAO 연결
            수강신청DAO 수강신청DAO = new 수강신청DAO(MyBatisConnectionFactory.getSqlSessionFactory());
            수강신청DTO 수강신청DTO = new 수강신청DTO();
            수강신청DTO.set교과목번호(subjectNumber);
            List<학생DTO> 학생DTOS = 수강신청DAO.교과목수강신청학생목록조회(수강신청DTO);

            //교과목번호 확인
            if (개설교과목DTO != null) {
                // 수강신청학생목록  정보 내용 작성 후 전송
                String processedData = "--------------- " + subjectNumber + "를 수강한 학생목록 --------------- \n";

                for (int i = 0; i < 학생DTOS.size(); i++) {
                    processedData +=
                            "학번 : " + 학생DTOS.get(i).get사용자ID() + "\n" +
                                    "이름 : " + 학생DTOS.get(i).get사용자명() + "\n" +
                                    "학과 : " + 학생DTOS.get(i).get학과명() + "\n" +
                                    "학년 : " + 학생DTOS.get(i).get학년() + "\n" +
                                    "--------------------------------------------------------";
                    //마지막 라인은 \n 하지않음
                    if (i < 학생DTOS.size() - 1)
                        processedData += "\n";
                }
                //가공한 데이터 전송
                ProtocolLibrary.sendSuccessProtocolWithData(os, protocol, buf, sv, processedData);

            } else {
                //해당 교과목이 존재하지 않는 경우 실패 메시지 전송
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getEmptyKeyMessage());
            }

        } else if (code == Protocol.코드_교수요청_교과목정보조회_전체) {

            교과목DAO 교과목DAO = new 교과목DAO(MyBatisConnectionFactory.getSqlSessionFactory());
            List<교과목DTO> 교과목DTOS = 교과목DAO.전체교과목출력();

            // DAO 전체 개설교과목 정보 내용 작성 후 전송
            String processedData = "--------------- 전체 교과목 목록 --------------- \n";

            for (int i = 0; i < 교과목DTOS.size(); i++) {

                //true false 표시를 O, X 로 변경
                String 개설여부 = "", 설계과목여부 = "";
                if (교과목DTOS.get(i).is설계과목여부()) {
                    설계과목여부 = "O";
                } else 설계과목여부 = "X";

                if (교과목DTOS.get(i).is개설여부()) {
                    개설여부 = "O";
                } else 개설여부 = "X";

                processedData +=
                        "교과목번호 : " + 교과목DTOS.get(i).get교과목번호() + "\n" +
                                "교과목이름  : " + 교과목DTOS.get(i).get교과목이름() + "\n" +
                                "대상학년 : " + 교과목DTOS.get(i).get대상학년() + "\n" +
                                "교육과정 : " + 교과목DTOS.get(i).get교육과정() + "\n" +
                                "이수구분 : " + 교과목DTOS.get(i).get이수구분() + "\n" +
                                "학점 : " + 교과목DTOS.get(i).get학점() + "\n" +
                                "설계과목여부 : " + 설계과목여부 + "\n" +
                                "개설여부 : " + 개설여부 + "\n" +
                                "-----------------------------------------------";

                //마지막 라인은 \n 하지않음
                if (i < 교과목DTOS.size() - 1)
                    processedData += "\n";
            }
            //가공한 데이터 전송
            ProtocolLibrary.sendSuccessProtocolWithData(os, protocol, buf, sv, processedData);

        } else if (code == Protocol.코드_교수요청_교과목정보조회_1학년) {
            //학년별 교과목 정보 조회를 위해 DAO에 연결
            교과목DAO 교과목DAO = new 교과목DAO(MyBatisConnectionFactory.getSqlSessionFactory());
            교과목DTO 교과목DTO = new 교과목DTO();
            교과목DTO.set대상학년(1);
            List<교과목DTO> 교과목DTOS = 교과목DAO.학년별교과목출력(교과목DTO);

            // DAO 1학년 개설교과목 정보 내용 작성 후 전송
            String processedData = "--------------- 1학년 교과목 목록 --------------- \n";

            for (int i = 0; i < 교과목DTOS.size(); i++) {

                //true false 표시를 O, X 로 변경
                String 개설여부 = "", 설계과목여부 = "";
                if (교과목DTOS.get(i).is설계과목여부()) {
                    설계과목여부 = "O";
                } else 설계과목여부 = "X";

                if (교과목DTOS.get(i).is개설여부()) {
                    개설여부 = "O";
                } else 개설여부 = "X";

                processedData +=
                        "교과목번호 : " + 교과목DTOS.get(i).get교과목번호() + "\n" +
                                "교과목이름  : " + 교과목DTOS.get(i).get교과목이름() + "\n" +
                                "대상학년 : " + 교과목DTOS.get(i).get대상학년() + "\n" +
                                "교육과정 : " + 교과목DTOS.get(i).get교육과정() + "\n" +
                                "이수구분 : " + 교과목DTOS.get(i).get이수구분() + "\n" +
                                "학점 : " + 교과목DTOS.get(i).get학점() + "\n" +
                                "설계과목여부 : " + 설계과목여부 + "\n" +
                                "개설여부 : " + 개설여부 + "\n" +
                                "---------------------------------------------";
                    //마지막 라인은 \n 하지않음
                    if (i < 교과목DTOS.size() - 1)
                        processedData += "\n";
            }

            //가공한 데이터 전송
            ProtocolLibrary.sendSuccessProtocolWithData(os, protocol, buf, sv, processedData);

        } else if (code == Protocol.코드_교수요청_교과목정보조회_2학년) {
            //학년별 교과목 정보 조회를 위해 DAO에 연결
            교과목DAO 교과목DAO = new 교과목DAO(MyBatisConnectionFactory.getSqlSessionFactory());
            교과목DTO 교과목DTO = new 교과목DTO();
            교과목DTO.set대상학년(2);
            List<교과목DTO> 교과목DTOS = 교과목DAO.학년별교과목출력(교과목DTO);

            // DAO 2학년 개설교과목 정보 내용 작성 후 전송
            String processedData = "--------------- 2학년 교과목 목록 --------------- \n";

            for (int i = 0; i < 교과목DTOS.size(); i++) {

                //true false 표시를 O, X 로 변경
                String 개설여부 = "", 설계과목여부 = "";
                if (교과목DTOS.get(i).is설계과목여부()) {
                    설계과목여부 = "O";
                } else 설계과목여부 = "X";

                if (교과목DTOS.get(i).is개설여부()) {
                    개설여부 = "O";
                } else 개설여부 = "X";

                processedData +=
                        "교과목번호 : " + 교과목DTOS.get(i).get교과목번호() + "\n" +
                                "교과목이름  : " + 교과목DTOS.get(i).get교과목이름() + "\n" +
                                "대상학년 : " + 교과목DTOS.get(i).get대상학년() + "\n" +
                                "교육과정 : " + 교과목DTOS.get(i).get교육과정() + "\n" +
                                "이수구분 : " + 교과목DTOS.get(i).get이수구분() + "\n" +
                                "학점 : " + 교과목DTOS.get(i).get학점() + "\n" +
                                "설계과목여부 : " + 설계과목여부 + "\n" +
                                "개설여부 : " + 개설여부 + "\n" +
                                "---------------------------------------------";
                    //마지막 라인은 \n 하지않음
                    if (i < 교과목DTOS.size() - 1)
                        processedData += "\n";
                }
            // 가공한 데이터 전송
            ProtocolLibrary.sendSuccessProtocolWithData(os, protocol, buf, sv, processedData);

        } else if (code == Protocol.코드_교수요청_교과목정보조회_3학년) {
            //학년별 교과목 정보 조회를 위해 DAO에 연결
            교과목DAO 교과목DAO = new 교과목DAO(MyBatisConnectionFactory.getSqlSessionFactory());
            교과목DTO 교과목DTO = new 교과목DTO();
            교과목DTO.set대상학년(3);
            List<교과목DTO> 교과목DTOS = 교과목DAO.학년별교과목출력(교과목DTO);

            // DAO 3학년 개설교과목 정보 내용 작성 후 전송
            String processedData = "--------------- 3학년 교과목 목록 --------------- \n";

            for (int i = 0; i < 교과목DTOS.size(); i++) {

                //true false 표시를 O, X 로 변경
                String 개설여부 = "", 설계과목여부 = "";
                if (교과목DTOS.get(i).is설계과목여부()) {
                    설계과목여부 = "O";
                } else 설계과목여부 = "X";

                if (교과목DTOS.get(i).is개설여부()) {
                    개설여부 = "O";
                } else 개설여부 = "X";

                processedData +=
                        "교과목번호 : " + 교과목DTOS.get(i).get교과목번호() + "\n" +
                                "교과목이름  : " + 교과목DTOS.get(i).get교과목이름() + "\n" +
                                "대상학년 : " + 교과목DTOS.get(i).get대상학년() + "\n" +
                                "교육과정 : " + 교과목DTOS.get(i).get교육과정() + "\n" +
                                "이수구분 : " + 교과목DTOS.get(i).get이수구분() + "\n" +
                                "학점 : " + 교과목DTOS.get(i).get학점() + "\n" +
                                "설계과목여부 : " + 설계과목여부 + "\n" +
                                "개설여부 : " + 개설여부 + "\n" +
                                "---------------------------------------------";
                    //마지막 라인은 \n 하지않음
                    if (i < 교과목DTOS.size() - 1)
                        processedData += "\n";
                }
            //가공한 데이터 전송
            ProtocolLibrary.sendSuccessProtocolWithData(os, protocol, buf, sv, processedData);

        } else if (code == Protocol.코드_교수요청_교과목정보조회_4학년) {
            //학년별 교과목 정보 조회를 위해 DAO에 연결
            교과목DAO 교과목DAO = new 교과목DAO(MyBatisConnectionFactory.getSqlSessionFactory());
            교과목DTO 교과목DTO = new 교과목DTO();
            교과목DTO.set대상학년(4);
            List<교과목DTO> 교과목DTOS = 교과목DAO.학년별교과목출력(교과목DTO);

            // DAO 4학년 개설교과목 정보 내용 작성 후 전송
            String processedData = "--------------- 4학년 교과목 목록 --------------- \n";

            for (int i = 0; i < 교과목DTOS.size(); i++) {

                //true false 표시를 O, X 로 변경
                String 개설여부 = "", 설계과목여부 = "";
                if (교과목DTOS.get(i).is설계과목여부()) {
                    설계과목여부 = "O";
                } else 설계과목여부 = "X";

                if (교과목DTOS.get(i).is개설여부()) {
                    개설여부 = "O";
                } else 개설여부 = "X";

                processedData +=
                            "교과목번호 : " + 교과목DTOS.get(i).get교과목번호() + "\n" +
                                    "교과목이름  : " + 교과목DTOS.get(i).get교과목이름() + "\n" +
                                    "대상학년 : " + 교과목DTOS.get(i).get대상학년() + "\n" +
                                    "교육과정 : " + 교과목DTOS.get(i).get교육과정() + "\n" +
                                    "이수구분 : " + 교과목DTOS.get(i).get이수구분() + "\n" +
                                    "학점 : " + 교과목DTOS.get(i).get학점() + "\n" +
                                    "설계과목여부 : " + 설계과목여부 + "\n" +
                                    "개설여부 : " + 개설여부 + "\n" +
                                    "---------------------------------------------";
                    //마지막 라인은 \n 하지않음
                if (i < 교과목DTOS.size() - 1)
                    processedData += "\n";
                }

            // 가공한 데이터 전송
            ProtocolLibrary.sendSuccessProtocolWithData(os, protocol, buf, sv, processedData);

        } else if (code == Protocol.코드_교수요청_담당개설교과목강의계획서조회) {
            //담당교과목의 강의계획서 조회를 위해 DAO에 연결
            개설교과목DAO 개설교과목DAO = new 개설교과목DAO(MyBatisConnectionFactory.getSqlSessionFactory());
            List<개설교과목DTO> 개설교과목DTOS = 개설교과목DAO.담당개설교과목강의계획서출력(professorNumber);

            // 담당개설교과목 강의계획서  정보 내용 작성 후 전송
            String processedData = "--------------- 담당교과목 강의계획서 --------------- \n";

            for (int i = 0; i < 개설교과목DTOS.size(); i++) {
                processedData +=
                        "교과목번호 : " + 개설교과목DTOS.get(i).get교과목번호() + "\n" +
                                "교과목이름 : " + 개설교과목DTOS.get(i).get교과목이름() + "\n" +
                                "강의계획  : " + 개설교과목DTOS.get(i).get강의계획() + "\n";
                //마지막 라인은 \n 하지않음
                if (i < 개설교과목DTOS.size() - 1)
                    processedData += "\n";
            }
            //가공한 데이터 전송
            ProtocolLibrary.sendSuccessProtocolWithData(os, protocol, buf, sv, processedData);
        } else {
            //타입과 코드가 일치 하지 않는 경우 실패 메시지 전송
            ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
        }
    }
}
