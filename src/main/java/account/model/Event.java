package account.model;

import account.enums.AppEvent;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity(name = "events")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class Event implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private AppEvent action;

    private String subject;

    private String object;

    private String path;
}
