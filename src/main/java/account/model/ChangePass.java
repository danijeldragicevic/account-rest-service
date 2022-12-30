package account.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class ChangePass {
    private String email;
    private String status;
    private String password;
}
