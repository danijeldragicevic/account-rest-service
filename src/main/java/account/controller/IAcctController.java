package account.controller;

import account.exception.DuplicatePaymentEntryException;
import account.model.dto.PaymentReqDto;
import account.validator.ValidList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Map;

@RequestMapping("/api/acct")
public interface IAcctController {
    @PostMapping("/payments")
    ResponseEntity<Map<String, String>> postPayments(@RequestBody @Valid ValidList<PaymentReqDto> paymentReqDtos)
            throws DuplicatePaymentEntryException;

    @PutMapping("/payments")
    ResponseEntity<Map<String, String>> putPayment(@RequestBody @Valid PaymentReqDto paymentReqDto);
}
