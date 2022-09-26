package persistence.dto;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class 강의시간DTO {
    private String 교과목번호;
    private String 수업요일;
    private LocalTime 수업시작시간;
    private LocalTime 수업종료시간;

}
