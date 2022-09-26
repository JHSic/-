package service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import network.Protocol;
import network.ProtocolLibrary;
import persistence.MyBatisConnectionFactory;
import persistence.dao.*;
import persistence.dto.*;
import view.ServerNetworkView;

public class RequestByAdminService {
    /*
        관리자의 요청에 대한 세부 기능들을 메소드로 구현해놓은 클래스    
     */
    private OutputStream os;
    private InputStream is;
    private Socket socket;
    private byte[] buf;
    private String[] userInfo;
    private ServerNetworkView sv;

    public RequestByAdminService(OutputStream os, InputStream is, Socket socket, byte[] buf, String[] userInfo) {
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

        if (code == Protocol.코드_관리자요청_계정생성_교수) {
            // 계정 생성 응답
            ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            // 계정 정보 전송 받음
            if (!ProtocolLibrary.isValidReceiveProtocol(is, protocol, buf, sv, Protocol.타입_정보전송_계정정보, Protocol.코드_정보전송_계정정보_교수)) {
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                return;
            }
            // 교수 계정 정보를 저장
            String[] professorInfo = protocol.getDatas();

            // 계정 생성
            교수DAO 교수DAO = new 교수DAO();
            // 중복 판단
            교수DTO 교수DTO = 교수DAO.선택교수출력(professorInfo[0]);
            if (교수DTO.get사용자ID() == null) {
                교수DTO = new 교수DTO();
                if (!professorInfo[0].equals(""))
                    교수DTO.set사용자ID(professorInfo[0]);
                if (!professorInfo[1].equals(""))
                    교수DTO.set사용자명(professorInfo[1]);
                if (!professorInfo[2].equals(""))
                    교수DTO.set사용자PW(professorInfo[2]);
                if (!professorInfo[3].equals(""))
                    교수DTO.set주소(professorInfo[3]);
                if (!professorInfo[4].equals(""))
                    교수DTO.set주민번호(professorInfo[4]);
                if (!professorInfo[5].equals(""))
                    교수DTO.set전화번호(professorInfo[5]);
                if (!professorInfo[6].equals(""))
                    교수DTO.set학과명(professorInfo[6]);
                if (!professorInfo[7].equals(""))
                    교수DTO.set교수실(professorInfo[7]);
                if (!professorInfo[8].equals(""))
                    교수DTO.set교수실전화번호(professorInfo[8]);
                교수DAO.교수추가(교수DTO);
                // 계정 생성 성공 메시지 전송
                ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            } else {
                // 계정 생성 실패 메시지 전송
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getRedundantKeyMessage());
            }

        } else if (code == Protocol.코드_관리자요청_계정생성_학생) {
            // 계정 생성 응답
            ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            // 계정 정보 전송 받음
            if (!ProtocolLibrary.isValidReceiveProtocol(is, protocol, buf, sv, Protocol.타입_정보전송_계정정보, Protocol.코드_정보전송_계정정보_학생)) {
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
            }
            // 학생 계정 정보 저장
            String[] studentInfo = protocol.getDatas();

            // 계정 생성
            학생DAO 학생DAO = new 학생DAO();
            // 중복 판단
            학생DTO 학생DTO = 학생DAO.선택학생출력(studentInfo[0]);
            if (학생DTO.get사용자ID() == null) {
                학생DTO = new 학생DTO();
                if (!studentInfo[0].equals(""))
                    학생DTO.set사용자ID(studentInfo[0]);
                if (!studentInfo[1].equals(""))
                    학생DTO.set사용자명(studentInfo[1]);
                if (!studentInfo[2].equals(""))
                    학생DTO.set사용자PW(studentInfo[2]);
                if (!studentInfo[3].equals(""))
                    학생DTO.set주소(studentInfo[3]);
                if (!studentInfo[4].equals(""))
                    학생DTO.set주민번호(studentInfo[4]);
                if (!studentInfo[5].equals(""))
                    학생DTO.set전화번호(studentInfo[5]);
                if (!studentInfo[6].equals(""))
                    학생DTO.set학과명(studentInfo[6]);
                if (!studentInfo[7].equals(""))
                    학생DTO.set학년(Integer.parseInt(studentInfo[7]));
                학생DAO.학생추가(학생DTO);
                // 학생 계정 생성 성공 메시지 전송
                ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            } else {
                // 계정 생성 실패 메시지 전송
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getRedundantKeyMessage());
            }

        } else if (code == Protocol.코드_관리자요청_계정삭제) {
            // 계정 삭제 응답
            ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            // 사용자 ID 정보 전송받음
            if (!ProtocolLibrary.isValidReceiveProtocolType(is, protocol, buf, sv, Protocol.타입_정보전송_ID)) {
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                return;
            }
            // 사용자ID 저장
            String userID = protocol.getData();

            // 계정 삭제
            사용자DAO 사용자DAO = new 사용자DAO();
            // 존재 판단
            사용자DTO 사용자DTO = 사용자DAO.선택사용자출력(userID);
            if (사용자DTO.get사용자ID() != null) {
                // DAO를 통해 계정 삭제
                사용자DAO.사용자삭제(사용자DTO);
                // 계정 삭제 성공 메시지 전송
                ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            } else {
                // 계정 삭제 실패 메시지 전송
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getEmptyKeyMessage());
            }

        } else if (code == Protocol.코드_관리자요청_교과목생성) {
            // 교과목 생성 응답
            ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            // 교과목 정보 전송 받음
            if (!ProtocolLibrary.isValidReceiveProtocolType(is, protocol, buf, sv, Protocol.타입_정보전송_교과목정보)) {
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                return;
            }
            // 교과목 생성 정보 저장
            String[] subjectInfo = protocol.getDatas();

            // 교과목 생성
            교과목DAO 교과목DAO = new 교과목DAO(MyBatisConnectionFactory.getSqlSessionFactory());
            교과목DTO 교과목DTO = new 교과목DTO();
            // 중복 판단
            if (교과목DAO.선택교과목출력(subjectInfo[0]) == null) {
                교과목DTO.set교과목번호(subjectInfo[0]);
                교과목DTO.set교과목이름(subjectInfo[1]);
                교과목DTO.set대상학년(Integer.parseInt(subjectInfo[2]));
                교과목DTO.set교육과정(subjectInfo[3]);
                교과목DTO.set이수구분(subjectInfo[4]);
                교과목DTO.set학점(Integer.parseInt(subjectInfo[5]));
                교과목DTO.set설계과목여부(Boolean.parseBoolean(subjectInfo[6]));
                교과목DAO.교과목입력(교과목DTO);
                // 교과목 생성 성공 메시지 전송
                ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            } else {
                // 교과목 생성 실패 메시지 전송
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getRedundantKeyMessage());
            }

        } else if (code == Protocol.코드_관리자요청_교과목수정) {
            // 교과목 수정 응답
            ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            // 교과목 ID 정보 전송 받음
            if (!ProtocolLibrary.isValidReceiveProtocolType(is, protocol, buf, sv, Protocol.타입_정보전송_교과목번호)) {
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                return;
            }
            // 교과목 ID 저장 후 수정 정보 요청
            String subjectCode = protocol.getData();
            ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            // 교과목 정보 전송받음
            if (!ProtocolLibrary.isValidReceiveProtocolType(is, protocol, buf, sv, Protocol.타입_정보전송_교과목정보)) {
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                return;
            }
            // 교과목 정보 저장
            String[] subjectInfo = protocol.getDatas();

            // 교과목 수정
            교과목DAO 교과목DAO = new 교과목DAO(MyBatisConnectionFactory.getSqlSessionFactory());
            // 존재 판단
            교과목DTO 교과목DTO = 교과목DAO.선택교과목출력(subjectCode);
            if (교과목DTO != null) {
                교과목DTO.set교과목번호(subjectCode);
                if (!subjectInfo[0].equals(""))
                    교과목DTO.set교과목이름(subjectInfo[0]);
                if (!subjectInfo[1].equals(""))
                    교과목DTO.set대상학년(Integer.parseInt(subjectInfo[1]));
                if (!subjectInfo[2].equals(""))
                    교과목DTO.set교육과정(subjectInfo[2]);
                if (!subjectInfo[3].equals(""))
                    교과목DTO.set이수구분(subjectInfo[3]);
                if (!subjectInfo[4].equals(""))
                    교과목DTO.set학점(Integer.parseInt(subjectInfo[4]));
                if (!subjectInfo[5].equals(""))
                    교과목DTO.set설계과목여부(Boolean.parseBoolean(subjectInfo[5]));
                교과목DAO.교과목전체정보수정(교과목DTO);
                // 교과목 수정 성공 메시지 전송
                ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            } else {
                // 교과목 수정 실패 메시지 전송
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getEmptyKeyMessage());
            }

        } else if (code == Protocol.코드_관리자요청_교과목삭제) {
            // 교과목 삭제 응답
            ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            // 교과목 ID 정보 전송 받음
            if (!ProtocolLibrary.isValidReceiveProtocolType(is, protocol, buf, sv, Protocol.타입_정보전송_교과목번호)) {
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                return;
            }
            // 교과목 ID 정보 저장
            String subjectCode = protocol.getData();

            // 교과목 삭제
            교과목DAO 교과목DAO = new 교과목DAO(MyBatisConnectionFactory.getSqlSessionFactory());
            // 존재 판단
            교과목DTO 교과목DTO = 교과목DAO.선택교과목출력(subjectCode);
            if (교과목DTO != null) {
                교과목DAO.교과목삭제(교과목DTO);
                // 교과목 삭제 성공 메시지 전송
                ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            } else {
                // 교과목 삭제 실패 메시지 전송
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getEmptyKeyMessage());
            }

        } else if (code == Protocol.코드_관리자요청_개설교과목생성) {
            // 개설교과목 생성 응답
            ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            // 개설교과목 정보 전송 받음
            if (!ProtocolLibrary.isValidReceiveProtocolType(is, protocol, buf, sv, Protocol.타입_정보전송_개설교과목정보)) {
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                return;
            }
            // 개설교과목 정보 저장
            String[] openedSubjectInfo = protocol.getDatas();
            개설교과목DAO 개설교과목DAO = new 개설교과목DAO(MyBatisConnectionFactory.getSqlSessionFactory());
            개설교과목DTO 개설교과목DTO = new 개설교과목DTO();
            교과목DAO 교과목DAO = new 교과목DAO(MyBatisConnectionFactory.getSqlSessionFactory());

            // 교과목 존재 판단
            if(교과목DAO.선택교과목출력(openedSubjectInfo[0]) == null) {
                // 개설교과목 생성 실패 메시지 전송
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getEmptyKeyMessage());
                return;
            }

            // 개설교과목 생성
            if (개설교과목DAO.개설교과목선택출력(openedSubjectInfo[0]) == null) { // 중복 판단
                if (!openedSubjectInfo[0].equals(""))
                    개설교과목DTO.set교과목번호(openedSubjectInfo[0]);
                if (!openedSubjectInfo[1].equals(""))
                    개설교과목DTO.set최대수강인원(Integer.parseInt(openedSubjectInfo[1]));
                if (!openedSubjectInfo[2].equals(""))
                    개설교과목DTO.set강의실(openedSubjectInfo[2]);
                if (!openedSubjectInfo[3].equals(""))
                    개설교과목DTO.set분반(Integer.parseInt(openedSubjectInfo[3]));
                if (!openedSubjectInfo[4].equals(""))
                    개설교과목DTO.set수업요일(openedSubjectInfo[4]);
                if (!openedSubjectInfo[5].equals(""))
                    개설교과목DTO.set수업시작시간(LocalTime.parse(openedSubjectInfo[5], DateTimeFormatter.ofPattern("HHmmss")));
                if (!openedSubjectInfo[6].equals(""))
                    개설교과목DTO.set수업종료시간(LocalTime.parse(openedSubjectInfo[6], DateTimeFormatter.ofPattern("HHmmss")));

                개설교과목DAO.개설교과목정보입력(개설교과목DTO);

                // 개설된 교과목은 담당하는 교수가 존재하므로, 담당 과목 목록을 함께 생성해준다.
                담당과목목록DAO 담당과목목록DAO = new 담당과목목록DAO(MyBatisConnectionFactory.getSqlSessionFactory());
                담당과목목록DTO 담당과목목록DTO = new 담당과목목록DTO();
                담당과목목록DTO.set교과목번호(openedSubjectInfo[0]);
                담당과목목록DTO.set사용자ID(openedSubjectInfo[7]);
                try {
                    담당과목목록DAO.담당과목입력(담당과목목록DTO);
                } catch(Exception e) {
                    // 존재하지 않는 교수 등 예외 발생 시, 생성된 개설 교과목도 함께 삭제시키고 실패 메시지 전송
                    개설교과목DAO.개설교과목삭제(개설교과목DTO);
                    ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, e.getMessage());
                    return;
                }
                // 개설교과목 생성 성공 메시지 전송
                ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            } else {
                // 개설교과목 생성 실패 메시지 전송
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getRedundantKeyMessage());
            }

        } else if (code == Protocol.코드_관리자요청_개설교과목수정) {
            // 개설교과목 수정 응답
            ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            // 개설교과목 ID 정보 전송 받음
            if (!ProtocolLibrary.isValidReceiveProtocolType(is, protocol, buf, sv, Protocol.타입_정보전송_교과목번호)) {
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                return;
            }
            // 개설교과목 ID 정보 저장
            String openedSubjectCode = protocol.getData();
            // 개설교과목 정보 요청 메시지 전송
            ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            // 개설교과목 정보 전송 받음
            if (!ProtocolLibrary.isValidReceiveProtocolType(is, protocol, buf, sv, Protocol.타입_정보전송_개설교과목정보)) {
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                return;
            }
            // 개설교과목 정보 저장
            String[] openedSubjectInfo = protocol.getDatas();

            개설교과목DAO 개설교과목DAO = new 개설교과목DAO(MyBatisConnectionFactory.getSqlSessionFactory());
            개설교과목DTO 개설교과목DTO = 개설교과목DAO.개설교과목선택출력(openedSubjectCode);

            // 개설교과목 수정
            if (개설교과목DTO != null) { // 존재 판단
                개설교과목DTO.set교과목번호(openedSubjectCode);
                if (!openedSubjectInfo[0].equals(""))
                    개설교과목DTO.set최대수강인원(Integer.parseInt(openedSubjectInfo[0]));
                if (!openedSubjectInfo[1].equals(""))
                    개설교과목DTO.set강의실(openedSubjectInfo[1]);
                if (!openedSubjectInfo[2].equals(""))
                    개설교과목DTO.set분반(Integer.parseInt(openedSubjectInfo[2]));
                개설교과목DAO.개설교과목전체정보수정(개설교과목DTO);
                // 개설교과목 수정 성공 메시지 전송
                ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            } else {
                // 개설교과목 수정 실패 메시지 전송
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getEmptyKeyMessage());
            }

        } else if (code == Protocol.코드_관리자요청_개설교과목삭제) {
            // 개설교과목 삭제 응답
            ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            // 개설교과목 ID 정보 전송 받음
            if (!ProtocolLibrary.isValidReceiveProtocolType(is, protocol, buf, sv, Protocol.타입_정보전송_교과목번호)) {
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                return;
            }
            // 개설교과목 ID 정보 저장
            String openedSubjectCode = protocol.getData();

            개설교과목DAO 개설교과목DAO = new 개설교과목DAO(MyBatisConnectionFactory.getSqlSessionFactory());
            개설교과목DTO 개설교과목DTO = 개설교과목DAO.개설교과목선택출력(openedSubjectCode);

            // 개설교과목 삭제
            if (개설교과목DTO != null) { // 존재 판단
                // 개설교과목 삭제
                개설교과목DAO.개설교과목삭제(개설교과목DTO);
                // 개설교과목 삭제 결과 전송
                ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            } else {
                // 개설교과목 삭제 실패 결과 전송
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getEmptyKeyMessage());
            }

        } else if (code == Protocol.코드_관리자요청_강의계획서시작기간설정) {
            // 강의계획서입력기간 설정 응답
            ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            // 교과목 번호 확인
            if (!ProtocolLibrary.isValidReceiveProtocolType(is, protocol, buf, sv, Protocol.타입_정보전송_교과목번호)) {
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                return;
            }
            // 교과목 번호 저장
            String openedSubjectCode = protocol.getData();
            // 강의계획서입력기간 정보 요청
            ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);

            // 강의계획서입력기간 정보 수신
            if (!ProtocolLibrary.isValidReceiveProtocol(is, protocol, buf, sv, Protocol.타입_정보전송_강의계획서기간정보, Protocol.코드_정보전송_강의계획서기간정보_시작기간)) {
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                return;
            }

            // 강의계획서입력기간(시작시간) 설정
            개설교과목DAO 개설교과목DAO = new 개설교과목DAO(MyBatisConnectionFactory.getSqlSessionFactory());
            개설교과목DTO 개설교과목DTO = 개설교과목DAO.개설교과목선택출력(openedSubjectCode);
            if (개설교과목DTO != null) { // 존재 판단
                String insertDateInfo_lecturePlan = protocol.getData();
                강의계획서입력기간DAO 강의계획서입력기간DAO = new 강의계획서입력기간DAO(MyBatisConnectionFactory.getSqlSessionFactory());
                강의계획서입력기간DTO 강의계획서입력기간DTO = new 강의계획서입력기간DTO();
                강의계획서입력기간DTO.set교과목번호(openedSubjectCode);
                강의계획서입력기간DTO.set강의계획서입력시작시간(LocalDateTime.parse(insertDateInfo_lecturePlan, DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
                강의계획서입력기간DAO.강의계획서입력시작시간수정(강의계획서입력기간DTO);
                // 강의계획서입력기간 설정 성공 메시지 전송
                ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
                return;
            } else {
                // 강의계획서입력기간 설정 실패 결과 전송
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getEmptyKeyMessage());
            }

        } else if (code == Protocol.코드_관리자요청_강의계획서종료기간설정) {
            // 강의계획서입력기간 설정 응답
            ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            // 교과목 번호 확인
            if (!ProtocolLibrary.isValidReceiveProtocolType(is, protocol, buf, sv, Protocol.타입_정보전송_교과목번호)) {
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                return;
            }
            // 교과목 번호 저장
            String openedSubjectCode = protocol.getData();

            // 강의계획서입력기간 정보 요청
            ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            // 강의계획서입력기간 정보 수신
            if (!ProtocolLibrary.isValidReceiveProtocol(is, protocol, buf, sv, Protocol.타입_정보전송_강의계획서기간정보, Protocol.코드_정보전송_강의계획서기간정보_종료기간)) {
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                return;
            }

            // 강의계획서입력기간(종료시간) 설정
            개설교과목DAO 개설교과목DAO = new 개설교과목DAO(MyBatisConnectionFactory.getSqlSessionFactory());
            개설교과목DTO 개설교과목DTO = 개설교과목DAO.개설교과목선택출력(openedSubjectCode);
            if (개설교과목DTO != null) { // 존재 판단
                String insertDateInfo_lecturePlan = protocol.getData();
                강의계획서입력기간DAO 강의계획서입력기간DAO = new 강의계획서입력기간DAO(MyBatisConnectionFactory.getSqlSessionFactory());
                강의계획서입력기간DTO 강의계획서입력기간DTO = new 강의계획서입력기간DTO();
                강의계획서입력기간DTO.set교과목번호(openedSubjectCode);
                강의계획서입력기간DTO.set강의계획서입력종료시간(LocalDateTime.parse(insertDateInfo_lecturePlan, DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
                강의계획서입력기간DAO.강의계획서입력종료시간수정(강의계획서입력기간DTO);
                // 강의계획서입력기간 설정 성공 메시지 전송
                ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
                return;
            } else {
                // 강의계획서입력기간 설정 실패 메시지 전송
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getEmptyKeyMessage());
            }

        } else if (code == Protocol.코드_관리자요청_강의계획서시작및종료기간설정) {
            // 강의계획서입력기간 설정 응답
            ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            // 교과목 번호 확인
            if (!ProtocolLibrary.isValidReceiveProtocolType(is, protocol, buf, sv, Protocol.타입_정보전송_교과목번호)) {
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                return;
            }
            // 강의계획서입력기간 정보 요청
            ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            // 교과목 번호 저장
            String openedSubjectCode = protocol.getData();

            // 강의계획서입력기간 정보 수신
            if (!ProtocolLibrary.isValidReceiveProtocol(is, protocol, buf, sv, Protocol.타입_정보전송_강의계획서기간정보, Protocol.코드_정보전송__강의계획서기간정보_시작및종료기간)) {
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                return;
            }

            // 강의계획서입력기간 설정
            개설교과목DAO 개설교과목DAO = new 개설교과목DAO(MyBatisConnectionFactory.getSqlSessionFactory());
            개설교과목DTO 개설교과목DTO = 개설교과목DAO.개설교과목선택출력(openedSubjectCode);
            if (개설교과목DTO != null) { // 존재 판단
                String[] insertDateInfo_lecturePlan = protocol.getDatas();
                강의계획서입력기간DAO 강의계획서입력기간DAO = new 강의계획서입력기간DAO(MyBatisConnectionFactory.getSqlSessionFactory());
                강의계획서입력기간DTO 강의계획서입력기간DTO = new 강의계획서입력기간DTO();
                강의계획서입력기간DTO.set교과목번호(openedSubjectCode);
                강의계획서입력기간DTO.set강의계획서입력시작시간(
                        LocalDateTime.parse(insertDateInfo_lecturePlan[0], DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
                강의계획서입력기간DTO.set강의계획서입력종료시간(
                        LocalDateTime.parse(insertDateInfo_lecturePlan[1], DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
                강의계획서입력기간DAO.강의계획서입력기간수정(강의계획서입력기간DTO);
                // 강의계획서입력기간 설정 성공 메시지 전송
                ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
                return;
            } else {
                // 강의계획서입력기간 설정 실패 메시지 전송
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getEmptyKeyMessage());
            }

        } else if (code == Protocol.코드_관리자요청_학년별수강신청시작기간설정) {
            // 학년별수강신청기간 설정 응답
            ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            // 학년별수강신청기간 정보 전송 받음
            if (!ProtocolLibrary.isValidReceiveProtocol(is, protocol, buf, sv, Protocol.타입_정보전송_수강신청기간정보, Protocol.코드_정보전송_수강신청기간정보_시작기간)) {
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                return;
            }
            // 학년별 수강신청기간 정보 저장. [0] = 대상학년, [1] = 수강신청시작기간
            String[] insertDateInfo_enrollClasses = protocol.getDatas();

            // 학년별수강신청기간 설정
            수강신청기간DAO 수강신청기간DAO = new 수강신청기간DAO(MyBatisConnectionFactory.getSqlSessionFactory());
            수강신청기간DTO 수강신청기간DTO = new 수강신청기간DTO();

            int targetYear = Integer.parseInt(insertDateInfo_enrollClasses[0]);
            if (targetYear < 0 || 5 < targetYear) { // 대상학년 범위 검사
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, "ERROR: 대상학년이 옳지 않습니다.");
                return;
            }
            수강신청기간DTO.set수강신청시작시간(
                    LocalDateTime.parse(insertDateInfo_enrollClasses[1], DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            수강신청기간DAO.학년별수강신청시작기간수정(수강신청기간DTO, targetYear);
            // 학년별수강신청기간 설정 성공 메시지 전송
            ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            return;

        } else if (code == Protocol.코드_관리자요청_학년별수강신청종료기간설정) {
            // 학년별수강신청기간 설정 응답
            ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            // 학년별수강신청기간 정보 전송 받음
            if (!ProtocolLibrary.isValidReceiveProtocol(is, protocol, buf, sv, Protocol.타입_정보전송_수강신청기간정보, Protocol.코드_정보전송_수강신청기간정보_종료기간)) {
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                return;
            }
            // 학년별 수강신청기간 정보 저장. [0] = 대상학년, [1] = 수강신청종료기간
            String[] insertDateInfo_enrollClasses = protocol.getDatas();
            
            int targetYear = Integer.parseInt(insertDateInfo_enrollClasses[0]);
            if (targetYear < 0 || 5 < targetYear) { // 대상학년 범위 검사
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, "ERROR: 대상학년이 옳지 않습니다.");
                return;
            }
            // 학년별수강신청기간 설정
            수강신청기간DAO 수강신청기간DAO = new 수강신청기간DAO(MyBatisConnectionFactory.getSqlSessionFactory());
            수강신청기간DTO 수강신청기간DTO = new 수강신청기간DTO();

            수강신청기간DTO.set수강신청종료시간(
                    LocalDateTime.parse(insertDateInfo_enrollClasses[1], DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            수강신청기간DAO.학년별수강신청종료기간수정(수강신청기간DTO, targetYear);
            // 학년별수강신청기간 설정 성공 메시지 전송
            ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            return;

        } else if (code == Protocol.코드_관리자요청_학년별수강신청시작및종료기간설정) {
            // 학년별수강신청기간 설정 응답
            ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            // 학년별수강신청기간 정보 전송 받음
            if (!ProtocolLibrary.isValidReceiveProtocol(is, protocol, buf, sv, Protocol.타입_정보전송_수강신청기간정보, Protocol.코드_정보전송_수강신청기간정보_시작및종료기간)) {
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
                return;
            }
            // 학년별 수강신청기간 정보 저장. [0] = 대상학년, [1] = 수강신청시작기간, [2] = 수강신청종료기간
            String[] insertDateInfo_enrollClasses = protocol.getDatas();
            
            int targetYear = Integer.parseInt(insertDateInfo_enrollClasses[0]);
            if (targetYear < 0 || 5 < targetYear) { // 대상학년 범위 검사
                ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, "ERROR: 대상학년이 옳지 않습니다.");
                return;
            }
            // 학년별수강신청기간 설정
            수강신청기간DAO 수강신청기간DAO = new 수강신청기간DAO(MyBatisConnectionFactory.getSqlSessionFactory());
            수강신청기간DTO 수강신청기간DTO = new 수강신청기간DTO();
            수강신청기간DTO.set수강신청시작시간(
                    LocalDateTime.parse(insertDateInfo_enrollClasses[1], DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            수강신청기간DTO.set수강신청종료시간(
                    LocalDateTime.parse(insertDateInfo_enrollClasses[2], DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            수강신청기간DAO.학년별수강신청기간수정(수강신청기간DTO, targetYear);
            // 학년별수강신청기간 설정 성공 메시지 전송
            ProtocolLibrary.sendSuccessProtocol(os, protocol, buf, sv);
            return;

        } else if (code == Protocol.코드_관리자요청_계정정보조회_관리자) {
            // 관리자 DTO 생성
            관리자DAO 관리자DAO = new 관리자DAO();
            List<관리자DTO> 관리자DTOS = 관리자DAO.관리자출력();

            // 출력 메세지 작성
            String processedData = "--------------- 전체 관리자 계정 목록 --------------- \n";

            for (int i = 0; i < 관리자DTOS.size(); i++) {
                processedData +=
                        "사용자ID : " + 관리자DTOS.get(i).get사용자ID() + "\n" +
                                "이름 : " + 관리자DTOS.get(i).get사용자명() + "\n" +
                                "PW : " + 관리자DTOS.get(i).get사용자PW() + "\n" +
                                "주소 : " + 관리자DTOS.get(i).get주소() + "\n" +
                                "주민번호 : " + 관리자DTOS.get(i).get주민번호() + "\n" +
                                "전화번호 : " + 관리자DTOS.get(i).get전화번호() + "\n" +
                                "학과 : " + 관리자DTOS.get(i).get학과명() + "\n" +
                                "---------------------------------------------";

                //마지막 라인은 \n 하지않음
                if (i < 관리자DTOS.size() - 1)
                    processedData += "\n";
            }
            // 데이터와 함께 성공 메시지 전송
            ProtocolLibrary.sendSuccessProtocolWithData(os, protocol, buf, sv, processedData);
            return;
        } else if (code == Protocol.코드_관리자요청_계정정보조회_교수) {
            // 교수 DTO 생성
            교수DAO 교수DAO = new 교수DAO();
            List<교수DTO> 교수DTOS = 교수DAO.전체교수출력();

            // 출력 메세지 작성
            String processedData = "--------------- 전체 교수 계정 목록 --------------- \n";

            for (int i = 0; i < 교수DTOS.size(); i++) {
                processedData +=
                        "사용자ID : " + 교수DTOS.get(i).get사용자ID() + "\n" +
                                "이름 : " + 교수DTOS.get(i).get사용자명() + "\n" +
                                "PW : " + 교수DTOS.get(i).get사용자PW() + "\n" +
                                "주소 : " + 교수DTOS.get(i).get주소() + "\n" +
                                "주민번호 : " + 교수DTOS.get(i).get주민번호() + "\n" +
                                "전화번호 : " + 교수DTOS.get(i).get전화번호() + "\n" +
                                "학과 : " + 교수DTOS.get(i).get학과명() + "\n" +
                                "교수실 : " + 교수DTOS.get(i).get교수실() + "\n" +
                                "교수실전화번호 : " + 교수DTOS.get(i).get교수실전화번호() + "\n" +
                                "---------------------------------------------";
                //마지막 라인은 \n 하지않음
                if (i < 교수DTOS.size() - 1)
                    processedData += "\n";
            }
            // 데이터와 함께 성공 메시지 전송
            ProtocolLibrary.sendSuccessProtocolWithData(os, protocol, buf, sv, processedData);
        } else if (code == Protocol.코드_관리자요청_계정정보조회_학생) {
            // 학생 DTO 생성
            학생DAO 학생DAO = new 학생DAO();
            List<학생DTO> 학생DTOS = 학생DAO.전체학생출력();

            // 출력 메세지 작성
            String processedData = "--------------- 전체 학생 계정 목록 --------------- \n";

            for (int i = 0; i < 학생DTOS.size(); i++) {
                processedData +=
                        "사용자ID : " + 학생DTOS.get(i).get사용자ID() + "\n" +
                                "이름 : " + 학생DTOS.get(i).get사용자명() + "\n" +
                                "PW : " + 학생DTOS.get(i).get사용자PW() + "\n" +
                                "주소 : " + 학생DTOS.get(i).get주소() + "\n" +
                                "주민번호 : " + 학생DTOS.get(i).get주민번호() + "\n" +
                                "전화번호 : " + 학생DTOS.get(i).get전화번호() + "\n" +
                                "학과 : " + 학생DTOS.get(i).get학과명() + "\n" +
                                "학년 : " + 학생DTOS.get(i).get학년() + "\n" +
                                "신청과목수 : " + 학생DTOS.get(i).get신청과목수() + "\n" +
                                "신청총학점 : " + 학생DTOS.get(i).get신청총학점() + "\n" +
                                "---------------------------------------------";
                //마지막 라인은 \n 하지않음
                if (i < 학생DTOS.size() - 1)
                    processedData += "\n";
            }
            // 데이터와 함께 성공 메시지 전송
            ProtocolLibrary.sendSuccessProtocolWithData(os, protocol, buf, sv, processedData);
        } else if (code == Protocol.코드_관리자요청_개설교과목정보조회_전체) {
            // 개설교과목 DTO 생성
            개설교과목DAO 개설교과목DAO = new 개설교과목DAO(MyBatisConnectionFactory.getSqlSessionFactory());
            List<개설교과목DTO> 개설교과목DTOS = 개설교과목DAO.개설교과목전체출력();

            // 개설교과목목록 정보 내용 작성 후 전송
            String processedData = "------------------------ 전체 개설교과목 목록 ------------------------ \n";

            for (int i = 0; i < 개설교과목DTOS.size(); i++) {
                String 개설여부 = "", 설계과목여부 = "";
                if (개설교과목DTOS.get(i).is설계과목여부()) {
                    설계과목여부 = "O";
                } else 설계과목여부 = "X";
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                교수DAO 교수DAO = new 교수DAO();  // 해당 개설교과목의 담당 교수를 함께 출력하기 위해 교수DAO 사용
                processedData +=
                        "교과목번호 : " + 개설교과목DTOS.get(i).get교과목번호() + "\n" +
                                "교과목이름 : " + 개설교과목DTOS.get(i).get교과목이름() + "\n" +
                                "담당교수 : " + 교수DAO.담당교수출력(개설교과목DTOS.get(i).get교과목번호()).get사용자명() + "\n" +
                                "대상학년 : " + 개설교과목DTOS.get(i).get대상학년() + "\n" +
                                "교육과정  : " + 개설교과목DTOS.get(i).get교육과정() + "\n" +
                                "이수구분  : " + 개설교과목DTOS.get(i).get이수구분() + "\n" +
                                "학점  : " + 개설교과목DTOS.get(i).get학점() + "\n" +
                                "설계과목여부  : " + 설계과목여부 + "\n" +
                                "최대수강인원  : " + 개설교과목DTOS.get(i).get최대수강인원() + "\n" +
                                "수강신청인원  : " + 개설교과목DTOS.get(i).get수강신청인원() + "\n" +
                                "강의실 : " + 개설교과목DTOS.get(i).get강의실() + "\n" +
                                "분반 : " + 개설교과목DTOS.get(i).get분반() + "\n" +
                                "수업요일  : " + 개설교과목DTOS.get(i).get수업요일() + "\n" +
                                "수업시간  : " + 개설교과목DTOS.get(i).get수업시작시간() + " ~ " + 개설교과목DTOS.get(i).get수업종료시간() + "\n";

                // 수강신청기간 및 강의계획서입력기간도 함께 출력
                if (개설교과목DTOS.get(i).get수강신청시작시간() != null && 개설교과목DTOS.get(i).get수강신청종료시간() != null) {
                    processedData += "수강신청기간  : " + 개설교과목DTOS.get(i).get수강신청시작시간().format(dateTimeFormatter) + " ~ " + 개설교과목DTOS.get(i).get수강신청종료시간().format(dateTimeFormatter) + "\n";
                } else processedData += "수강신청기간  : " + "설정안됨" + "\n";
                if (개설교과목DTOS.get(i).get강의계획서입력시작시간() != null && 개설교과목DTOS.get(i).get강의계획서입력종료시간() != null) {
                    processedData += "강의계획서입력기간  : " + 개설교과목DTOS.get(i).get강의계획서입력시작시간().format(dateTimeFormatter) + " ~ " + 개설교과목DTOS.get(i).get강의계획서입력종료시간().format(dateTimeFormatter) + "\n" +
                            "----------------------------------------------------------------";
                } else processedData += "강의계획서입력기간  : " + "설정안됨" + "\n" +
                        "----------------------------------------------------------------";
                //클라이언트의 메뉴구현 범용성을 위해 마지막 라인은 \n 하지않음
                if (i < 개설교과목DTOS.size() - 1)
                    processedData += "\n";
            }

            // 데이터와 함께 성공 메시지 전송
            ProtocolLibrary.sendSuccessProtocolWithData(os, protocol, buf, sv, processedData);
        } else if (code == Protocol.코드_관리자요청_개설교과목정보조회_교수별) {
            // 교수 이름을 받음
            String professorName = protocol.getData();
            // 개설교과목목록  정보 내용 작성 후 전송
            개설교과목DAO 개설교과목DAO = new 개설교과목DAO(MyBatisConnectionFactory.getSqlSessionFactory());
            List<개설교과목DTO> 개설교과목DTOS = 개설교과목DAO.교수별개설교과목출력(professorName);

            // 개설교과목목록  정보 내용 작성 후 전송
            String processedData = "------------------------ 교수별 개설교과목 목록 ------------------------ \n";

            for (int i = 0; i < 개설교과목DTOS.size(); i++) {
                String 개설여부 = "", 설계과목여부 = "";
                if (개설교과목DTOS.get(i).is설계과목여부()) {
                    설계과목여부 = "O";
                } else 설계과목여부 = "X";
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                교수DAO 교수DAO = new 교수DAO();  // 해당 개설교과목의 담당 교수를 함께 출력하기 위해 교수DAO 사용
                processedData +=
                        "교과목번호 : " + 개설교과목DTOS.get(i).get교과목번호() + "\n" +
                                "교과목이름 : " + 개설교과목DTOS.get(i).get교과목이름() + "\n" +
                                "담당교수 : " + 교수DAO.담당교수출력(개설교과목DTOS.get(i).get교과목번호()).get사용자명() + "\n" +
                                "대상학년 : " + 개설교과목DTOS.get(i).get대상학년() + "\n" +
                                "교육과정  : " + 개설교과목DTOS.get(i).get교육과정() + "\n" +
                                "이수구분  : " + 개설교과목DTOS.get(i).get이수구분() + "\n" +
                                "학점  : " + 개설교과목DTOS.get(i).get학점() + "\n" +
                                "설계과목여부  : " + 설계과목여부 + "\n" +
                                "최대수강인원  : " + 개설교과목DTOS.get(i).get최대수강인원() + "\n" +
                                "수강신청인원  : " + 개설교과목DTOS.get(i).get수강신청인원() + "\n" +
                                "강의실 : " + 개설교과목DTOS.get(i).get강의실() + "\n" +
                                "분반 : " + 개설교과목DTOS.get(i).get분반() + "\n" +
                                "수업요일  : " + 개설교과목DTOS.get(i).get수업요일() + "\n" +
                                "수업시간  : " + 개설교과목DTOS.get(i).get수업시작시간() + " ~ " + 개설교과목DTOS.get(i).get수업종료시간() + "\n";

                // 수강신청기간 및 강의계획서입력기간도 함께 출력
                if (개설교과목DTOS.get(i).get수강신청시작시간() != null && 개설교과목DTOS.get(i).get수강신청종료시간() != null) {
                    processedData += "수강신청기간  : " + 개설교과목DTOS.get(i).get수강신청시작시간().format(dateTimeFormatter) + " ~ " + 개설교과목DTOS.get(i).get수강신청종료시간().format(dateTimeFormatter) + "\n";
                } else processedData += "수강신청기간  : " + "설정안됨" + "\n";

                if (개설교과목DTOS.get(i).get강의계획서입력시작시간() != null && 개설교과목DTOS.get(i).get강의계획서입력종료시간() != null) {
                    processedData += "강의계획서입력기간  : " + 개설교과목DTOS.get(i).get강의계획서입력시작시간().format(dateTimeFormatter) + " ~ " + 개설교과목DTOS.get(i).get강의계획서입력종료시간().format(dateTimeFormatter) + "\n" +
                            "----------------------------------------------------------------";
                } else processedData += "강의계획서입력기간  : " + "설정안됨" + "\n" +
                        "----------------------------------------------------------------";
                //클라이언트의 메뉴구현 범용성을 위해 마지막 라인은 \n 하지않음
                if (i < 개설교과목DTOS.size() - 1)
                    processedData += "\n";
            }

            // 데이터와 함께 성공 메시지 전송
            ProtocolLibrary.sendSuccessProtocolWithData(os, protocol, buf, sv, processedData);
        } else if (code == Protocol.코드_관리자요청_개설교과목정보조회_학년별) {
            // 대상 학년을 받음
            int targetGrade = Integer.parseInt(protocol.getData());
            // 개설교과목목록  정보 내용 작성 후 전송
            개설교과목DAO 개설교과목DAO = new 개설교과목DAO(MyBatisConnectionFactory.getSqlSessionFactory());
            List<개설교과목DTO> 개설교과목DTOS = 개설교과목DAO.학년별개설교과목출력(targetGrade);

            // 개설교과목목록  정보 내용 작성 후 전송
            String processedData = "------------------------ 학년별 개설교과목 목록 ------------------------ \n";

            for (int i = 0; i < 개설교과목DTOS.size(); i++) {
                String 개설여부 = "", 설계과목여부 = "";
                if (개설교과목DTOS.get(i).is설계과목여부()) {
                    설계과목여부 = "O";
                } else 설계과목여부 = "X";
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                교수DAO 교수DAO = new 교수DAO();  // 해당 개설교과목의 담당 교수를 함께 출력하기 위해 교수DAO 사용
                processedData +=
                        "교과목번호 : " + 개설교과목DTOS.get(i).get교과목번호() + "\n" +
                                "교과목이름 : " + 개설교과목DTOS.get(i).get교과목이름() + "\n" +
                                "담당교수 : " + 교수DAO.담당교수출력(개설교과목DTOS.get(i).get교과목번호()).get사용자명() + "\n" +
                                "대상학년 : " + 개설교과목DTOS.get(i).get대상학년() + "\n" +
                                "교육과정  : " + 개설교과목DTOS.get(i).get교육과정() + "\n" +
                                "이수구분  : " + 개설교과목DTOS.get(i).get이수구분() + "\n" +
                                "학점  : " + 개설교과목DTOS.get(i).get학점() + "\n" +
                                "설계과목여부  : " + 설계과목여부 + "\n" +
                                "최대수강인원  : " + 개설교과목DTOS.get(i).get최대수강인원() + "\n" +
                                "수강신청인원  : " + 개설교과목DTOS.get(i).get수강신청인원() + "\n" +
                                "강의실 : " + 개설교과목DTOS.get(i).get강의실() + "\n" +
                                "분반 : " + 개설교과목DTOS.get(i).get분반() + "\n" +
                                "수업요일  : " + 개설교과목DTOS.get(i).get수업요일() + "\n" +
                                "수업시간  : " + 개설교과목DTOS.get(i).get수업시작시간() + " ~ " + 개설교과목DTOS.get(i).get수업종료시간() + "\n";

                // 수강신청기간 및 강의계획서입력기간도 함께 출력
                if (개설교과목DTOS.get(i).get수강신청시작시간() != null && 개설교과목DTOS.get(i).get수강신청종료시간() != null) {
                    processedData += "수강신청기간  : " + 개설교과목DTOS.get(i).get수강신청시작시간().format(dateTimeFormatter) + " ~ " + 개설교과목DTOS.get(i).get수강신청종료시간().format(dateTimeFormatter) + "\n";
                } else processedData += "수강신청기간  : " + "설정안됨" + "\n";

                if (개설교과목DTOS.get(i).get강의계획서입력시작시간() != null && 개설교과목DTOS.get(i).get강의계획서입력종료시간() != null) {
                    processedData += "강의계획서입력기간  : " + 개설교과목DTOS.get(i).get강의계획서입력시작시간().format(dateTimeFormatter) + " ~ " + 개설교과목DTOS.get(i).get강의계획서입력종료시간().format(dateTimeFormatter) + "\n" +
                            "----------------------------------------------------------------";
                } else processedData += "강의계획서입력기간  : " + "설정안됨" + "\n" +
                        "----------------------------------------------------------------";
                //클라이언트의 메뉴구현 범용성을 위해 마지막 라인은 \n 하지않음
                if (i < 개설교과목DTOS.size() - 1)
                    processedData += "\n";
            }

            // 데이터와 함께 성공 메시지 전송
            ProtocolLibrary.sendSuccessProtocolWithData(os, protocol, buf, sv, processedData);
        } else if (code == Protocol.코드_관리자요청_개설교과목정보조회_교수학년별) {
            // 교수 이름 및 대상 학년을 받음
            String[] filter = protocol.getDatas();
            // 개설교과목목록  정보 내용 작성 후 전송
            개설교과목DAO 개설교과목DAO = new 개설교과목DAO(MyBatisConnectionFactory.getSqlSessionFactory());
            List<개설교과목DTO> 개설교과목DTOS = 개설교과목DAO.학년교수별개설교과목출력(filter[0], Integer.parseInt(filter[1]));

            // 개설교과목목록  정보 내용 작성 후 전송
            String processedData = "------------------------ 학년 교수별 개설교과목 목록 ------------------------ \n";

            for (int i = 0; i < 개설교과목DTOS.size(); i++) {
                String 개설여부 = "", 설계과목여부 = "";
                if (개설교과목DTOS.get(i).is설계과목여부()) {
                    설계과목여부 = "O";
                } else 설계과목여부 = "X";
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                교수DAO 교수DAO = new 교수DAO();  // 해당 개설교과목의 담당 교수를 함께 출력하기 위해 교수DAO 사용
                processedData +=
                        "교과목번호 : " + 개설교과목DTOS.get(i).get교과목번호() + "\n" +
                                "교과목이름 : " + 개설교과목DTOS.get(i).get교과목이름() + "\n" +
                                "담당교수 : " + 교수DAO.담당교수출력(개설교과목DTOS.get(i).get교과목번호()).get사용자명() + "\n" +
                                "대상학년 : " + 개설교과목DTOS.get(i).get대상학년() + "\n" +
                                "교육과정  : " + 개설교과목DTOS.get(i).get교육과정() + "\n" +
                                "이수구분  : " + 개설교과목DTOS.get(i).get이수구분() + "\n" +
                                "학점  : " + 개설교과목DTOS.get(i).get학점() + "\n" +
                                "설계과목여부  : " + 설계과목여부 + "\n" +
                                "최대수강인원  : " + 개설교과목DTOS.get(i).get최대수강인원() + "\n" +
                                "수강신청인원  : " + 개설교과목DTOS.get(i).get수강신청인원() + "\n" +
                                "강의실 : " + 개설교과목DTOS.get(i).get강의실() + "\n" +
                                "분반 : " + 개설교과목DTOS.get(i).get분반() + "\n" +
                                "수업요일  : " + 개설교과목DTOS.get(i).get수업요일() + "\n" +
                                "수업시간  : " + 개설교과목DTOS.get(i).get수업시작시간() + " ~ " + 개설교과목DTOS.get(i).get수업종료시간() + "\n";

                // 수강신청기간 및 강의계획서입력기간도 함께 출력
                if (개설교과목DTOS.get(i).get수강신청시작시간() != null && 개설교과목DTOS.get(i).get수강신청종료시간() != null) {
                    processedData += "수강신청기간  : " + 개설교과목DTOS.get(i).get수강신청시작시간().format(dateTimeFormatter) + " ~ " + 개설교과목DTOS.get(i).get수강신청종료시간().format(dateTimeFormatter) + "\n";
                } else processedData += "수강신청기간  : " + "설정안됨" + "\n";

                if (개설교과목DTOS.get(i).get강의계획서입력시작시간() != null && 개설교과목DTOS.get(i).get강의계획서입력종료시간() != null) {
                    processedData += "강의계획서입력기간  : " + 개설교과목DTOS.get(i).get강의계획서입력시작시간().format(dateTimeFormatter) + " ~ " + 개설교과목DTOS.get(i).get강의계획서입력종료시간().format(dateTimeFormatter) + "\n" +
                            "----------------------------------------------------------------";
                } else processedData += "강의계획서입력기간  : " + "설정안됨" + "\n" +
                        "----------------------------------------------------------------";
                //클라이언트의 메뉴구현 범용성을 위해 마지막 라인은 \n 하지않음
                if (i < 개설교과목DTOS.size() - 1)
                    processedData += "\n";
            }

            // 데이터와 함께 성공 메시지 전송
            ProtocolLibrary.sendSuccessProtocolWithData(os, protocol, buf, sv, processedData);
        } else {
            // 옳지 못한 통신
            ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
            return;
        }
    }
}
