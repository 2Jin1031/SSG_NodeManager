package ssg.nodemanager.global;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ssg.nodemanager.exception.NotLoggedInException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotLoggedInException.class)
    public ModelAndView handleNotLoggedIn() {
        return new ModelAndView("redirect:/login");
    }
}