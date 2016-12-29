package interview.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason="Internal Server Error")
public class InternalException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InternalException() {
     super("Internal Server Error");
    }
}