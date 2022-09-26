package persistence.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class 교과목DTO {
    private String 교과목번호;
    private String 교과목이름;
    private int 대상학년;
    private String 교육과정;
    private String 이수구분;
    private int 학점;
    private boolean 설계과목여부;
    private boolean 개설여부;
}