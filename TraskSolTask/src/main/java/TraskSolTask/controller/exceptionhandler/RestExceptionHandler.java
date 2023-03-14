package TraskSolTask.controller.exceptionhandler;


import TraskSolTask.exceptions.UserAccessException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import TraskSolTask.exceptions.NotFoundException;
import TraskSolTask.exceptions.ValidationException;
import TraskSolTask.payload.response.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.List;


/**
 * Exception handlers for REST controllers.
 * <p>
 * The general pattern should be that unless an exception can be handled in a more appropriate place it bubbles up to a
 * REST controller which originally received the request. There, it is caught by this handler, logged and a reasonable
 * error message is returned to the user.
 */
@ControllerAdvice
public class RestExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RestExceptionHandler.class);

    private static void logException(Throwable ex) {
        LOG.error("Exception caught: {}", ex.getMessage());
    }

    private static MessageResponse messageResponse(HttpServletRequest request, Throwable e) {
        if (e.getCause() != null) {
            return new MessageResponse(e.getMessage(), e.getCause().toString(), request.getRequestURI());
        }
        return new MessageResponse(e.getMessage(), request.getRequestURI());
    }

    private static MessageResponse messageResponse(HttpServletRequest request, String message) {
        return new MessageResponse(message, request.getRequestURI());
    }

    private String msg;


    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<MessageResponse> handlePersistenceException(HttpServletRequest request,
                                                                      PersistenceException e) {
        logException(e);
        return new ResponseEntity<>(messageResponse(request, e), HttpStatus.INTERNAL_SERVER_ERROR);
    }




    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<MessageResponse> handleNotFoundException(HttpServletRequest request, NotFoundException e) {
        // Not necessary to log NotFoundException, they may be quite frequent and do not represent an issue
        // with the application
        return new ResponseEntity<>(messageResponse(request, e), HttpStatus.NOT_FOUND);
    }



    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<MessageResponse> handleConstraintViolation(HttpServletRequest request,
                                                                     ConstraintViolationException e) {
        logException(e);
        return new ResponseEntity<>(messageResponse(request, e), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<MessageResponse> handleBadRequestFromClient(HttpServletRequest request,
                                                                      HttpClientErrorException e) {
        // Not necessary to log HttpClientErrorException, they may be quite frequent and do not represent an issue
        // with the application
        return new ResponseEntity<>(messageResponse(request, e), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponse> handleMethodArgumentNotValidException(HttpServletRequest request,
                                                                                 MethodArgumentNotValidException e) {
        // Not necessary to log MethodArgumentNotValidException, they may be quite frequent and do not represent an
        // issue with the application
        BindingResult result = e.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        msg = "";
        fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).forEach(m -> msg = msg + m + " ");
        return new ResponseEntity<>(messageResponse(request,msg), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<MessageResponse> handleMethodArgumentTypeMismatch(HttpServletRequest request,
                                                                            MethodArgumentTypeMismatchException e) {
        // Not necessary to log MethodArgumentTypeMismatchException, they may be quite frequent and do not represent an
        // issue with the application
        return new ResponseEntity<>(messageResponse(request, e), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MismatchedInputException.class)
    public ResponseEntity<MessageResponse> handleMismatchedInputException(HttpServletRequest request,
                                                                          MismatchedInputException e) {
        // Not necessary to log MismatchedInputException, they may be quite frequent and do not represent an
        // issue with the application
        return new ResponseEntity<>(messageResponse(request, e), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<MessageResponse> handleValidationException(HttpServletRequest request,
                                                                     ValidationException e) {
        logException(e);
        return new ResponseEntity<>(messageResponse(request, e), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<MessageResponse> handleNullPointerException(HttpServletRequest request,
                                                                      NullPointerException e) {
        logException(e);
        return new ResponseEntity<>(messageResponse(request, e), HttpStatus.CONFLICT);
    }


    @ExceptionHandler(UserAccessException.class)
    public ResponseEntity<MessageResponse> systemUserAccessException(HttpServletRequest request,
                                                                   UserAccessException e) {
        logException(e);
        return new ResponseEntity<>(messageResponse(request, e), HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<MessageResponse> handleAccessDeniedException(HttpServletRequest request,
                                                                       AccessDeniedException e) {
        logException(e);
        return new ResponseEntity<>(messageResponse(request, e), HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MessageResponse> handleIllegalArgumentException(HttpServletRequest request,
                                                                     IllegalArgumentException e) {
        logException(e);
        return new ResponseEntity<>(messageResponse(request, e), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<MessageResponse> handleOtherExceptions(HttpServletRequest request, JsonMappingException e) {
        logException(e);
        return new ResponseEntity<>(messageResponse(request, e), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}