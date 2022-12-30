package account.model;

import lombok.*;

import java.time.Month;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class PaymentResp {
    private String name;
    private String lastname;
    private String period;
    private String salary;

    public void setPeriod(String period) {
        int monthNumber = Integer.parseInt(period.split("-")[0]);
        String month = Month.of(monthNumber).toString();
        this.period = month.substring(0,1).toUpperCase() + month.substring(1).toLowerCase() + "-" + period.split("-")[1];
    }

    public void setSalary(Long salary) {
        this.salary = String.format("%s dollar(s) %s cent(s)", salary/100, salary%100);
    }
}
