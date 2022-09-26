package service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.format.DateTimeFormatter;
import java.util.List;

import network.Protocol;
import network.ProtocolLibrary;
import persistence.MyBatisConnectionFactory;
import persistence.dao.*;
import persistence.dto.개설교과목DTO;
import persistence.dto.담당과목목록DTO;
import persistence.dto.수강신청DTO;
import persistence.dto.학생DTO;
import view.ServerNetworkView;

public class RequestByProfessorAndStudentService {
    /*
        학생 및 교수 타입의 공통적인 요청에 대한 세부 기능들을 메소드로 구현한 클래스
     */
    private OutputStream os;
    private InputStream is;
    private Socket socket;
    private byte[] buf;
    private String[] userInfo;
    private ServerNetworkView sv;

    public RequestByProfessorAndStudentService(OutputStream os, InputStream is, Socket socket, byte[] buf, String[] userInfo) {
        this.os = os;
        this.is = is;
        this.socket = socket;
        this.buf = buf;
        this.userInfo = userInfo;
        sv = new ServerNetworkView(socket, userInfo);
    }

    public void run(Protocol protocol) throws IOException, ClassNotFoundException, InterruptedException {
        int code = protocol.getCode();

        sendProcessedData(os, protocol, code, buf); //조회 관련 내용
    }

    public void sendProcessedData(OutputStream os, Protocol protocol, int code, byte[] buf) throws IOException, ClassNotFoundException, InterruptedException {
        String userID = userInfo[0];
        sv.printReceiveProtocol(protocol);

        if (code == Protocol.코드_교수학생공통요청_개설교과목목록조회) {
            // 개설교과목목록  정보 내용 작성 후 전송
            개설교과목DAO 개설교과목DAO = new 개설교과목DAO(MyBatisConnectionFactory.getSqlSessionFactory());
            개설교과목DTO 개설교과목DTO = new 개설교과목DTO();
            List<개설교과목DTO> 개설교과목DTOS = 개설교과목DAO.개설교과목전체출력();

            // 개설교과목목록  정보 내용 작성
            String processedData = "------------------------ 개설교과목 목록 ------------------------ \n";

            for (int i = 0; i < 개설교과목DTOS.size(); i++) {
                String 개설여부 = "", 설계과목여부 = "";
                if (개설교과목DTOS.get(i).is설계과목여부()) {
                    설계과목여부 = "O";
                } else 설계과목여부 = "X";

                // 기간 포맷 설정
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                교수DAO 교수DAO = new 교수DAO();
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

            //가공한 데이터 전송
            ProtocolLibrary.sendSuccessProtocolWithData(os, protocol, buf, sv, processedData);

        } else if (code == Protocol.코드_교수학생공통요청_교과목시간표조회_교수) {
            // 교수의 개인 담당과목목록 조회 이때, DB 쿼리를 요일/시간 순으로 정렬 해서 조회
            담당과목목록DAO 담당과목목록DAO = new 담당과목목록DAO(MyBatisConnectionFactory.getSqlSessionFactory());
            담당과목목록DTO 담당과목목록DTO = new 담당과목목록DTO();
            담당과목목록DTO.set사용자ID(userID);
            List<개설교과목DTO> 개설교과목DTOS = 담당과목목록DAO.개인담당과목목록출력(담당과목목록DTO);

            // 데이터가 있는경우에만 요일 보여주기
            boolean 월요일_처음 = true;
            boolean 화요일_처음 = true;
            boolean 수요일_처음 = true;
            boolean 목요일_처음 = true;
            boolean 금요일_처음 = true;

            //담당과목 목록을 바탕으로 시간표 가공
            String processedData = "-------------시간표------------\n";

            for (int i = 0; i < 개설교과목DTOS.size(); i++) {
                if ("월".equals(개설교과목DTOS.get(i).get수업요일())) {
                    if (월요일_처음) {
                        processedData += "-------------월요일------------" + "\n";
                        월요일_처음 = false;
                    }
                    processedData +=
                            "교과목번호 : " + 개설교과목DTOS.get(i).get교과목번호() + "\n" +
                                    "교과목이름 : " + 개설교과목DTOS.get(i).get교과목이름() + "\n" +
                                    "수업시간  : " + 개설교과목DTOS.get(i).get수업시작시간() + " ~ " + 개설교과목DTOS.get(i).get수업종료시간() + "\n";
                }
                if ("화".equals(개설교과목DTOS.get(i).get수업요일())) {
                    if (화요일_처음) {
                        processedData += "-------------화요일------------" + "\n";
                        화요일_처음 = false;
                    }

                    processedData +=
                            "교과목번호 : " + 개설교과목DTOS.get(i).get교과목번호() + "\n" +
                                    "교과목이름 : " + 개설교과목DTOS.get(i).get교과목이름() + "\n" +
                                    "수업시간  : " + 개설교과목DTOS.get(i).get수업시작시간() + " ~ " + 개설교과목DTOS.get(i).get수업종료시간() + "\n";
                }

                if ("수".equals(개설교과목DTOS.get(i).get수업요일())) {
                    if (수요일_처음) {
                        processedData += "-------------수요일------------" + "\n";
                        수요일_처음 = false;
                    }

                    processedData +=
                            "교과목번호 : " + 개설교과목DTOS.get(i).get교과목번호() + "\n" +
                                    "교과목이름 : " + 개설교과목DTOS.get(i).get교과목이름() + "\n" +
                                    "수업시간  : " + 개설교과목DTOS.get(i).get수업시작시간() + " ~ " + 개설교과목DTOS.get(i).get수업종료시간() + "\n";
                }

                if ("목".equals(개설교과목DTOS.get(i).get수업요일())) {
                    if (목요일_처음) {
                        processedData += "-------------목요일------------" + "\n";
                        목요일_처음 = false;
                    }
                    processedData +=
                            "교과목번호 : " + 개설교과목DTOS.get(i).get교과목번호() + "\n" +
                                    "교과목이름 : " + 개설교과목DTOS.get(i).get교과목이름() + "\n" +
                                    "수업시간  : " + 개설교과목DTOS.get(i).get수업시작시간() + " ~ " + 개설교과목DTOS.get(i).get수업종료시간() + "\n";
                }

                if ("금".equals(개설교과목DTOS.get(i).get수업요일())) {
                    if (금요일_처음) {
                        processedData += "-------------금요일------------" + "\n";
                        금요일_처음 = false;
                    }
                    processedData +=
                            "교과목번호 : " + 개설교과목DTOS.get(i).get교과목번호() + "\n" +
                                    "교과목이름 : " + 개설교과목DTOS.get(i).get교과목이름() + "\n" +
                                    "수업시간  : " + 개설교과목DTOS.get(i).get수업시작시간() + " ~ " + 개설교과목DTOS.get(i).get수업종료시간() + "\n";
                }

                //마지막 라인은 \n 하지않음
                if (i < 개설교과목DTOS.size() - 1)
                    processedData += "\n";
            }
            //가공한 데이터 전송
            ProtocolLibrary.sendSuccessProtocolWithData(os, protocol, buf, sv, processedData);

        } else if (code == Protocol.코드_교수학생공통요청_교과목시간표조회_학생) {
            // 학생이 신청한 교과목의 정보 조회 이때, DB 쿼리를 요일/시간 순으로 정렬 해서 조회
            개설교과목DTO 개설교과목DTO = new 개설교과목DTO();
            수강신청DAO 수강신청DAO = new 수강신청DAO(MyBatisConnectionFactory.getSqlSessionFactory());
            수강신청DTO 수강신청DTO = new 수강신청DTO();
            수강신청DTO.set사용자ID(userID);
            List<개설교과목DTO> 개설교과목DTOS = 수강신청DAO.개인수강신청목록출력(수강신청DTO);

            // 학생의 신청 과목수와 신청 총학점 조회
            학생DAO 학생DAO = new 학생DAO();
            학생DTO 학생DTO = 학생DAO.선택학생출력(userID);
            String processedData = "-------------시간표------------\n";
            processedData += "신청과목수 : " + 학생DTO.get신청과목수() + "\n" +
                                "신청총학점 : " + 학생DTO.get신청총학점() + "\n";

            // 데이터가 있는경우에만 요일 보여주기
            boolean 월요일_처음 = true;
            boolean 화요일_처음 = true;
            boolean 수요일_처음 = true;
            boolean 목요일_처음 = true;
            boolean 금요일_처음 = true;

            //조회한 데이터를 바탕으로 시간표 데이터 가공
            for (int i = 0; i < 개설교과목DTOS.size(); i++) {
                if ("월".equals(개설교과목DTOS.get(i).get수업요일())) {
                    if (월요일_처음) {
                        processedData += "-------------월요일------------" + "\n ";
                        월요일_처음 = false;
                    }
                    processedData +=
                            "교과목번호 : " + 개설교과목DTOS.get(i).get교과목번호() + "\n" +
                                    "교과목이름 : " + 개설교과목DTOS.get(i).get교과목이름() + "\n" +
                                    "수업시간  : " + 개설교과목DTOS.get(i).get수업시작시간() + " ~ " + 개설교과목DTOS.get(i).get수업종료시간() + "\n";
                }
                if ("화".equals(개설교과목DTOS.get(i).get수업요일())) {
                    if (화요일_처음) {
                        processedData += "-------------화요일------------" + "\n";
                        화요일_처음 = false;
                    }

                    processedData +=
                            "교과목번호 : " + 개설교과목DTOS.get(i).get교과목번호() + "\n" +
                                    "교과목이름 : " + 개설교과목DTOS.get(i).get교과목이름() + "\n" +
                                    "수업시간  : " + 개설교과목DTOS.get(i).get수업시작시간() + " ~ " + 개설교과목DTOS.get(i).get수업종료시간() + "\n";
                }

                if ("수".equals(개설교과목DTOS.get(i).get수업요일())) {
                    if (수요일_처음) {
                        processedData += "-------------수요일------------" + "\n";
                        수요일_처음 = false;
                    }

                    processedData +=
                            "교과목번호 : " + 개설교과목DTOS.get(i).get교과목번호() + "\n" +
                                    "교과목이름 : " + 개설교과목DTOS.get(i).get교과목이름() + "\n" +
                                    "수업시간  : " + 개설교과목DTOS.get(i).get수업시작시간() + " ~ " + 개설교과목DTOS.get(i).get수업종료시간() + "\n";
                }

                if ("목".equals(개설교과목DTOS.get(i).get수업요일())) {
                    if (목요일_처음) {
                        processedData += "-------------목요일------------" + "\n";
                        목요일_처음 = false;
                    }
                    processedData +=
                            "교과목번호 : " + 개설교과목DTOS.get(i).get교과목번호() + "\n" +
                                    "교과목이름 : " + 개설교과목DTOS.get(i).get교과목이름() + "\n" +
                                    "수업시간  : " + 개설교과목DTOS.get(i).get수업시작시간() + " ~ " + 개설교과목DTOS.get(i).get수업종료시간() + "\n";
                }

                if ("금".equals(개설교과목DTOS.get(i).get수업요일())) {
                    if (금요일_처음) {
                        processedData += "-------------금요일------------" + "\n";
                        금요일_처음 = false;
                    }
                    processedData +=
                            "교과목번호 : " + 개설교과목DTOS.get(i).get교과목번호() + "\n" +
                                    "교과목이름 : " + 개설교과목DTOS.get(i).get교과목이름() + "\n" +
                                    "수업시간  : " + 개설교과목DTOS.get(i).get수업시작시간() + " ~ " + 개설교과목DTOS.get(i).get수업종료시간() + "\n";
                }

                //마지막 라인은 \n 하지않음
                if (i < 개설교과목DTOS.size() - 1)
                    processedData += "\n";
            }
            //가공한 데이터 전송
            ProtocolLibrary.sendSuccessProtocolWithData(os, protocol, buf, sv, processedData);
        } else {
            //타입과 코드가 옳바르지 않은경우 실패 메시지 전송
            ProtocolLibrary.sendFailProtocol(os, protocol, buf, sv, sv.getInvalidProtocolMessage());
        }
    }
}
