package com.company;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
public class StudentMainService {

    public static void updateStudent(InputStream ois, OutputStream oos, Protocol protocol) throws IOException {
        공통메뉴.개인정보및비밀번호수정메뉴();
        StudentSubService.updateStudent(ois, oos, protocol); // 학생정보 수정
    }

    public static void requestAndDeleteLecture(InputStream ois, OutputStream oos, Protocol protocol) throws IOException { // 수강신청 및 취소에 대한 세부 메뉴
        int select = -1;

        학생메뉴.수강신청취소메뉴();
        select = NecessaryFunction.getInput(select); // 유저의 입력을 받음

        if(select == 1) {
            학생메뉴.수강신청메뉴();
            StudentSubService.requestLecture(ois, oos, protocol); // 수강신청
        }

        else if(select == 2) {
            학생메뉴.수강취소메뉴();
            StudentSubService.deleteLecture(ois, oos, protocol); // 수강취소
        }

        else if(select == 0 ){ // 뒤로가기
            return;
        }

        else if(select == -1) { // 잘못된 입력시 리턴
            return;
        }


    }

    public static void readOpenSubjectAll(InputStream ois, OutputStream oos, Protocol protocol) throws IOException {
        학생메뉴.개설교과목목록전학년조회();
        StudentSubService.readOpenSubjectAll(ois, oos, protocol); // 개설교과목 조회
    }

    public static void readSelectSyllabus(InputStream ois, OutputStream oos, Protocol protocol) throws IOException {
        학생메뉴.선택교과목강의계획서조회();
        StudentSubService.readSelectSyllabus(ois, oos, protocol); // 강의계획서 조회
    }

    public static void readStudentSubjectTimeTable(InputStream ois, OutputStream oos, Protocol protocol) throws IOException {
        학생메뉴.본인시간표조회();
        StudentSubService.readStudentSubjectTimeTable(ois, oos, protocol); // 시간표 조회
    }

}
