package persistence.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class 수강신청기간DTO {
    private String 교과목번호;
    private LocalDateTime 수강신청시작시간;
    private LocalDateTime 수강신청종료시간;
}