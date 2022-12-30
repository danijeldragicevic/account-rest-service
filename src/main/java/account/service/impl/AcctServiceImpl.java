package account.service.impl;

import account.model.PaymentReq;
import account.repository.IAcctRepository;
import account.service.IAcctService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AcctServiceImpl implements IAcctService {
    final IAcctRepository repository;

    @Override
    public Optional<PaymentReq> getPayment(String username, String period) {
        Optional<PaymentReq> paymentReq = repository.findPaymentReqByUsernameAndPeriod(username, period);

        return paymentReq;
    }

    @Override
    public Optional<List<PaymentReq>> getPayments(String username) {
        Optional<List<PaymentReq>> paymentReqList = repository.findPaymentReqsByUsernameOrderByPeriodDesc(username);

        return paymentReqList;
    }

    @Transactional
    @Override
    public List<PaymentReq> savePayments(List<PaymentReq> paymentReqs) {
        List<PaymentReq> paymentReqList = repository.saveAll(paymentReqs);

        return paymentReqList;
    }

    @Transactional
    @Override
    public PaymentReq updatePayment(PaymentReq currPayment, PaymentReq newPayment) {
        Optional<PaymentReq> paymentReq = repository.findById(currPayment.getId());
        paymentReq.get().setSalary(newPayment.getSalary());
        repository.save(paymentReq.get());

        return paymentReq.get();
    }
}
