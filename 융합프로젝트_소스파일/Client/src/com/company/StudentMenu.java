package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StudentMenu {  // 학생의 메인 메뉴
    public static void StudentMenuRun(InputStream ois, OutputStream oos, Protocol protocol) throws IOException {

        int num = -1;

        while (true) {
            학생메뉴.학생메뉴();
            num = NecessaryFunction.getInput(num);  // 유저의 입력을 받음

            if (num == 1) {
                StudentMainService.updateStudent(ois, oos, protocol); // 학생정보 수정
            } else if (num == 2) {
                StudentMainService.requestAndDeleteLecture(ois, oos, protocol); // 수강신청 및 취소
            } else if (num == 3) {
                StudentMainService.readOpenSubjectAll(ois, oos, protocol); // 개설교과목 조회
            } else if (num == 4) {
                StudentMainService.readSelectSyllabus(ois, oos, protocol); // 강의계획서 조회
            } else if (num == 5) {
                StudentMainService.readStudentSubjectTimeTable(ois, oos, protocol); // 시간표 조회
            } else if (num == 0) { // 로그아웃
                공통메뉴.로그아웃메뉴();
                return;
            } else if(num == -1) { // 잘못된 입력이 들어왔을 시 다시 입력받음
                continue;
            }
        }
    }
}
