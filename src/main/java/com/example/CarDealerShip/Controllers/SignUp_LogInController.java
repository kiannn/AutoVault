package com.example.CarDealerShip.Controllers;

import com.example.CarDealerShip.Models.Credentials;
import com.example.CarDealerShip.Models.OwnerSignUpDTO;
import com.example.CarDealerShip.Services.OwnerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

@Controller
@RequestMapping
@SessionAttributes({"authorizedUser"})
public class SignUp_LogInController {

    @Autowired
    OwnerService OwnerService;
    
    @Autowired
    @Qualifier("userClient")
    WebClient WebClient;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/loginpage")
    public String loginView(ModelMap mm, HttpServletRequest req) {

        mm.addAttribute("xxx", "");
        
        String header = req.getHeader("X-Forwarded-For");
        String remoteAddr = req.getRemoteAddr();
        
        log.info("\nX-Forwarded-For = {}", header);
        log.info("remote = {}", req.getRemoteAddr());
        log.info("User = {}",req.getRemoteUser());

        try {
            ResponseEntity<Map> block = WebClient.get()
                    .uri(r -> r.path(header != null ? header.split(",")[0] : remoteAddr)
                    .build())
                    .retrieve()
                    .toEntity(Map.class)
                    .block();

            log.info("USER = {}", block.getBody());

        } catch (WebClientException wce) {
            log.warn("\n {}", wce.getMessage());

        }
        log.info("sec-ch-ua-mobile: {}",req.getHeader("sec-ch-ua-mobile")); 
        log.info("sec-ch-ua-platform: {}",req.getHeader("sec-ch-ua-platform"));
        
        return "login";
    }

    @GetMapping("/pre-logout")
    public String logOut() {

        return "pre-logout";
    }

    @GetMapping("/signup")
    public String signUpPage(ModelMap mm, HttpServletRequest req) {
        
        log.info(" *** " + req.getHeader("Referer"));
        
        if (req.getHeader("Referer") == null) {
            return "redirect:loginpage";
        }
        OwnerSignUpDTO build = OwnerSignUpDTO.builder()
                                    .firstName(null)
                                    .lastName(null)
                                    .dob(null)
                                    .email(null)
                                    .username(null)
                                    .credentials(Credentials.builder()
                                            .password(null)
                                            .confirmPassword(null)
                                            .build())
                                    .build();

        mm.addAttribute("signupForm", build);

        return "signup";

    }

    @PostMapping("/signup")
    public String signUpProcess(@Valid @ModelAttribute("signupForm") OwnerSignUpDTO Owner, BindingResult br, ModelMap ModelMap) {
        
        final String username = Owner.getUsername();
       
        boolean userExists = OwnerService.userExists(username);
        boolean emailUniqueness = OwnerService.emailUniquenessForSignUp(Owner);

        if (!emailUniqueness) {
            br.rejectValue("email", "emailBadFormat", "Someone already has this email address. Try another name");

        }

        if (userExists) {
            br.rejectValue("username", "repeatedUsername", "Username '" + username + "' already exists");
        }

        if (!br.hasErrors() && !userExists) {

            OwnerService.createNewUSer(Owner);
            return "redirect:loginpage?sigUpSuccess";
        }

        ModelMap.addAttribute("sigUpErrorGeneralMsg", "Sign up unsuccessful !");

        return "signup";
    }

}
