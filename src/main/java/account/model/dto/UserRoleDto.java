package account.model.dto;

import account.enums.Enum;
import account.enums.RoleOperation;
import account.validator.EnumValidationGroup;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.groups.Default;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class UserRoleDto {

    @NotBlank(message = "User can not be empty!")
    private String user;

    @NotBlank(message = "Role can not be empty!")
    private String role;

    // Using groups to control the execution sequence (1. Default.class, 2. EnumValidationGroup).
    // See: account.validator.ValidationSequence
    @NotBlank(message = "Operation can not be empty!", groups = Default.class)
    @Enum(enumClass = RoleOperation.class, ignoreCase = true,
            message = "Operation field should be GRANT or REMOVE.", groups = EnumValidationGroup.class)
    private String operation;

    public void setRole(String role) {
        if (role.startsWith("ROLE_")) {
            this.role = role;
        } else {
            this.role = "ROLE_" + role;
        }
    }
}
