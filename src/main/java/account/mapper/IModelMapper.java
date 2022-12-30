package account.mapper;

import account.enums.Role;
import account.model.Event;
import account.model.PaymentReq;
import account.model.PaymentResp;
import account.model.User;
import account.model.dto.EventDto;
import account.model.dto.PaymentReqDto;
import account.model.dto.PaymentRespDto;
import account.model.dto.UserDto;

import java.util.Set;

public interface IModelMapper {
    User mapToUserModel(UserDto userDto, Set<Role> roles);
    UserDto mapToUserDto(User user, Set<Role> roles);

    PaymentReq mapToPaymentReqModel(PaymentReqDto paymentReqDto);

    PaymentRespDto mapToPaymentRespDto(PaymentResp paymentResp);

    EventDto mapToEventDto(Event event);
}
