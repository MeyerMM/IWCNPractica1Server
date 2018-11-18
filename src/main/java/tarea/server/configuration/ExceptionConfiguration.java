package tarea.server.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionConfiguration {
    private Logger logger = LoggerFactory.getLogger(ExceptionConfiguration.class);

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> nullPointerException(HttpServletRequest request, NullPointerException exception){
        logger.debug("Null pointer exception in request to: " + request.getRequestURI());
        return new ResponseEntity<Object>(new Object(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> unhandledException(HttpServletRequest request, NullPointerException exception){
        logger.debug("Unhandled exception in request to: " + request.getRequestURI());
        return new ResponseEntity<Object>(new Object(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
