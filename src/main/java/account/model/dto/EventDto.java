package account.model.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
@JsonPropertyOrder({
        "id",
        "date",
        "action",
        "subject",
        "object",
        "path"
})
public class EventDto implements Serializable {
    private Long Id;
    private String date;
    private String action;
    private String subject;
    private String object;
    private String path;
}