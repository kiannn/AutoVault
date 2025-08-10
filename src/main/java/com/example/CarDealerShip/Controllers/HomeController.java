
package com.example.CarDealerShip.Controllers;

import com.example.CarDealerShip.Models.Car;
import com.example.CarDealerShip.Models.CarWithDocsDTO;
import com.example.CarDealerShip.Models.FileExtension;
import com.example.CarDealerShip.Services.CarServices;
import com.example.CarDealerShip.Services.OwnerService;
import java.util.Collection;
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
@SessionAttributes({"authorizedUser", "listOfProperties", "listOfAcceptedfileExts","showAll"})
public class HomeController {

    @Autowired
    private CarServices CarServices;
    
    @Autowired
    OwnerService OwnerService;
    
    @GetMapping("/showallcars")
    public String homePage(ModelMap mm) {

        String name = (String) mm.getAttribute("authorizedUser");
        OwnerService.userSessionValidity(name);

        List<CarWithDocsDTO> findAll = CarServices.getAllCars(name);
 
        Collection<List<CarWithDocsDTO>> values = CarServices.arrangDataForView(findAll);
        mm.addAttribute("showAll", values);
        
        List<String[]> listOfProperties = (List<String[]>) mm.getAttribute("listOfProperties");
        
        mm.addAttribute("noValue", CarServices.columnEntirelyHasNoValueSort(findAll, listOfProperties));
        
        System.out.println("com.example.CarDealerShip.Controllers.HomeController.homePage()");
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
                                             new String[]{"make","0"},
                                             new String[]{"model","1"}, 
                                             new String[]{"year","2"},
                                             new String[]{"datePurchased","3"},
                                             new String[]{"price","4"},
                                             new String[]{"powerTrain","5"},
                                             new String[]{"condn","6"},
                                             new String[]{"horsePower","7"}
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
