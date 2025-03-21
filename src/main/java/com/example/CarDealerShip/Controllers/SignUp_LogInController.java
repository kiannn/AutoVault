package com.example.CarDealerShip.Controllers;

import com.example.CarDealerShip.Models.Credentials;
import com.example.CarDealerShip.Models.Owner;
import com.example.CarDealerShip.Services.OwnerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Arrays;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping
@SessionAttributes({"authorizedUser"})
public class SignUp_LogInController {

    @Autowired
    OwnerService OwnerService;

    @GetMapping("/loginpage")
    public String loginView(ModelMap mm, HttpServletRequest req) {

        mm.addAttribute("xxx", "");
        System.out.println("com.example.CarDealerShip.Controllers.OwnerController.loginView()");
        req.getParameterMap().forEach((k, v) -> System.out.println(k + " -> " + Arrays.toString(v)));
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
