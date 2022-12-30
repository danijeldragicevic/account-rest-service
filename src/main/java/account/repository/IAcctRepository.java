package account.repository;

import account.model.PaymentReq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IAcctRepository extends JpaRepository<PaymentReq, Long> {
   Optional<PaymentReq> findById(Long id);

   Optional<PaymentReq> findPaymentReqByUsernameAndPeriod(String username, String period);

   Optional<List<PaymentReq>> findPaymentReqsByUsernameOrderByPeriodDesc(String username);
}
