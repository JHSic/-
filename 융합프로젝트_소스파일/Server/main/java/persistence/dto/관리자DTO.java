package persistence.dto;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)

//관리자가 사용자의 정보를 상속받는데 관리자는 자신의 고유한 정보를 가지지 않으므로 DTO가 빈틀
public class 관리자DTO extends 사용자DTO {
}