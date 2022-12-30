package account.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class PaymentRespDto {
    private String name;
    private String lastname;
    private String period;
    private String salary;
}
