package com.example.CarDealerShip.Controllers;

import com.example.CarDealerShip.Models.Credentials;
import com.example.CarDealerShip.Models.Owner;
import com.example.CarDealerShip.Services.OwnerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.Map;
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

@Controller
@RequestMapping
@SessionAttributes({"authorizedUser"})
public class SignUp_LogInController {

    @Autowired
    OwnerService OwnerService;
    
    @Autowired
    @Qualifier("userClient")
    WebClient WebClient;

    @GetMapping("/loginpage")
    public String loginView(ModelMap mm, HttpServletRequest req) {

        mm.addAttribute("xxx", "");
        
        String header = req.getHeader("X-Forwarded-For");
        String remoteAddr = req.getRemoteAddr();
        System.out.println("\nX-Forwarded-For = "+header);
        System.out.println("remote = "+req.getRemoteAddr());
        System.out.println("User = "+req.getRemoteUser());
      
        ResponseEntity<Map> block = WebClient.get()
                .uri(r -> r.path(header!=null ? header.split(",")[0] : remoteAddr)
                .build())
                .retrieve()
                .toEntity(Map.class)
                .block();
        
        System.out.println("USER =\n"+block.getBody());
        System.out.println("sec-ch-ua-mobile: "+req.getHeader("sec-ch-ua-mobile")); 
        System.out.println("sec-ch-ua-platform: "+req.getHeader("sec-ch-ua-platform"));
        
        return "login";
    }

    @GetMapping("/pre-logout")
    public String logOut() {

        return "pre-logout";
    }

    @GetMapping("/signup")
    public String signUpPage(ModelMap mm, HttpServletRequest req) {
        System.out.println(" *** " + req.getHeader("Referer"));
        if (req.getHeader("Referer") == null) {
            return "redirect:loginpage";
        }
        Owner build = Owner.builder()
                .firstName(null)
                .lastName(null)
                .dob(null)
                .email(null)
                .credentials(Credentials.builder()
                        .password(null)
                        .confirmPassword(null)
                        .build())
                .build();

        mm.addAttribute("signupForm", build);

        return "signup";

    }

    @PostMapping("/signup")
    public String signUpProcess(@Valid @ModelAttribute("signupForm") Owner Owner, BindingResult br, ModelMap ModelMap) {

        boolean userExists = OwnerService.userExists(Owner.getUsername());
        boolean emailUniqueness = OwnerService.emailUniquenessForSignUp(Owner);

        if (!emailUniqueness) {
            br.rejectValue("email", "XXX", "Someone already has this email address. Try another name");

        }

        if (userExists) {
            br.rejectValue("username", "XXX", "Username '" + Owner.getUsername() + "' already exists");
        }

        if (!br.hasErrors() && !userExists) {

            OwnerService.createNewUSer(Owner);
            return "redirect:loginpage?sigUpSuccess";
        }

        ModelMap.addAttribute("sigUpErrorGeneralMsg", "Sign up unsuccessful !");

        return "signup";
    }

}
