package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ProfessorMainService {

    public static void updateProfessor(InputStream ois, OutputStream oos, Protocol protocol) throws IOException {
        공통메뉴.개인정보및비밀번호수정메뉴();
        ProfessorSubService.updateProfessor(ois, oos, protocol); // 교수정보 수정

    }
    public static void readOpenSubject(InputStream ois, OutputStream oos, Protocol protocol) throws IOException {
        공통메뉴.개설교과목목록조회메뉴();
        ProfessorSubService.readOpenSubject(ois, oos, protocol); // 개설교과목 조회

    }
    public static void insertAndUpdateSyllabus(InputStream ois, OutputStream oos, Protocol protocol) throws IOException { // 강의계획서 입력 및 수정에 대한 세부 메뉴
        int select = -1;

        교수메뉴.강의계획서입력수정메뉴();
        select = NecessaryFunction.getInput(select); // 유저의 입력을 받음

        if(select == 1) {
            교수메뉴.강의계획서입력메뉴();
            ProfessorSubService.insertSyllabus(ois, oos, protocol); // 강의계획서 입력
        }

        else if(select == 2) {
            교수메뉴.강의계획서수정메뉴();
            ProfessorSubService.updateSyllabus(ois, oos, protocol); // 강의계획서 수정
        }

        else if(select == 0 ){ // 뒤로가기
            return;
        }

        else if(select == -1) { // 잘못된 입력시 리턴
            return;
        }


    }
    public static void readOpenSubjectSyllabus(InputStream ois, OutputStream oos, Protocol protocol) throws IOException {
        교수메뉴.개설교과목강의계획서조회메뉴();
        ProfessorSubService.readOpenSubjectSyllabus(ois, oos, protocol); // 강의계획서 조회

    }
    public static void readStudentListOnSubject(InputStream ois, OutputStream oos, Protocol protocol) throws IOException {
        교수메뉴.교과목수강신청학생목록조회메뉴();
        ProfessorSubService.readStudentListOnSubject(ois, oos, protocol); // 학생목록 조회

    }

    public static void readSubjectTimeTable(InputStream ois, OutputStream oos, Protocol protocol) throws IOException {
        교수메뉴.교과목시간표조회();
        ProfessorSubService.readSubjectTimeTable(ois, oos, protocol); // 시간표 조회
    }
}