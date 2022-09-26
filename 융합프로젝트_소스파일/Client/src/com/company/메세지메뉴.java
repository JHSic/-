package com.company;

public class 메세지메뉴 {

    public static void 교수계정생성성공(){
        System.out.println(">> 입력하신 교수 계정이 생성되었습니다.");
    }

    public static void 학생계정생성성공(){
        System.out.println(">> 입력하신 학생 계정이 생성되었습니다.");
    }

    public static void 교과목생성성공(){
        System.out.println(">> 입력하신 교과목이 생성되었습니다.");
    }

    public static void 교과목수정성공(){
        System.out.println(">> 교과목 정보가 수정되었습니다.");
    }

    public static void 교과목삭제성공(){
        System.out.println(">> 입력하신 교과목이 삭제되었습니다.");
    }

    public static void 개설교과목생성성공(){
        System.out.println(">> 입력하신 개설교과목이 개설되었습니다.");
    }

    public static void 개설교과목수정성공(){
        System.out.println(">> 개설교과목 정보가 수정되었습니다.");
    }

    public static void 개설교과목삭제성공(){  System.out.println(">> 입력하신 개설교과목이 폐설되었습니다."); }

    public static void 강의계획서기간설정성공(){
        System.out.println(">> 기간이 설정되었습니다.");
    }

    public static void 수강신청기간설정성공(){
        System.out.println(">> 기간이 설정되었습니다.");
    }

    //교수, 학생 공통으로 사용
    public static void 개인정보수정성공() { System.out.println(">> 사용자 정보가 변경되었습니다."); }

    public static void 강의계획서입력성공() { System.out.println(">> 강의 계획서가 입력되었습니다."); }

    public static void 강의계획서수정성공() { System.out.println(">> 강의 계획서가 수정되었습니다."); }

    public static void 수강신청성공(){
        System.out.println(">> 해당 과목 수강신청에 성공했습니다.");
    }

    public static void 수강취소성공(){
        System.out.println(">> 해당 과목 수강신청을 취소했습니다.");
    }

    //초기화면에서 사용
    public static void 로그인성공(){
        System.out.println("로그인 성공!");
    }

    public static void 관리자계정생성성공(){
        System.out.println(">> 입력하신 관리자 계정이 생성되었습니다.");
    }

    public static void 연결종료() { System.out.println("종료합니다."); }

    public static void 오류로인한종료() { System.out.println("오류!"); }

}
