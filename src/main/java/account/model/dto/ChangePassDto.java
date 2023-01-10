package account.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class ChangePassDto {
    private String email;

    private String status;

    @NotBlank(message = "New password can not be empty!")
    @JsonProperty(value = "new_password", access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 12, message = "Password length must be 12 chars minimum!")
    private String password;
}
