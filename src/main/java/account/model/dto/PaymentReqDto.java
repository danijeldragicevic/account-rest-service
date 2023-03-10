package account.model.dto;

import lombok.*;

import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class PaymentReqDto {
    @NotBlank(message = "Employee can not be empty!")
    @Email(regexp = ".+@\\W*((?i)acme.com(?-i))\\W*", message = "Email should be of acme.com domain!")
    private String employee;

    @NotBlank(message = "Period can not be empty!")
    @Pattern(regexp = "^((0[1-9])|(1[0-2]))\\-(\\d{4})$", message = "Wrong date!")
    private String period;

    @NotNull(message = "Salary can not be empty!")
    @Min(value = 0L, message = "Salary must be non negative!")
    private Long salary;
}
