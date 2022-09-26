package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AdminMainService {

    public static void createUser(InputStream ois, OutputStream oos, Protocol protocol) throws IOException { // 계정 생성에 대한 세부 메뉴
        int select = -1;

        관리자메뉴.교수학생계정생성선택메뉴();
        select = NecessaryFunction.getInput(select); // 유저의 입력을 받음

        if(select == 1) {
            관리자메뉴.교수계정생성메뉴();
            AdminSubService.createProfessor(ois, oos, protocol); // 교수 생성
        }

        else if(select == 2) {
            관리자메뉴.학생계정생성메뉴();
            AdminSubService.createStudent(ois, oos, protocol); // 학생 생성
        }

        else if(select == 0) { // 뒤로가기
            return;
        }

        else if(select == -1) { // 잘못된 입력시 리턴
            return;
        }

    }

    public static void subjectCUD(InputStream ois, OutputStream oos, Protocol protocol) throws IOException { // 교과목 생성/수정/삭제에 대한 세부 메뉴
        int select = -1;

        관리자메뉴.교과목생성수정삭제선택메뉴();
        select = NecessaryFunction.getInput(select); // 유저의 입력을 받음

        if(select == 1) {
            관리자메뉴.교과목생성메뉴();
            AdminSubService.createSubject(ois, oos, protocol); // 교과목 생성
        }

        else if(select == 2) {
            관리자메뉴.교과목수정메뉴();
            AdminSubService.updateSubject(ois, oos, protocol); //교과목 수정
        }

        else if(select == 3) {
            관리자메뉴.교과목삭제메뉴();
            AdminSubService.deleteSubject(ois, oos, protocol); // 교과목 삭제
        }

        else if(select == 0) { // 뒤로가기
            return;
        }

        else if(select == -1) { // 잘못된 입력시 리턴
            return;
        }

    }

    public static void openSubjectCUD(InputStream ois, OutputStream oos, Protocol protocol) throws IOException { // 개설교과목 생성/수정/삭제에 대한 세부 메뉴
        int select = -1;

        관리자메뉴.개설교과목생성수정삭제선택메뉴();
        select = NecessaryFunction.getInput(select); // 유저의 입력을 받음

        if(select == 1) {
            관리자메뉴.개설교과목생성메뉴();
            AdminSubService.createOpenSubject(ois, oos, protocol); // 개설교과목 생성
        }

        else if(select == 2) {
            관리자메뉴.개설교과목수정메뉴();
            AdminSubService.updateOpenSubject(ois, oos, protocol); // 개설교과목 수정
        }

        else if(select == 3) {
            관리자메뉴.개설교과목삭제메뉴();
            AdminSubService.deleteOpenSubject(ois, oos, protocol); // 개설교과목 삭제
        }

        else if(select == 0) { // 뒤로가기
            return;
        }

        else if(select == -1) { // 잘못된 입력시 리턴
            return;
        }

    }

    public static void inputSyllabus(InputStream ois, OutputStream oos, Protocol protocol) throws IOException { // 강의계획서 입력기간에 대한 세부 메뉴
        int select = -1;

        관리자메뉴.강의계획서입력기간설정메뉴();
        select = NecessaryFunction.getInput(select);

        if(select == 1) {
            관리자메뉴.교과목번호입력메뉴();
            AdminSubService.inputStartAndEndSyllabus(ois, oos, protocol); // 강의계획서 입력 시작기간/종료기간 설정
        }

        else if(select == 2) {
            관리자메뉴.교과목번호입력메뉴();
            AdminSubService.inputStartSyllabus(ois, oos, protocol); // 강의계획서 입력 시작기간 설정
        }

        else if(select == 3) {
            관리자메뉴.교과목번호입력메뉴();
            AdminSubService.inputEndSyllabus(ois, oos, protocol); // 강의계획서 입력 종료기간 설정
        }

        else if(select == 0) { // 뒤로가기
            return;
        }

        else if(select == -1) { // 잘못된 입력시 리턴
            return;
        }

    }

    public static void inputCourse(InputStream ois, OutputStream oos, Protocol protocol) throws IOException { // 수강신청기간 입력에 대한 세부 메뉴
        int select = -1;

        관리자메뉴.학년별수강신청기간설정메뉴();
        select = NecessaryFunction.getInput(select); // 유저의 입력을 받음

        if(select == 1) {
            AdminSubService.inputStartAndEndCourse(ois, oos, protocol); // 수강신청 시작기간/종료기간 설정
        }

        else if(select == 2) {
            AdminSubService.inputStartCourse(ois, oos, protocol); // 수강신청 시작기간 설정
        }

        else if(select == 3) {
            AdminSubService.inputEndCourse(ois, oos, protocol); // 수강신청 종료기간 설정
        }

        else if(select == 0) { // 뒤로가기
            return;
        }

        else if(select == -1) { // 잘못된 입력시 리턴
            return;
        }

    }

    public static void readUser(InputStream ois, OutputStream oos, Protocol protocol) throws IOException { // 계정 조회에 대한 세부 메뉴
        int select = -1;

        관리자메뉴.교수학생정보조회메뉴();
        select = NecessaryFunction.getInput(select); // 유저의 입력을 받음

        if(select == 1) {
            관리자메뉴.교수정보조회메뉴();
            AdminSubService.readProfessor(ois, oos, protocol); // 교수 정보 조회
        }

        else if(select == 2) {
            관리자메뉴.학생정보조회메뉴();
            AdminSubService.readStudent(ois, oos, protocol); // 학생 정보 조회
        }

        else if(select == 0) { // 뒤로가기
            return;
        }

        else if(select == -1) { // 잘못된 입력시 리턴
            return;
        }

    }

    public static void readSubject(InputStream ois, OutputStream oos, Protocol protocol) throws IOException { // 개설교과목 정보 조회에 대한 세부 메뉴
        int select = -1;

        관리자메뉴.개설교과목정보조회메뉴();
        select = NecessaryFunction.getInput(select); // 유저의 입력을 받음

        if(select == 1) {
            관리자메뉴.개설교과목전체정보조회메뉴();
            AdminSubService.readSubjectAll(ois, oos, protocol); // 전체 개설교과목 정보 조회
        }

        else if(select == 2) {
            관리자메뉴.개설교과목교수별정보조회메뉴();
            AdminSubService.readSubjectProfessor(ois, oos, protocol); // 교수별 개설교과목 정보 조회
        }

        else if(select == 3) {
            관리자메뉴.개설교과목학생별정보조회메뉴();
            AdminSubService.readSubjectGrade(ois, oos, protocol); // 학생별 개설교과목 정보 조회
        }

        else if(select == 4) {
            관리자메뉴.개설교과목교수학년별조회메뉴();
            AdminSubService.readSubjectProfessorAndGrade(ois, oos, protocol); // 교수/학생별 개설교과목 정보 조회
        }

        else if(select == 0) { // 뒤로가기
            return;
        }

        else if(select == -1) { // 잘못된 입력시 리턴
            return;
        }

    }

}
