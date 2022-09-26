package persistence.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class 수강신청DTO {
    private String 교과목번호;
    private String 사용자ID;
    private int 페이지;
}
