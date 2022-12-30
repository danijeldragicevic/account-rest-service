package account.controller;

import account.exception.*;
import account.model.dto.LockReqDto;
import account.model.dto.UserDto;
import account.model.dto.UserRoleDto;
import account.validator.ValidationSequence;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/admin")
public interface IAdminController {
    @GetMapping("/user")
    ResponseEntity<List<UserDto>> getUsers();

    @DeleteMapping("/user/{email}")
    ResponseEntity<Map<String, String>> deleteUser(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String email, HttpServletRequest request)
            throws UserNotFoundException, CanNotRemoveAdminRoleException;

    @PutMapping("/user/role")
    ResponseEntity<UserDto> putRole(@AuthenticationPrincipal UserDetails userDetails, @RequestBody @Validated(ValidationSequence.class) UserRoleDto roleDto, HttpServletRequest request)
            throws UserNotFoundException, RoleNotFoundException, UserDoesNotHaveThatRoleException, CanNotRemoveAdminRoleException,
            UserCanNotCombineBusinessAndAdminRolesException, UserMustHaveAtLeastOneRoleException;

    @PutMapping("/user/access")
    ResponseEntity<Map<String, String>> lockUser(@AuthenticationPrincipal UserDetails userDetails, @RequestBody @Validated(ValidationSequence.class) LockReqDto lockReqDto, HttpServletRequest request)
            throws UserNotFoundException, CanNotLockAdministratorException;
}
