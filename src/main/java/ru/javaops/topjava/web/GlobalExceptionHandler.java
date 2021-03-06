package ru.javaops.topjava.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.javaops.topjava.util.ValidationUtil;
import ru.javaops.topjava.util.exeption.ApplicationException;
import ru.javaops.topjava.util.exeption.ErrorType;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javaops.topjava.util.ValidationUtil.logAndGetRootCause;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = getLogger(GlobalExceptionHandler.class);

    private final MessageUtil messageUtil;

    @Autowired
    public GlobalExceptionHandler(MessageUtil messageUtil) {
        this.messageUtil = messageUtil;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView wrongRequest(HttpServletRequest request, NoHandlerFoundException e) {
        return logAndGetExceptionView(request, e, false, ErrorType.WRONG_REQUEST, null);
    }

    @ExceptionHandler(ApplicationException.class)
    public ModelAndView applicationErrorHandler(HttpServletRequest request, ApplicationException e) {
        return logAndGetExceptionView(request, e, true, e.getType(), messageUtil.getMessage(e));
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest request, Exception e) {
        log.error("Exception at request " + request.getRequestURL(), e);
        return logAndGetExceptionView(request, e, true, ErrorType.APP_ERROR, null);
    }

    private ModelAndView logAndGetExceptionView(HttpServletRequest request, Exception e, boolean logException,
                                                ErrorType errorType, String message) {
        Throwable rootCause = logAndGetRootCause(log, request, e, logException, errorType);
        var httpStatus = errorType.getStatus();

        var modelAndView = new ModelAndView("exception",
                Map.of("exception", rootCause,
                        "message", message != null ? message : ValidationUtil.getMessage(rootCause),
                        "typeMessage", messageUtil.getMessage(errorType.getErrorCode()),
                        "status", httpStatus)
        );
        modelAndView.setStatus(httpStatus);
        return modelAndView;
    }
}
