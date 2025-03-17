
package com.example.CarDealerShip.Controllers;

import com.example.CarDealerShip.Models.Car;
import com.example.CarDealerShip.Models.FileExtension;
import com.example.CarDealerShip.Services.CarServices;
import com.example.CarDealerShip.Services.OwnerService;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/cars/home")
@SessionAttributes({"authorizedUser", "listOfProperties", "listOfAcceptedfileExts"})
public class HomeController {

    @Autowired
    private CarServices CarServices;
    
    @Autowired
    OwnerService OwnerService;
    
    @GetMapping("/showallcars")
    public String homePage(ModelMap mm) {//, @RequestParam(required = false) Boolean findall) {

        String name = (String) mm.getAttribute("authorizedUser");
        OwnerService.userSessionValidity(name);

        List<Car> findAll = CarServices.getAllCars(name);

        mm.addAttribute("showAll", findAll);

        mm.addAttribute("noValue", CarServices.columnEntirelyHasNoValueSort(findAll));

        return "showListingCarsPage";

    }
 
    @ModelAttribute("authorizedUser")
    public String getAuthorizedUser(Authentication Authentication) {
        String name = Authentication.getName();

        return name;
    }
    
    @ModelAttribute("listOfProperties")
    public List<String[]> listOfProperties(){
    
     List<String[]>listOfProperties = List.of(new String[]{"itemNo"},
                                             new String[]{"make"},
                                             new String[]{"model","0"}, 
                                             new String[]{"year","1"},
                                             new String[]{"datePurchased","2"},
                                             new String[]{"price","3"},
                                             new String[]{"powerTrain","4"},
                                             new String[]{"condn","5"},
                                             new String[]{"horsePower","6"}
                                           );
    
        return listOfProperties;
    }

    @ModelAttribute("listOfAcceptedfileExts")
    public List<String> listOfAcceptedfileExts() {

        EnumSet<FileExtension> range = EnumSet.range(FileExtension.PDF, FileExtension.AVI);
        List<String> collect = range.stream().map(ext -> ext.getEx()).collect(Collectors.toList());

        return collect;

    }
}
