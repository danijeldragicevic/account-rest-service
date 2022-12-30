package account.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class ApiError {
    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
