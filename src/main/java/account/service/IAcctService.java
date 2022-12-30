package account.service;

import account.model.PaymentReq;

import java.util.List;
import java.util.Optional;

public interface IAcctService {
    Optional<PaymentReq> getPayment(String username, String period);

    Optional<List<PaymentReq>> getPayments(String username);

    List<PaymentReq> savePayments(List<PaymentReq> paymentReqs);

    PaymentReq updatePayment(PaymentReq currPayment, PaymentReq newPayment);
}
