package account.controller.impl;

import account.configuration.BeansConfig;
import account.controller.IAuthController;
import account.enums.Role;
import account.exception.BreachedPasswordException;
import account.exception.IdenticalPasswordException;
import account.exception.UserAlreadyExistException;
import account.mapper.IModelMapper;
import account.model.Event;
import account.model.User;
import account.model.dto.ChangePassDto;
import account.model.dto.UserDto;
import account.service.IAuthService;
import account.service.IEventService;
import account.utils.IEventCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements IAuthController {
    private final BeansConfig config;
    private final IAuthService authService;
    private final IEventService eventService;
    private final IEventCreator eventCreator;
    private final IModelMapper mapper;

    @Override
    public ResponseEntity<UserDto> createUser(UserDto userDto, HttpServletRequest request)
            throws UserAlreadyExistException, BreachedPasswordException {

        if (authService.findUserByEmail(userDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistException();
        }
        if (authService.getBreachedPasswords().contains(userDto.getPassword())) {
            throw new BreachedPasswordException();
        }

        Role role;
        if (authService.getAllUsers().isEmpty()) {
            role = Role.ROLE_ADMINISTRATOR;
        } else {
            role = Role.ROLE_USER;
        }

        User newUser = mapper.mapToUserModel(userDto, Set.of(role));
        authService.saveUser(newUser);

        Event event = eventCreator.makeCreateUserEvent(newUser.getUsername(), request.getServletPath());
        eventService.saveEvent(event);

        UserDto newUserDto = mapper.mapToUserDto(newUser, Set.of(role));
        return ResponseEntity.ok(newUserDto);
    }


    @Override
    public ResponseEntity<ChangePassDto> changePass(UserDetails userDetails, ChangePassDto passDto, HttpServletRequest request)
            throws BreachedPasswordException, IdenticalPasswordException {

        if (authService.getBreachedPasswords().contains(passDto.getPassword())) {
            throw new BreachedPasswordException();
        }
        if (config.getEncoder().matches(passDto.getPassword(), userDetails.getPassword())) {
            throw new IdenticalPasswordException();
        }

        Optional<User> user = authService.findUserByEmail(userDetails.getUsername());
        user.get().setPassword(config.getEncoder().encode(passDto.getPassword()));
        authService.saveUser(user.get());

        Event event = eventCreator.makeChangePassEvent(userDetails.getUsername(), userDetails.getUsername(), request.getServletPath());
        eventService.saveEvent(event);

        ChangePassDto changePassDto = new ChangePassDto();
        changePassDto.setEmail(userDetails.getUsername());
        changePassDto.setStatus("The password has been updated successfully");

        return ResponseEntity.ok(changePassDto);
    }
}
