package account.model;

import account.enums.Role;
import account.enums.RoleOperation;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class UserRole {
    private String user;
    private Role role;
    private RoleOperation roleOperation;
}