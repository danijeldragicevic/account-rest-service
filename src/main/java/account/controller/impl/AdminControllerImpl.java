package account.controller.impl;

import account.controller.IAdminController;
import account.enums.LockOperation;
import account.enums.Role;
import account.exception.*;
import account.mapper.IModelMapper;
import account.model.Event;
import account.model.User;
import account.model.dto.LockReqDto;
import account.model.dto.UserDto;
import account.model.dto.UserRoleDto;
import account.service.IAuthService;
import account.service.IEventService;
import account.utils.IEventCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class AdminControllerImpl implements IAdminController {
    private static final int ONE = 1;
    private final IModelMapper mapper;
    private final IAuthService authService;
    private final IEventCreator eventCreator;
    private final IEventService eventService;

    @Override
    public ResponseEntity<List<UserDto>> getUsers() {
        List<User> users = authService.getAllUsers();
        List<UserDto> userDtos = new ArrayList<>();
        for (User user: users) {
            userDtos.add(mapper.mapToUserDto(user, user.getRoles()));
        }

        return ResponseEntity.ok(userDtos);
    }

    @Override
    public ResponseEntity<Map<String, String>> deleteUser(UserDetails userDetails, String email, HttpServletRequest request) throws UserNotFoundException, CanNotRemoveAdminRoleException {
        Optional<User> user = authService.findUserByEmail(email);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        if (user.get().getRoles().contains(Role.ROLE_ADMINISTRATOR)) {
            throw new CanNotRemoveAdminRoleException();
        }
        authService.deleteUserByEmail(email);

        Event event = eventCreator.makeDeleteUserEvent(userDetails.getUsername(), user.get().getUsername(), request.getServletPath());
        eventService.saveEvent(event);

        return ResponseEntity.ok(Map.of(
                "user", email,
                "status", "Deleted successfully!"
        ));
    }

    @Override
    public ResponseEntity<UserDto> putRole(UserDetails userDetails, UserRoleDto roleDto, HttpServletRequest request) throws UserNotFoundException, RoleNotFoundException,
            UserDoesNotHaveThatRoleException, CanNotRemoveAdminRoleException, UserCanNotCombineBusinessAndAdminRolesException, UserMustHaveAtLeastOneRoleException {

        Optional<User> user = authService.findUserByEmail(roleDto.getUser());
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        if (!ObjectUtils.containsConstant(Role.values(), roleDto.getRole())) {
            throw new RoleNotFoundException();
        }
        switch (roleDto.getOperation()) {
            case "REMOVE": removeRoleFromUser(userDetails.getUsername(), user.get(), roleDto.getRole(), request.getServletPath());
                break;
            case "GRANT": addRoleToTheUser(userDetails.getUsername(), user.get(), roleDto.getRole(), request.getServletPath());
                break;
        }
        authService.saveUser(user.get());
        UserDto userDto = mapper.mapToUserDto(user.get(), user.get().getRoles());

        return ResponseEntity.ok(userDto);
    }

    @Override
    public ResponseEntity<Map<String, String>> lockUser(UserDetails userDetails, LockReqDto lockReqDto, HttpServletRequest request) throws UserNotFoundException, CanNotLockAdministratorException {
        Optional<User> user = authService.findUserByEmail(lockReqDto.getEmail());
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        if (user.get().getRoles().contains(Role.ROLE_ADMINISTRATOR) && lockReqDto.getOperation().equals(LockOperation.LOCK.name())) {
            throw new CanNotLockAdministratorException();
        }

        //TODO: Implement, when user is locked, it should get HttpStatus.FORBIDDEN for all resources it has rights before, until it's unlocked again.
        user.get().setLockOperation(LockOperation.valueOf(lockReqDto.getOperation()));
        authService.saveUser(user.get());

        Event event;
        if (lockReqDto.getOperation().equals(LockOperation.LOCK.name())) {
            event = eventCreator.makeLockUserEvent(userDetails.getUsername(), lockReqDto.getEmail(), request.getServletPath());
        } else {
            event = eventCreator.makeUnlockUserEvent(userDetails.getUsername(), lockReqDto.getEmail(), request.getServletPath());
        }
        eventService.saveEvent(event);

        String respMsg = "User " + user.get().getUsername() + " is " + user.get().getLockOperation().toString().toLowerCase() + "ed!";

        return ResponseEntity.ok(Map.of("status", respMsg));
    }

    private void removeRoleFromUser(String username, User user, String role, String path) throws UserDoesNotHaveThatRoleException, CanNotRemoveAdminRoleException, UserMustHaveAtLeastOneRoleException {
        Set<Role> userRoles = user.getRoles();
        if (!userRoles.contains(Role.valueOf(role))) {
            throw new UserDoesNotHaveThatRoleException();
        }
        if (Role.ROLE_ADMINISTRATOR.name().equals(role)) {
            throw new CanNotRemoveAdminRoleException();
        }
        if (userRoles.size() == ONE) {
            throw new UserMustHaveAtLeastOneRoleException();
        }

        userRoles.remove(Role.valueOf(role));
        user.setRoles(userRoles);

        Event event = eventCreator.makeRemoveRoleEvent(username, role, user.getUsername(), path);
        eventService.saveEvent(event);
    }

    private void addRoleToTheUser(String username, User user, String role, String path) throws UserCanNotCombineBusinessAndAdminRolesException {
        Set<Role> userRoles = user.getRoles();

        boolean isBusinessUser = userRoles.contains(Role.ROLE_USER) || userRoles.contains(Role.ROLE_ACCOUNTANT) || userRoles.contains(Role.ROLE_AUDITOR);
        if (isBusinessUser && role.equals(Role.ROLE_ADMINISTRATOR.name())) {
            throw new UserCanNotCombineBusinessAndAdminRolesException();
        }

        boolean isAdministrativeUser = userRoles.contains(Role.ROLE_ADMINISTRATOR);
        if (isAdministrativeUser && (role.equals(Role.ROLE_USER.name()) || role.equals(Role.ROLE_ACCOUNTANT.name()) || role.equals(Role.ROLE_AUDITOR.name()))) {
            throw new UserCanNotCombineBusinessAndAdminRolesException();
        }

        userRoles.add(Role.valueOf(role));
        user.setRoles(userRoles);

        Event event = eventCreator.makeGrantRoleEvent(username, role, user.getUsername(), path);
        eventService.saveEvent(event);
    }
}
