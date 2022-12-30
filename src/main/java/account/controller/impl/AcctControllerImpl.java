package account.controller.impl;

import account.controller.IAcctController;
import account.exception.DuplicatePaymentEntryException;
import account.exception.PaymentNotFoundException;
import account.mapper.IModelMapper;
import account.model.PaymentReq;
import account.model.dto.PaymentReqDto;
import account.service.IAcctService;
import account.validator.ValidList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class AcctControllerImpl implements IAcctController {
    private final IAcctService service;
    private final IModelMapper mapper;

    @Override
    public ResponseEntity<Map<String, String>> postPayments(ValidList<PaymentReqDto> paymentReqDtosList)
            throws DuplicatePaymentEntryException{

        Set<PaymentReqDto> paymentReqDtosSet = removeDuplicateDtos(paymentReqDtosList);
        if (paymentReqDtosSet.size() < paymentReqDtosList.size()) {
            throw new DuplicatePaymentEntryException();
        }

        List<PaymentReq> paymentReqList = new ArrayList<>();
        for (PaymentReqDto p: paymentReqDtosList) {
            paymentReqList.add(mapper.mapToPaymentReqModel(p));
        }
        service.savePayments(paymentReqList);

        return ResponseEntity.ok(Map.of("status", "Added successfully!"));
    }

    @Override
    public ResponseEntity<Map<String, String>> putPayment(PaymentReqDto paymentReqDto)
            throws PaymentNotFoundException {

        Optional<PaymentReq> currPayment = service.getPayment(paymentReqDto.getEmployee(), paymentReqDto.getPeriod());
        if (currPayment.isPresent()) {
            PaymentReq newPayment = mapper.mapToPaymentReqModel(paymentReqDto);
            service.updatePayment(currPayment.get(), newPayment);
        } else {
            throw new PaymentNotFoundException();
        }

        return ResponseEntity.ok(Map.of("status", "Updated successfully!"));
    }

    private Set<PaymentReqDto> removeDuplicateDtos(ValidList<PaymentReqDto> dtos) {
        List<PaymentReqDto> list = new LinkedList<>();
        for (PaymentReqDto p: dtos) {
            PaymentReqDto dto = new PaymentReqDto();
            dto.setEmployee(p.getEmployee());
            dto.setPeriod(p.getPeriod());
            list.add(dto);
        }

        return new HashSet<>(list);
    }
}
