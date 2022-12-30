package account.controller;

import account.exception.BreachedPasswordException;
import account.exception.IdenticalPasswordException;
import account.exception.UserAlreadyExistException;
import account.model.dto.ChangePassDto;
import account.model.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequestMapping("/api/auth")
public interface IAuthController {
    @PostMapping("/signup")
    ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto userDto, HttpServletRequest request)
            throws UserAlreadyExistException, BreachedPasswordException;

    @PostMapping("/changepass")
    ResponseEntity<ChangePassDto> changePass(@AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid ChangePassDto changePassDto, HttpServletRequest request)
            throws BreachedPasswordException, IdenticalPasswordException;
}
