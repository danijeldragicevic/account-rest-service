package account.exception;

import account.model.Event;
import account.service.IEventService;
import account.utils.IEventCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@ControllerAdvice
@RequiredArgsConstructor
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler implements AccessDeniedHandler {
    private final IEventCreator eventCreator;
    private final IEventService eventService;
    private Map<String, Integer> failedLogins = new HashMap<>();

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ApiError error = initBadRequestErrorObj(request);

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        if (fieldErrors.size() > 1) {
            Map<String, String> fieldErrorsSorted = new TreeMap<>();
            for (FieldError e: fieldErrors) {
                fieldErrorsSorted.put(e.getField().replace("list", "payments"), e.getDefaultMessage());
            }

            String errorMessage = "";
            for (Map.Entry<String, String> entry: fieldErrorsSorted.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                errorMessage += key + ": " + value + ", ";
            }

            error.setMessage(errorMessage.substring(0, errorMessage.length()-2));
        } else {
            error.setMessage(ex.getBindingResult().getFieldError().getDefaultMessage());
        }

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String authorization = request.getHeader("Authorization");
        String username = new String(Base64.getDecoder().decode(authorization.split("\\s+")[1])).split(":")[0];
        Event event = eventCreator.makeAccessDeniedEvent(username, request.getServletPath(), request.getServletPath());
        eventService.saveEvent(event);

        ApiError error = initForbiddenErrorObj(request);
        error.setMessage("Access Denied!");
        response.setStatus(error.getStatus()); //HttpStatus.FORBIDDEN

        response.getOutputStream().println(new ObjectMapper().writeValueAsString(error));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(WebRequest request) {
        String authorization = request.getHeader("Authorization");
        String username = new String(Base64.getDecoder().decode(authorization.split("\\s+")[1])).split(":")[0];
        String path = (((ServletWebRequest)request).getRequest().getRequestURI());

        if (failedLogins.containsKey(username)) {
            int attempts = failedLogins.get(username);
            failedLogins.put(username, ++attempts);
        } else {
            failedLogins.put(username, 1);
        }

        List<Event> events = new ArrayList<>();
        events.add(eventCreator.makeLoginFailedEvent(username, path, path));
        if (failedLogins.get(username) >= 3) {
            events.add(eventCreator.makeBruteForceEvent(username, path, path));
            events.add(eventCreator.makeLockUserEvent(username, username, path));
            failedLogins.remove(username);
        }
        eventService.saveAllEvents(events);

        ApiError error = initUnauthErrorObj(request);
        error.setMessage("Username or password are not correct!");

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    protected ResponseEntity<Object> handleUserExistException(WebRequest request) {
        ApiError error = initBadRequestErrorObj(request);
        error.setMessage("User exist!");

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BreachedPasswordException.class)
    public ResponseEntity<Object> handleBreachedPasswordException(WebRequest request) {
        ApiError error = initBadRequestErrorObj(request);
        error.setMessage("The password is in the hacker's database!");

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IdenticalPasswordException.class)
    public ResponseEntity<Object> handleIdenticalPasswordException(WebRequest request) {
        ApiError error = initBadRequestErrorObj(request);
        error.setMessage("The passwords must be different!");

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicatePaymentEntryException.class)
    public ResponseEntity<Object> handleDuplicatePaymentEntryException(WebRequest request) {
        ApiError error = initBadRequestErrorObj(request);
        error.setMessage("Duplicate payment entry!");

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<Object> handlePaymentNotFoundException(WebRequest request) {
        ApiError error = initBadRequestErrorObj(request);
        error.setMessage("Payment does not exists!");

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(WebRequest request) {
        ApiError error = initBadRequestErrorObj(request);
        error.setMessage("Error!");

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CanNotRemoveAdminRoleException.class)
    public ResponseEntity<Object> handleCanNotRemoveAdministratorException(WebRequest request) {
        ApiError error = initBadRequestErrorObj(request);
        error.setMessage("Can't remove ADMINISTRATOR role!");

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserMustHaveAtLeastOneRoleException.class)
    public ResponseEntity<Object> handleUserMustHaveOneRoleException(WebRequest request) {
        ApiError error = initBadRequestErrorObj(request);
        error.setMessage("The user must have at least one role!");

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserCanNotCombineBusinessAndAdminRolesException.class)
    public ResponseEntity<Object> handleUserCanNotCombineBusinessAndAdminRolesException(WebRequest request) {
        ApiError error = initBadRequestErrorObj(request);
        error.setMessage("The user cannot combine administrative and business roles!");

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserDoesNotHaveThatRoleException.class)
    public ResponseEntity<Object> handleUserDoesNotHaveRoleException(WebRequest request) {
        ApiError error = initBadRequestErrorObj(request);
        error.setMessage("The user does not have a role!");

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CanNotLockAdministratorException.class)
    public ResponseEntity<Object> handleCanNotLockAdministratorException(WebRequest request) {
        ApiError error = initBadRequestErrorObj(request);
        error.setMessage("Can't lock the ADMINISTRATOR!");

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(WebRequest request) {
        ApiError error = initNotFoundErrorObj(request);
        error.setMessage("User not found!");

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Object> handleRoleNotFoundException(WebRequest request) {
        ApiError error = initNotFoundErrorObj(request);
        error.setMessage("Role not found!");

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    private ApiError initBadRequestErrorObj(WebRequest request) {
        ApiError error = new ApiError();
        error.setTimestamp(String.valueOf(LocalDate.now()));
        error.setStatus(400);
        error.setError("Bad Request");
        error.setPath(((ServletWebRequest)request).getRequest().getRequestURI());

        return error;
    }

    private ApiError initUnauthErrorObj(WebRequest request) {
        ApiError error = new ApiError();
        error.setTimestamp(String.valueOf(LocalDate.now()));
        error.setStatus(401);
        error.setError("Unauthorized");
        error.setPath(((ServletWebRequest)request).getRequest().getRequestURI());

        return error;
    }

    private ApiError initForbiddenErrorObj(HttpServletRequest request) {
        ApiError error = new ApiError();
        error.setTimestamp(String.valueOf(LocalDate.now()));
        error.setStatus(403);
        error.setError("Forbidden");
        error.setPath(request.getServletPath());

        return error;
    }

    private ApiError initNotFoundErrorObj(WebRequest request) {
        ApiError error = new ApiError();
        error.setTimestamp(String.valueOf(LocalDate.now()));
        error.setStatus(404);
        error.setError("Not Found");
        error.setPath(((ServletWebRequest)request).getRequest().getRequestURI());

        return error;
    }
}
