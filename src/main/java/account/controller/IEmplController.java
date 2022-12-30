package account.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Pattern;

@Validated
@RequestMapping("/api/empl")
public interface IEmplController {
    @GetMapping(value = "/payment")
    ResponseEntity<?> getPayments(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(name = "period", required = false) @Pattern(regexp = "^((0[1-9])|(1[0-2]))\\-(\\d{4})$") String period)
            throws ConstraintViolationException;
}
