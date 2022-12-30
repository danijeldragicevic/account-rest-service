package account.model;

import account.enums.LockOperation;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class LockReq {
    private User user;
    private LockOperation operation;
}
