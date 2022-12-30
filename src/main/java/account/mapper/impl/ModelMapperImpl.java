package account.mapper.impl;

import account.configuration.BeansConfig;
import account.enums.Role;
import account.mapper.IModelMapper;
import account.model.Event;
import account.model.PaymentReq;
import account.model.PaymentResp;
import account.model.User;
import account.model.dto.EventDto;
import account.model.dto.PaymentReqDto;
import account.model.dto.PaymentRespDto;
import account.model.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

public class ModelMapperImpl implements IModelMapper {

    @Autowired
    private BeansConfig config;

    @Override
    public User mapToUserModel(UserDto userDto, Set<Role> roles) {
        User user = new User();
        user.setName(userDto.getName());
        user.setLastname(userDto.getLastname());
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getEmail().toLowerCase());
        user.setPassword(config.getEncoder().encode(userDto.getPassword()));
        user.setRoles(roles);

        return user;
    }

    @Override
    public UserDto mapToUserDto(User user, Set<Role> roles) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setLastname(user.getLastname());
        userDto.setEmail(user.getEmail().toLowerCase());
        userDto.setPassword(user.getPassword());

        // Sort role values alphabetically
        List<Role> rolesSorted = roles.stream().collect(Collectors.toList());
        Collections.sort(rolesSorted, Comparator.comparing(Enum::toString));

        userDto.setRoles(new LinkedHashSet<>(rolesSorted));
        return userDto;
    }

    @Override
    public PaymentReq mapToPaymentReqModel(PaymentReqDto paymentReqDto) {
        PaymentReq payment = new PaymentReq();
        payment.setUsername(paymentReqDto.getEmployee());
        payment.setPeriod(paymentReqDto.getPeriod());
        payment.setSalary(paymentReqDto.getSalary());

        return payment;
    }

    @Override
    public PaymentRespDto mapToPaymentRespDto(PaymentResp paymentResp) {
        PaymentRespDto paymentRespDto = new PaymentRespDto();
        paymentRespDto.setName(paymentResp.getName());
        paymentRespDto.setLastname(paymentResp.getLastname());
        paymentRespDto.setPeriod(paymentResp.getPeriod());
        paymentRespDto.setSalary(paymentResp.getSalary());

        return paymentRespDto;
    }

    @Override
    public EventDto mapToEventDto(Event event) {
        EventDto eventDto = new EventDto();
        eventDto.setId(event.getId());
        eventDto.setDate(event.getDate().toString());
        eventDto.setAction(event.getAction().name());
        eventDto.setSubject(event.getSubject());
        eventDto.setObject(event.getObject());
        eventDto.setPath(event.getPath());

        return eventDto;
    }
}
