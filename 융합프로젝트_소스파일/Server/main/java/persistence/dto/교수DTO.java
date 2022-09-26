package persistence.dto;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)

//교수가 사용자의 정보를 상속받음
public class 교수DTO extends 사용자DTO {
    private String 교수실;
    private String 교수실전화번호;
}
