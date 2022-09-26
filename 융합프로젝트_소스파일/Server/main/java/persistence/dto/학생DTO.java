package persistence.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)

//사용자의 정보를 학생이 상속받음
public class 학생DTO extends 사용자DTO {
    private int 학년;
    private int 신청과목수;
    private int 신청총학점;
}
