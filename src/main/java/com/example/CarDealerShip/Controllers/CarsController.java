package com.example.CarDealerShip.Controllers;

import com.example.CarDealerShip.Models.Car;
import com.example.CarDealerShip.Models.CarSearchDTO;
import com.example.CarDealerShip.Models.MakeAndModel;
import com.example.CarDealerShip.Models.Transmissions;
import com.example.CarDealerShip.Services.CarServices;
import com.example.CarDealerShip.Services.OwnerService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
//import jakarta.validation.constraints.Pattern;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import java.util.regex.Pattern;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

@Controller
@RequestMapping("/cars") 
@SessionAttributes({"searchQuary", "searchUserInput", "carForm","authorizedUser","listOfProperties","listOfAcceptedfileExts"})
public class CarsController {
    
    @Autowired
    CarServices CarService;
    
    @Autowired
    OwnerService OwnerService;

    @Autowired
    private ResourceLoader resourceLoader;
    
    int [] listofYears ;
    double[] listofPrices ;
    private String referer;         
    private String refererAddUpdatePage;
    private List<String> rowData;

    @GetMapping("allcars/preshow")
    public String preShow(ModelMap mm, HttpServletRequest req) {
        
        String name = (String) mm.getAttribute("authorizedUser");
        OwnerService.userSessionValidity(name);
        
        return "redirect:" + referer.replace("false", "true");
    }  

    @GetMapping({"allcars/preshownav", "showsearchresult/pre"})
    public String preShowNav(ModelMap mm, HttpServletRequest req) {

        mm.addAttribute("searchUserInput", ""); 

        System.out.println(req.getRequestURI());
        System.out.println(req.getHeader("Referer"));
        String ret = req.getRequestURI().endsWith("showsearchresult/pre") ?"redirect:show":"redirect:/cars/home/showallcars";

        return ret;
    } 

    @GetMapping("allcars/addpage")
    public String showAddPage(ModelMap mm, Car car, @RequestParam boolean carFormstate) {

        String name = (String) mm.getAttribute("authorizedUser");
        OwnerService.userSessionValidity(name);
         
        mm.addAttribute("message", "Add a New Car");
        mm.addAttribute("button", "Add");
        mm.addAttribute("availablePowerTrain", Transmissions.values());
        if (carFormstate) {
            mm.addAttribute("carForm", car);
        }
        
        referer = "/cars/home/showallcars";
        
        populateDropDownList(mm);
        return "showAddOrUpdateFormPage";
    }
 
    @GetMapping("allcars/updatepage")
    public String showUpdatePage(@RequestParam int id, ModelMap mm, 
                                 @RequestParam boolean carFormstate, HttpServletRequest rqst) throws AccessDeniedException {
  
        String name = (String) mm.getAttribute("authorizedUser");
        System.out.println("com.example.CarDealerShip.Controllers.CarsController.showUpdatePage() = "+ name);
 
        OwnerService.userSessionValidity(name);
         
        boolean validCarIds = CarService.isValidCarIds(name, new Integer[]{id});
        if (!validCarIds) { 
            throw new AccessDeniedException("Not Authorized !");
        }
         
        referer = rqst.getHeader("Referer");
        
        boolean badRefer = referer == null || (!referer.contains("notInDropDownListPage")
                                            && !referer.contains("home")
                                            && !referer.contains("showsearchresult")
                                            && !referer.contains("updatepage")
                                            && !referer.contains("document"));
                           
        if (badRefer) {
            throw new AccessDeniedException("Direct Access Denied !");
        }

        referer = referer.contains("?addOrEditMsg")
                ? referer.substring(0, referer.indexOf("addOrEditMsg") - 1) : referer;

        Car get = CarService.findCarById(id).get();
        
        if (carFormstate) { 
            mm.addAttribute("carForm", get);
        }

        String msg = "Edit details for car id = %d, Make = %s, Model = %s";
        mm.addAttribute("message",String.format(msg,id, get.getMake(), get.getModel().isEmpty() ? "unkown" : get.getModel()));
        mm.addAttribute("button", "Update");
        mm.addAttribute("availablePowerTrain", Transmissions.values());
        refererAddUpdatePage = referer.contains("home") || referer.contains("showsearchresult")||referer.contains("document")? referer : refererAddUpdatePage;
        referer = refererAddUpdatePage;

        populateDropDownList(mm);
        return "showAddOrUpdateFormPage";
    }

    @PostMapping({"allcars/addpage", "allcars/updatepage"})
    public String addOrUpdateCars(@Valid @ModelAttribute("carForm") Car car,
            BindingResult br,
            MultipartFile[] filee, ModelMap mm, HttpServletRequest r)
            throws IOException {

        if (!car.getModel().isEmpty()) {
            
            List<String> verifyMakeAndModel = CarService.verifyMakeAndModelValidity(car.getMake(), car.getModel());

            if (verifyMakeAndModel.size() == 1) {

                br.rejectValue("model", "invalidModel", car.getModel() + " does not match vehicle make");
            }
        }
        
        if (br.hasErrors()) {

            List<FieldError> collect = br.getFieldErrors().stream()
                    .filter(a -> !a.getDefaultMessage().contains("Failed to convert property value")).collect(Collectors.toList());

            BindingResult tempBindingResult = new BeanPropertyBindingResult(car, "--"); 
            collect.forEach(e -> tempBindingResult.addError(e));

            List<String> erroneousProperties = List.of("datePurchased", "horsePower", "price");

            String defMsg = null;

            for (FieldError fe : br.getFieldErrors()) {

                if (erroneousProperties.contains(fe.getField()) && fe.getDefaultMessage().contains("Failed to convert property value")) {

                    switch (fe.getField()) {
                        case "datePurchased" ->
                            defMsg = "Invalid Purchase Date, must be from 1000-Jan-01";
                        case "horsePower" ->
                            defMsg = "Invalid Horse Power, failed to convert value";
                        case "price" ->
                            defMsg = "Invalid Price, not a valid number";
                    }

                    br.rejectValue(fe.getField(), "typeMismatch", defMsg);
                    tempBindingResult.addError(new FieldError("??", fe.getField(), fe.getRejectedValue(), true, new String[]{"typeMismatch"}, null, defMsg));
                }
            }
            mm.replace("org.springframework.validation.BindingResult.carForm", tempBindingResult);

            if (r.getParameter("act") != null) {

                mm.addAttribute("message", "Add a New Car");
                mm.addAttribute("button", "Add");
            } else {

                Car get = CarService.findCarById(car.getItemNo()).get();

                String msg = "Edit details for car id = %d, Make = %s, Model = %s";

                mm.addAttribute("docdeletAllowed", "");
                mm.addAttribute("message", String.format(msg, car.getItemNo(), get.getMake(), get.getModel().isEmpty() ? "unkown" : get.getModel()));
                mm.addAttribute("button", "Update");
            }

            populateDropDownList(mm);

            return "showAddOrUpdateFormPage";
        }

        Integer itenNo = car.getItemNo();

        String name = (String) mm.getAttribute("authorizedUser");
        System.out.println("---->file = " + Arrays.toString(filee) + "  " + (filee == null));

        Car Car = filee != null ? CarService.add_Update(car, name, filee) : 
                                  CarService.add_Update_NoFile(car, name);
        /*
        add_Update_NoFile(car, name);
          In the case of editing, the 'car' object might contain 5 files. Therefore, the edit form
          does not include any <input> tag with a 'type' attribute set to 'file,' resulting in the 'filee' object 
          being null.*/

        String format = String.format("Car Id: %d  has successfully %s", Car.getItemNo(), itenNo != null ? "updated" : "added");

        return "redirect:/cars/home/showallcars?addOrEditMsg=" + format;
    }


    @GetMapping("allcars/notInDropDownListPage")
    public ModelAndView showNotInDropDownListPage(ModelMap mm, Car car, HttpServletRequest req) throws AccessDeniedException {
         
        String name = (String) mm.getAttribute("authorizedUser");
        OwnerService.userSessionValidity(name);
        
        referer = req.getHeader("Referer"); 
        boolean badRefer = referer == null || (!referer.contains("addpage")
                                                 && !referer.contains("updatepage") 
                                                 && !referer.contains("document"));
 
        if (badRefer) {
            throw new AccessDeniedException("Direct Access Denied !");
        }
        ModelAndView mAv = new ModelAndView();

        mAv.setViewName("showNotInListFormPage");
        mAv.addObject("allPossibleMakes", rowData);
        
        if (referer.contains("docdelet")) {

            StringBuilder ref = new StringBuilder(referer);
            int indexOf = ref.indexOf("docdelet");
            int indexOf1 = ref.indexOf("&");
            ref.delete(indexOf, indexOf1 + 1);
            referer = ref.toString();
        }
 
        return mAv;
    }
   
    @PostMapping("allcars/addToDropDownList")
    public String addNewItemInDropDownList(@Valid @ModelAttribute("carForm") Car car, BindingResult brc, //Resolved [org.springframework.web.HttpSessionRequiredException: Expected session attribute 'carForm']
                                           @Valid Car carL, BindingResult br, 
                                           ModelMap mm) {

        if (!carL.getModel().isEmpty()) {
            
            List<String> verifyMakeAndModel = CarService.verifyMakeAndModelValidity(carL.getMake(), carL.getModel());

            if (verifyMakeAndModel.size() == 1) {
  
                br.rejectValue("model", "invalidModel", carL.getModel() + " does not match vehicle make");
            }
            if (br.hasErrors()) {
                mm.addAttribute("allPossibleMakes", rowData);
                mm.addAttribute("selectedItem", carL.getMake());

                return "showNotInListFormPage";
            }
            car.setModel(verifyMakeAndModel.get(1));
        }

        boolean contId = referer.contains("id");

        Car get = null;
        
        if (contId) {
            get = CarService.findCarById(Integer.parseInt(referer
                                                    .substring(referer
                                                    .indexOf('=') + 1, referer
                                                    .indexOf('&')))).get(); 
        }
        if (brc.getFieldErrorCount("price") != 0) {

            car.setPrice(contId ? get.getPrice() : null);
        }
        if (brc.getFieldErrorCount("horsePower") != 0) {
            car.setHorsePower(contId ? get.getHorsePower() : null);
        }
        if (brc.getFieldErrorCount("datePurchased") != 0) {

            car.setDatePurchased(contId ? get.getDatePurchased() : null);
        }
        
        String name = (String) mm.getAttribute("authorizedUser");
        List<MakeAndModel> availableMakesAndModels = OwnerService.availableMakesAndModels(name);
        
        CarService.adjustModelMakeList(availableMakesAndModels, car);
        
        OwnerService.updateMakeAndModelList(name, availableMakesAndModels);
        
        populateDropDownList(mm);

        return "redirect:".concat(referer.substring(0,  referer.lastIndexOf("=") + 1)).concat("false");
                                                   
    }
 
    @GetMapping("showsearchresult/show")
    public String showSearchPage(ModelMap mm, CarSearchDTO car, HttpServletRequest req) {

        String name = (String) mm.getAttribute("authorizedUser");
        OwnerService.userSessionValidity(name);
 
        req.getParameterMap().forEach((k, v) -> System.out.println("key = " + k + "  value = " + Arrays.toString(v)));

        populateDropDownList(mm);
        mm.addAttribute("availableprices", listofPrices);

        if (mm.getAttribute("searchUserInput") == "" || mm.getAttribute("searchUserInput") == null) {

            mm.addAttribute("searchUserInput", car);
        }

        mm.addAttribute("message", "Look up For a Car");
        mm.addAttribute("button", "Search");

        referer = "/cars/home/showallcars";

        return "showSearchForCarFormPage";
    }
  
    @PostMapping("showsearchresult/show")
    public String searchResults(ModelMap mm, @ModelAttribute("searchUserInput") CarSearchDTO carFrom) { // Resolved [org.springframework.web.HttpSessionRequiredException: Expected session attribute 'searchUserInput']
          
        String name = (String) mm.getAttribute("authorizedUser");
        List<Object> queryBasedOnSearch = CarService.searchForCars(carFrom, name); 
        CarSearchDTO actualSearchInput = (CarSearchDTO) queryBasedOnSearch.removeLast();
         
        List<Car> cars = new ArrayList<>();
        queryBasedOnSearch.forEach(q -> cars.add((Car)q)); 
                                           
        mm.addAttribute("searchQuary", actualSearchInput); // 'searchQuary' is needed when user performs sort cars

        String msg = "";

        msg =  actualSearchInput.getPrice() > actualSearchInput.getPriceTo() ?  msg.concat("invalid input for price : Price From > Price To-"):msg;
        msg =  actualSearchInput.getYear() > actualSearchInput.getYearTo() ? msg.concat("invalid input for year : Year From > Year To-"):msg;
        msg = actualSearchInput.getDatePurchased().isAfter(actualSearchInput.getDatePurchasedTo()) ? msg.concat("invalid input for date : Date From > Date To-" ): msg;

        mm.addAttribute("resultMsg", msg);
        mm.addAttribute("showAll", cars);
        mm.addAttribute("noValue", CarService.columnEntirelyHasNoValueSort(cars));
        
        return "showSearchResultPage";
    }

    @GetMapping("allcars/deletepage/{id}")
    public String deleteCars(@PathVariable Integer[] id, ModelMap mm, HttpServletRequest r) throws AccessDeniedException {
 
        String name = (String) mm.getAttribute("authorizedUser");
        boolean validFileId = CarService.isValidCarIds(name, id);
        if (!validFileId) {
            throw new AccessDeniedException("Not Authorized !");
        }
          
        String refer = r.getHeader("Referer");
        boolean badRefer = refer == null || (!refer.contains("showsearchresult")&& 
                                             !refer.contains("home") && 
                                             !refer.contains("document"));
                

        if (badRefer) {
            throw new AccessDeniedException("Direct Access Denied !");
        }

        CarService.deleteSelectedCars(Arrays.asList(id));

        return "redirect:/cars/home/showallcars?addOrEditMsg=Car Id: " + Arrays.toString(id).substring(1, Arrays.toString(id).length() - 1) + " has successfully deleted";

    }

    @GetMapping({"home/{by}", "showsearchresult/{by}"})
    public String orderBy(ModelMap mm, @PathVariable String by, HttpServletRequest request) throws AccessDeniedException {
 
        String name = (String) mm.getAttribute("authorizedUser");
        OwnerService.userSessionValidity(name);

        String refer = request.getHeader("Referer");

        boolean badRefer = refer == null || (!refer.contains("showsearchresult")&& 
                                             !refer.contains("home"));
        
         if (badRefer) {
            throw new AccessDeniedException("Invalid Data Or Direct Access Denied !");
        }
         
        if (CarService.numberOfRecors() == 0) {

            return refer.contains("home") ? "showListingCarsPage" : "showSearchResultPage";
        }

        if (refer.contains("home")) {

            List<Car> findAllOrderby = CarService.getAllCarsSortBy(name, by);

            List<Car> findAll = CarService.getAllCars(name);

            mm.addAttribute("noValue", CarService.columnEntirelyHasNoValueSort(findAll));
            mm.addAttribute("showAll", findAllOrderby);

            return "showListingCarsPage";
        }

        if (refer.contains("showsearchresult")) {

            CarSearchDTO q = (CarSearchDTO) mm.getAttribute("searchQuary");

            List<Car> searchForCarsOrderBy = CarService.searchForCarsOrderBy(q, by, name);

            mm.addAttribute("showAll", searchForCarsOrderBy);
            mm.addAttribute("noValue", CarService.columnEntirelyHasNoValueSort(searchForCarsOrderBy));

        }
        return "showSearchResultPage";
    }

    public void populateDropDownList(ModelMap mm) {

        String name = (String) mm.getAttribute("authorizedUser");

        List<MakeAndModel> availableMakesAndModels = OwnerService.availableMakesAndModels(name);
        List<MakeAndModel> makes = availableMakesAndModels.stream().filter(e -> !e.getAvailableModel().isEmpty()).toList();
        LinkedHashSet<MakeAndModel> models = new LinkedHashSet<>(availableMakesAndModels);

        mm.addAttribute("availableModels", makes);

        mm.addAttribute("availableMakes", models);

        mm.addAttribute("availableYears", listofYears);
    }

    @PostConstruct
    public void availableMakesAndModels() {
//C:\Users\kian1\OneDrive\Documents\NetBeansProjects\AutoVault-copy\src\main\java\com\example\CarDealerShip\Controllers\CarsController.java
        File file = new File("src/main/resources/Car Models List of Car Models.xlsx");
        System.out.println("\n POST "+file.exists()+"\n"+file);
        Resource resource = resourceLoader.getResource("classpath:Car Models List of Car Models.xlsx");
        /**
         *
         * Using Resource interface :
         *
         * The 'new File("src/main/resources/...")' approach looks for the file
         * in the filesystem relative to the application’s working directory.
         * When application is packaged into a JAR/WAR, the src/main/resources
         * directory does not exist as a filesystem path (file.exists() returns
         * false). The file will be placed inside the JAR/WAR and not on the
         * filesystem. Instead, directory contents are bundled inside the
         * JAR/WAR. After packaging, resources under src/main/resources are
         * included in the JAR/WAR and can still be accessed using classpath.
         * classpath: is a keyword or prefix used to specify that the resource
         * should be loaded from the application's classpath. It tells Spring to
         * look for the resource inside the directories or archives that are
         * included in the classpath (e.g., src/main/resources or inside the
         * JAR/WAR). Resource can be used to load files from both local
         * filesystem and when application deployed as a JAR/WAR.
         */
        try (InputStream fis = resource.getInputStream();) {

//        try (InputStream fis = new FileInputStream(file);
//             Workbook workbook = WorkbookFactory.create(fis)) {
            Workbook workbook = WorkbookFactory.create(fis);
            rowData = new ArrayList<>();

            Sheet sheet = workbook.getSheetAt(6);

            for (Row row : sheet) {

                Cell cell = row.getCell(1);

                switch (cell.getCellType()) {
                    case STRING ->
                        rowData.add(cell.getStringCellValue());
                }

            }

            rowData = rowData.stream().filter(car->Pattern.matches("[&a-yA-Y]+[-]?[&a-zA-Z]+", car)).collect(Collectors.toList());
            rowData.addAll(List.of("ASTON MARTIN",
                                    "ALFA ROMEO",
                                    "LAND ROVER",
                                    "ZENVO",
                                    "ZENOS", 
                                    "ZASTAVA", 
                                    "ZOTYE", 
                                    "ZIMMER", 
                                    "ZHONGXING", 
                                    "ZIBAR"));
            Collections.sort(rowData); 
            
        } catch (IOException e) {

        }

        listofYears = IntStream.iterate(LocalDate.now().getYear(), i -> i - 1).limit(42).toArray();

        listofPrices = DoubleStream.iterate(10000, i -> i + 10000).limit(20).toArray();

    }
}
