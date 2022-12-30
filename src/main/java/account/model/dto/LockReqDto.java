package account.model.dto;

import account.enums.Enum;
import account.enums.LockOperation;
import account.validator.EnumValidationGroup;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.groups.Default;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class LockReqDto {
    @NotBlank(message = "Bad Request")
    @JsonProperty(value = "user")
    private String email;

    @NotBlank(message = "Bad Request", groups = Default.class)
    @Enum(enumClass = LockOperation.class, ignoreCase = true,
            message = "Operation field should be LOCK or UNLOCK.", groups = EnumValidationGroup.class)
    private String operation;
}
