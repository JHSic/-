package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ProfessorMenu { // 교수의 메인 메뉴
    public static void ProfessorMenuRun(InputStream ois, OutputStream oos, Protocol protocol) throws IOException {

        int num = -1;

        while (true) {
            교수메뉴.교수메뉴();
            num = NecessaryFunction.getInput(num); // 유저의 입력을 받음

            if (num == 1) {
                ProfessorMainService.updateProfessor(ois, oos, protocol); // 교수정보 수정
            } else if (num == 2) {
                ProfessorMainService.readOpenSubject(ois, oos, protocol); // 개설교과목 조회
            } else if (num == 3) {
                ProfessorMainService.insertAndUpdateSyllabus(ois, oos, protocol); // 강의계획서 입력 및 수정
            } else if (num == 4) {
                ProfessorMainService.readOpenSubjectSyllabus(ois, oos, protocol); // 강의계획서 조회
            } else if (num == 5) {
                ProfessorMainService.readStudentListOnSubject(ois, oos, protocol); // 학생목록 조회
            } else if (num == 6) {
                ProfessorMainService.readSubjectTimeTable(ois, oos, protocol); // 시간표 조회
            } else if (num == 0) { // 로그아웃
                공통메뉴.로그아웃메뉴();
                return;
            } else if(num == -1) { // 잘못된 입력이 들어왔을 시 다시 입력받음
                continue;
            }
        }
    }
}
