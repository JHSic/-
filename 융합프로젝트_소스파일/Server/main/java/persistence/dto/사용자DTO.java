package persistence.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@Getter
@Setter
@SuperBuilder
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class 사용자DTO {
    private String 사용자ID;
    private String 사용자명;
    private String 사용자PW;
    private String 주소;
    private String 주민번호;
    private int 사용자유형;
    private String 전화번호;
    private String 학과명;
}
