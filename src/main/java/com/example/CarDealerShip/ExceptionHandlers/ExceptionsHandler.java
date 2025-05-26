package com.example.CarDealerShip.ExceptionHandlers;

import jakarta.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public String handleException1(Exception exc, Model m) {

        m.addAttribute("msg", "The page you are looking for does not exist !");

        return "errorPage";

    }

    @ExceptionHandler({NoSuchElementException.class, IllegalStateException.class})
    @ResponseStatus(code = HttpStatus.FOUND) 
    /** 
     * response status applied on @ExceptionHandler method (HttpStatus.REQUEST_TIMEOUT) should be compatible with
     * 'redirect' statement in the @ExceptionHandler method 'return', which results in a 302 Found. 
     * In case of using HttpStatus.REQUEST_TIMEOUT while IllegalStateException is being thrown 
     * the @ExceptionHandler method returns a Whitelabel Error Page
     * with a status code of type=Forbidden, status=403, and does NOT redirect. 
     * If application throws NoSuchElementException,
     * the "redirect:/loginpage?sessionTimedOut" in return statement is performed but the
     * query parameter 'sessionTimedOut' is not shown and ignored.
    */ 
    public String handleException2(Exception exc, Model m, HttpServletRequest h) {

        h.getSession(false).invalidate();
        System.out.println("com.example.CarDealerShip.ExceptionHandlers.ExceptionsHandler.handleException2()\n" + exc.getClass().getSimpleName()+"\n"+exc.getMessage());
        return "redirect:/loginpage?sessionTimedOut"; 
 
    }  
  
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public String handleException3(Exception exc, Model m) {

        m.addAttribute("msg", exc.getMessage());
        return "errorPage";
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class, MissingServletRequestParameterException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public String handleException4(Exception exc, Model m) {

        m.addAttribute("msg", "Request is not supported");
        return "errorPage";
    }
    
    @ExceptionHandler(WebClientResponseException.class)
    @ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE)
    public String handleException5(Exception exc, Model m) {

        m.addAttribute("msg", "Our vPIC API application is currently undergoing Maintenance," +
"                                         we are currently unable to complete your request.<br>Please try again at a later time");
        return "ServiceUnavailable";
    }
}
    /**
     *  java.lang.IllegalStateException: 
     *    Happens when application is restated and as such all session attributes of the app
     *    are gone.In this case make requests to a handler method having a @ModelAttribute parameter, 
     *    like @ModelAttribute("carForm"), gets :
     *    java.lang.IllegalStateException: Expected session attribute 'carForm'
     * 
     * org.springframework.web.HttpRequestMethodNotSupportedException: 
     *    Happens when a get request is made at a URL which is handled by a POST controller method,
     *    in such case we get : org.springframework.web.HttpRequestMethodNotSupportedException: Request method 'GET' is not supported
     * 
     * java.util.NoSuchElementException :
     *    Happens when methods like findById or findByID are invoked from within other methods 
     *    OwnerService.findUserById and  OwnerService.userSessionValidity respectively while there is no user records
     *    in the database because application re-started
     * 
     * java.nio.file.AccessDeniedException : 
     *    Happens when some controller methods like showUpdatePage, showNotInDropDownListPage, deleteCars, viewDocument
     *    throws this exception due to bad referer or invalid document id
     * 
     * org.springframework.web.servlet.resource.NoResourceFoundException: 
     *    Happens when an invalid url is executed, a url that has no endpoints in such case we get :
     *    org.springframework.web.servlet.resource.NoResourceFoundException: No static resource kir/home/kir.
     * 
     * org.springframework.web.reactive.function.client.WebClientResponseException :
     *   Happens when vPIC API application does not response (the API we make request at through WebClient) due to maintenance,
     *   it throws WebClientResponseException
     * 
     */