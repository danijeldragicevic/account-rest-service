package account.controller.impl;

import account.controller.IEmplController;
import account.mapper.impl.ModelMapperImpl;
import account.model.PaymentReq;
import account.model.PaymentResp;
import account.model.User;
import account.model.dto.PaymentRespDto;
import account.service.IAcctService;
import account.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class EmplControllerImpl implements IEmplController {
    private final IAcctService acctService;
    private final IAuthService authService;
    private final ModelMapperImpl mapper;

    @Override
    public ResponseEntity<?> getPayments(UserDetails userDetails, String period) throws ConstraintViolationException {
        Optional<PaymentReq> paymentReq = acctService.getPayment(userDetails.getUsername(), period);
        Optional<List<PaymentReq>> paymentReqs = acctService.getPayments(userDetails.getUsername());
        Optional<User> user = authService.findUserByEmail(userDetails.getUsername());

        if (period != null && !period.isEmpty()) {
            return createResponseOrEmptyObj(paymentReq, user);
        } else {
            return createResponseOrEmptyArray(paymentReqs, user);
        }
    }

    private ResponseEntity<?> createResponseOrEmptyObj(Optional<PaymentReq> req, Optional<User> u) {
        if (req.isPresent()) {
            PaymentResp paymentResp = createPaymentRespObj(req.get(), u.get());
            PaymentRespDto paymentRespDto = mapper.mapToPaymentRespDto(paymentResp);

            return ResponseEntity.ok(paymentRespDto);
        } else {
            return ResponseEntity.ok("{}");
        }
    }

    private ResponseEntity<?> createResponseOrEmptyArray(Optional<List<PaymentReq>> reqs, Optional<User> u) {
        if (reqs.isPresent()) {
            List<PaymentResp> paymentResps = new ArrayList<>();
            for (PaymentReq req: reqs.get()) {
                paymentResps.add(createPaymentRespObj(req, u.get()));
            }
            List<PaymentRespDto> paymentRespDtos = new ArrayList<>();
            for (PaymentResp p: paymentResps) {
                paymentRespDtos.add(mapper.mapToPaymentRespDto(p));
            }

            return ResponseEntity.ok(paymentRespDtos);
        } else {
            return ResponseEntity.ok("[]");
        }
    }

    private PaymentResp createPaymentRespObj (PaymentReq req, User u) {
        PaymentResp resp = new PaymentResp();
        resp.setName(u.getName());
        resp.setLastname(u.getLastname());
        resp.setPeriod(req.getPeriod());
        resp.setSalary(req.getSalary());

        return resp;
    }
}
