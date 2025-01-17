package com.example.CarDealerShip.Controllers;

import com.example.CarDealerShip.Models.Owner;
import com.example.CarDealerShip.Models.PasswordDTO;
import com.example.CarDealerShip.Services.OwnerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping
@SessionAttributes({"authorizedUser"})
public class AccountController {

    @Autowired
    OwnerService OwnerService;

    @GetMapping("/userpro")
    public String showProfile(ModelMap mp) {

        String attribute = (String) mp.getAttribute("authorizedUser");
        Owner findUserById = OwnerService.findUserById(attribute);
        mp.addAttribute("personalInfo", findUserById);

        return "personalInfo";

    }

    @PostMapping("/userpro")
    public String updateProfile(ModelMap mp, @Valid @ModelAttribute("personalInfo") Owner owner, BindingResult br) {
       
        String name = (String) mp.getAttribute("authorizedUser");
        OwnerService.userSessionValidity(name);

        BindingResult brNew = new BeanPropertyBindingResult(owner, "++");
        List<FieldError> collect = br.getFieldErrors().stream().filter(e -> !e.getField().equals("username")).collect(Collectors.toList());

        collect.forEach(e -> brNew.addError(e));
        mp.replace("org.springframework.validation.BindingResult.personalInfo", brNew);

        String loggedInUser = (String) mp.getAttribute("authorizedUser");

        boolean emailUniqueness = OwnerService.emailUniquenessForUpdate(owner, loggedInUser);

        if (!emailUniqueness) {
            brNew.rejectValue("email", "XXX", "Repetitive email address! Try another one");

        }

        if (brNew.hasErrors()) {
            mp.addAttribute("personalInfoUpdateMSG", "User Information Update Unsuccessful");
            return "personalInfo";
        }
        OwnerService.updatePersonalInfo(loggedInUser, owner);

        mp.addAttribute("personalInfoUpdateMSG", "User information updated successfully");

        return "personalInfo";

    }

    @GetMapping("/userpass")
    public String showPasswordPage(ModelMap mp, @ModelAttribute("passObj") PasswordDTO passDto) {

        return "password";
    }

    @PostMapping("/userpass")
    public String updatePasswordPage(ModelMap mp, @Valid @ModelAttribute("passObj") PasswordDTO passDto, BindingResult br) {

        String username = (String) mp.getAttribute("authorizedUser");
        OwnerService.userSessionValidity(username);

        boolean passwordMatch = OwnerService.verifyPasswordCorrectness(passDto, username);

        if (!passwordMatch) {
            br.rejectValue("currentPass", "InvalidPassword !", "Password invalid");
        }

        if (br.hasErrors()) {
            mp.addAttribute("passwordUpdateMSG", "Password Updated Unsuccessful");
            return "password";
        }
        OwnerService.updateUserPassword(username, passDto);
        mp.addAttribute("passwordUpdateMSG", "Password Update Successfully");
        return "password";

    }

    @GetMapping("/deletaccount")
    public String deleteAccountPage(ModelMap mp, @ModelAttribute("deletpage") PasswordDTO passwordDTO) {

        String name = (String) mp.getAttribute("authorizedUser");
        OwnerService.userSessionValidity(name);
        mp.addAttribute("showPassF", false);

        return "deleteAccountPage";

    }

    @PostMapping("/deletaccount")
    public String deleteAccount(ModelMap mp, @Valid @ModelAttribute("deletpage") PasswordDTO passwordDTO, BindingResult br, HttpServletRequest HttpServletRequest) {

        String attribute = (String) mp.getAttribute("authorizedUser");
        OwnerService.userSessionValidity(attribute);

        boolean passwordMatch = OwnerService.verifyPasswordCorrectness(passwordDTO, attribute);
        if (!passwordMatch) {
            br.rejectValue("currentPass", "NoMatch", "Password invalid");
        }
        System.out.println("com.example.CarDealerShip.Controllers.AccountController.deleteAccount()\n"+br);
        if (br.hasErrors()) {

            mp.addAttribute("deleteAccountMSG", "Delete Account Unsuccessful");
            mp.addAttribute("showPassF", true);
            return "deleteAccountPage";
        }
        HttpSession session = HttpServletRequest.getSession(false);
        System.out.println("session = ? " + session.getId());
        session.getAttributeNames().asIterator().forEachRemaining(a -> System.out.println("a = " + a));
        session.invalidate();

        OwnerService.deleteAccount(attribute);

        return "redirect:loginpage?deleteAccountMSG";

    }
}
