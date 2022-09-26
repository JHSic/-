package persistence.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor

//개설교과목이 교과목의 정보를 상속
public class 개설교과목DTO extends 교과목DTO {
    private int 최대수강인원;
    private int 수강신청인원;
    private String 강의실;
    private String 강의계획;
    private int 분반;

    // 다중속성
    private String 수업요일;
    private LocalTime 수업시작시간;
    private LocalTime 수업종료시간;

    // 다중속성
    private LocalDateTime 수강신청시작시간;
    private LocalDateTime 수강신청종료시간;

    private boolean 수강신청가능여부;

    // 다중속성
    private LocalDateTime 강의계획서입력시작시간;
    private LocalDateTime 강의계획서입력종료시간;

    private boolean 강의계획서입력가능여부;
}




