package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AdminMenu { // 관리자의 메인 메뉴
    public static void AdminMenuRun(InputStream ois, OutputStream oos, Protocol protocol) throws IOException {

        int num = -1;

        while(true) {
            관리자메뉴.관리자메뉴();
            num = NecessaryFunction.getInput(num); // 유저의 입력을 받음

            if(num == 1) {
                AdminMainService.createUser(ois, oos, protocol); // 계정 생성
            }

            else if(num == 2) {
                AdminMainService.subjectCUD(ois, oos, protocol); // 교과목 생성/수정/삭제
            }

            else if(num == 3) {
                AdminMainService.openSubjectCUD(ois, oos, protocol); // 개설교과목 생성/수정/삭제
            }

            else if(num == 4) {
                AdminMainService.inputSyllabus(ois, oos, protocol); // 강의계획서 기간 입력
            }

            else if(num == 5) {
                AdminMainService.inputCourse(ois, oos, protocol); // 수강 신청 기간 입력
            }

            else if(num == 6) {
                AdminMainService.readUser(ois, oos, protocol); // 사용자 조회
            }

            else if(num == 7) {
                AdminMainService.readSubject(ois, oos, protocol); // 교과목 조회
            }

            else if(num == 0) { // 로그아웃
                공통메뉴.로그아웃메뉴();
                return;
            }

            else if(num == -1) { // 잘못된 입력이 들어왔을 시 다시 입력받음
                continue;
            }
        }
    }
}