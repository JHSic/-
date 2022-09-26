package com.company;

public class Protocol_Const  {
    // =====================================================================================
    //데이터 길이에 관한 변수
    protected static final byte 타입길이= 1; // 프로토콜 타입 길이
    protected static final byte 코드길이 = 1; // 프로토콜 코드 길이
    protected static final byte 카운트길이 = 1; // 프로토콜 카운트 길이
    protected static final byte 가변데이터길이 = 2; // 프로토콜 전체 가변데이터 길이
    protected static final byte 개별데이터길이 = 2; // 프로토콜 개별데이터 길이
    protected static final byte 헤더길이 = 타입길이 + 코드길이 + 가변데이터길이; // 프로토콜 헤더 길이
    public static final int 최대길이 = 10000; // 최대 데이터 길이

    // =======================================프로토콜 타입=======================================
    //프로토콜 타입
    public static final byte 미정의 = -1;  //프로토콜이 지정되어 있지 않은 경우
    public static final byte 타입_종료 = 0;        //프로그램 종료 알림

    public static final byte 타입_요청_공통 = 0x10;
    public static final byte 타입_요청_관리자 = 0x11;
    public static final byte 타입_요청_교수 = 0x12;
    public static final byte 타입_요청_학생 = 0x13;
    public static final byte 타입_요청_교수학생공통 = 0x14;

    public static final byte 타입_응답 = 0x20;

    public static final byte 타입_정보전송_ID = 0x30;
    public static final byte 타입_정보전송_PW = 0x31;
    public static final byte 타입_정보전송_교과목번호 = 0x32;
    public static final byte 타입_정보전송_계정정보 = 0x33;
    public static final byte 타입_정보전송_교과목정보 = 0x34;
    public static final byte 타입_정보전송_개설교과목정보 = 0x35;
    public static final byte 타입_정보전송_강의계획서기간정보 = 0x36;
    public static final byte 타입_정보전송_수강신청기간정보 = 0x37;
    public static final byte 타입_정보전송_강의계획서입력정보 = 0x38;
    public static final byte 타입_정보전송_수강신청및취소정보 = 0x39;
    // =====================================================================================

    // =======================================프로토콜 코드=======================================
    //요청타입에 관한 코드_공통
    public static final byte 코드_공통요청_관리자계정생성 = 0x00;

    public static final byte 코드_공통요청_로그인 = 0x10;
    public static final byte 코드_공통요청_로그인_ID = 0x11;
    public static final byte 코드_공통요청_로그인_PW = 0x12;

    public static final byte 코드_공통요청_개인정보수정 = 0x20;

    //요청타입에 관한 코드_관리자
    public static final byte 코드_관리자요청_계정생성_교수 = 0x00;
    public static final byte 코드_관리자요청_계정생성_학생 = 0x01;
    //public static final byte 코드_관리자요청_계정삭제 = 0x02;

    public static final byte 코드_관리자요청_교과목생성 = 0x10;
    public static final byte 코드_관리자요청_교과목수정 = 0x11;
    public static final byte 코드_관리자요청_교과목삭제 = 0x12;

    public static final byte 코드_관리자요청_개설교과목생성 = 0x20;
    public static final byte 코드_관리자요청_개설교과목수정 = 0x21;
    public static final byte 코드_관리자요청_개설교과목삭제 = 0x22;

    public static final byte 코드_관리자요청_강의계획서시작기간설정 = 0x30;
    public static final byte 코드_관리자요청_강의계획서종료기간설정 = 0x31;
    public static final byte 코드_관리자요청_강의계획서시작및종료기간설정 = 0x32;

    public static final byte 코드_관리자요청_학년별수강신청시작기간설정 = 0x40;
    public static final byte 코드_관리자요청_학년별수강신청종료기간설정 = 0x41;
    public static final byte 코드_관리자요청_학년별수강신청시작및종료기간설정 = 0x42;

    //public static final byte 코드_관리자요청_계정정보조회_관리자 = 0x50;
    public static final byte 코드_관리자요청_계정정보조회_교수 = 0x51;
    public static final byte 코드_관리자요청_계정정보조회_학생 = 0x52;

    public static final byte 코드_관리자요청_개설교과목정보조회_전체 = 0x60;
    public static final byte 코드_관리자요청_개설교과목정보조회_교수별 = 0x61;
    public static final byte 코드_관리자요청_개설교과목정보조회_학년별 = 0x62;
    public static final byte 코드_관리자요청_개설교과목정보조회_교수학년별 = 0x63;

    //요청타입에 관한 코드_교수
    public static final byte 코드_교수요청_강의계획서입력 = 0x00;
    public static final byte 코드_교수요청_강의계획서수정 = 0x01;

    public static final byte 코드_교수요청_수강신청학생목록조회 = 0x10;

    public static final byte 코드_교수요청_담당개설교과목강의계획서조회 = 0x20;

    //요청타입에 관한 코드_학생
    public static final byte 코드_학생요청_수강신청및취소 = 0x00;

    public static final byte 코드_학생요청_특정개설교과목강의계획서조회 = 0x10;

    //요청타입에 관한 코드_교수학생공통
    public static final byte 코드_교수학생공통요청_교과목시간표조회_교수 = 0x00;
    public static final byte 코드_교수학생공통요청_교과목시간표조회_학생 = 0x01;

    public static final byte 코드_교수학생공통요청_개설교과목목록조회 = 0x20;

    //응답타입에 관한 코드
    public static final byte 코드_응답_성공 = 0x01;
    public static final byte 코드_응답_실패 = 0x02;
    public static final byte 코드_응답_데이터전송 = 0x03;

    //정보전송타입에 관한 코드
    public static final byte 코드_정보전송_계정정보_관리자 = 0x00;
    public static final byte 코드_정보전송_계정정보_교수 = 0x01;
    public static final byte 코드_정보전송_계정정보_학생 = 0x02;

    public static final byte 코드_정보전송_수강신청및취소정보_신청 = 0x10;
    public static final byte 코드_정보전송_수강신청및취소정보_취소 = 0x11;

    public static final byte 코드_정보전송_강의계획서기간정보_시작기간 = 0x20;
    public static final byte 코드_정보전송_강의계획서기간정보_종료기간 = 0x21;
    public static final byte 코드_정보전송_강의계획서기간정보_시작및종료기간 = 0x22;

    public static final byte 코드_정보전송_수강신청기간정보_시작기간 = 0x30;
    public static final byte 코드_정보전송_수강신청기간정보_종료기간 = 0x31;
    public static final byte 코드_정보전송_수강신청기간정보_시작및종료기간 = 0x32;
    // =====================================================================================
}

//길이정보 한글로 바꿈
//요청에서 강의계획서 입력기간 시작/종료/시작및종료로 구분
//정보전송에서 강의계획서 입력기간 시작/종료/시작및종료로 구분

