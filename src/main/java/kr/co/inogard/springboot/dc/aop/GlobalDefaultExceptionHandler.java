package kr.co.inogard.springboot.dc.aop;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
class GlobalDefaultExceptionHandler {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);
	
    public static final String DEFAULT_ERROR_VIEW = "/error/error";

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {

    	LOGGER.error("defaultErrorHandler", e);
    	
    	// Rethrow annotated exceptions or they will be processed here instead.
    	if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
    		throw e;
    	
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        mav.addObject("timestamp", new Date().toString());
        mav.setViewName(DEFAULT_ERROR_VIEW);
        return mav;
    }
}