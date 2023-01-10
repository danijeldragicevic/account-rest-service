package account.model.dto;

import account.enums.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
@JsonPropertyOrder({
        "id",
        "name",
        "lastname",
        "email",
        "roles"
})
public class UserDto {

    private long Id;

    @NotBlank(message = "Name can not be empty!")
    private String name;

    @NotBlank(message = "Lastname can not be empty!")
    private String lastname;

    @Email(regexp = ".+@\\W*((?i)acme.com(?-i))\\W*", message = "Email should be of acme.com domain!")
    @NotBlank(message = "Bad Request")
    private String email;

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 12, message = "The password length must be at least 12 chars!")
    private String password;

    private Set<Role> roles;
}
