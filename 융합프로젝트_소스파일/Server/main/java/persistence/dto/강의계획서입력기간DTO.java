package persistence.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class 강의계획서입력기간DTO {
    private String 교과목번호;
    private LocalDateTime 강의계획서입력시작시간;
    private LocalDateTime 강의계획서입력종료시간;
}
