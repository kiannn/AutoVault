package com.example.CarDealerShip.Controllers;

import com.example.CarDealerShip.Models.CarMakeModelDTO;
import com.example.CarDealerShip.Models.CarSearchDTO;
import com.example.CarDealerShip.Models.CarStatsDTO;
import com.example.CarDealerShip.Models.CarWithDocsDTO;
import com.example.CarDealerShip.Models.MakeAndModel;
import com.example.CarDealerShip.Models.Transmissions;
import com.example.CarDealerShip.Services.CarServices;
import com.example.CarDealerShip.Services.OwnerService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cars")
@SessionAttributes({"showAll", "searchUserInput", "carFormData","carFormDocs","authorizedUser","listOfAcceptedfileExts","listOfProperties"})
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

//        String name = (String) mm.getAttribute("authorizedUser");
//        OwnerService.userSessionValidity(name);

        return "redirect:" + referer.replace("false", "true");
    }

    @GetMapping({"allcars/preshownav", "showsearchresult/pre"})
    public String preShowNav(ModelMap mm, HttpServletRequest req) {

        mm.addAttribute("searchUserInput", ""); 
        String ret = req.getRequestURI().endsWith("showsearchresult/pre") ?"redirect:show":"redirect:/cars/home/showallcars";

        return ret;
    } 

    @GetMapping("allcars/addpage")
    public String showAddPage(ModelMap mm, CarWithDocsDTO car, @RequestParam boolean carFormstate) {

//        String name = (String) mm.getAttribute("authorizedUser");
        // OwnerService.userSessionValidity(name);
         
        mm.addAttribute("message", "Add a New Car");
        mm.addAttribute("button", "Add");
        mm.addAttribute("availablePowerTrain", Transmissions.values());
        if (carFormstate) {
            mm.addAttribute("carFormData", car);
        }
        
        referer = "/cars/home/showallcars";
        
        populateDropDownList(mm);
        return "showAddOrUpdateFormPage";
    }
 
    @GetMapping("allcars/updatepage")
    public String showUpdatePage(@RequestParam int id, ModelMap mm, 
                                 @RequestParam boolean carFormstate, HttpServletRequest rqst) throws AccessDeniedException {
  
        String name = (String) mm.getAttribute("authorizedUser");
        System.out.println("\n-------------------> showUpdatePage()= "+name+"\n");
        // OwnerService.userSessionValidity(name);
          
        boolean validCarIds = CarService.isValidCarIds(name, new Integer[]{id});
        if (!validCarIds) { 
            throw new AccessDeniedException("Not Authorized !");
        }
         
        referer = rqst.getHeader("Referer");
       
        boolean badRefer = referer == null || (!referer.contains("notInDropDownListPage")
                                            && !referer.contains("addToDropDownList") // At the URL /cars/allcars/notInDropDownListPage, the user enters mismatched values for make and model and submits the form. This redirects them to http://localhost:8080/cars/allcars/addToDropDownList. If the user then clicks the Cancel button, the referrer becomes http://localhost:8080/cars/allcars/addToDropDownList
                                            && !referer.contains("home")
                                            && !referer.contains("showsearchresult")
                                            && !referer.contains("updatepage")
                                            && !referer.contains("document"));
                               
        if (badRefer) {
            throw new AccessDeniedException("Direct Access Denied !");
        }

        List<CarWithDocsDTO> get = CarService.findCarById(id);
        CarWithDocsDTO first = get.getFirst();
        if (carFormstate) { 
            mm.addAttribute("carFormData", first);
            mm.addAttribute("carFormDocs", get);
        }

        String msg = "Edit details for car id = %d, Make = %s, Model = %s";
        mm.addAttribute("message",String.format(msg,id, first.getMake(), first.getModel().isEmpty() ? "unkown" : first.getModel()));
        mm.addAttribute("button", "Update");
        mm.addAttribute("availablePowerTrain", Transmissions.values());
        refererAddUpdatePage = referer.contains("home") || referer.contains("showsearchresult")||referer.contains("document")? referer : refererAddUpdatePage;
        referer = refererAddUpdatePage;

        populateDropDownList(mm);
        return "showAddOrUpdateFormPage";
    }

    @PostMapping({"allcars/addpage", "allcars/updatepage"})
    public String addOrUpdateCars(@Valid @ModelAttribute("carFormData") CarWithDocsDTO carDTO,
                                                            BindingResult br,
                                                            MultipartFile[] filee, ModelMap mm, RedirectAttributes rediAtt, HttpServletRequest r)
                                                            throws IOException {

        if (!carDTO.getModel().isEmpty()) {

            List<String> verifyMakeAndModel = CarService.verifyMakeAndModelValidity(carDTO.getMake(), carDTO.getModel());

            if (verifyMakeAndModel.size() == 1) {

                br.rejectValue("model", "invalidModel", carDTO.getModel() + " does not match vehicle make");
            }
        }
        
        if (br.hasErrors()) {

            List<FieldError> collect = br.getFieldErrors()
                                         .stream()
                                         .filter(a -> !a.getDefaultMessage().contains("Failed to convert property value"))
                                         .collect(Collectors.toList());

            BindingResult tempBindingResult = new BeanPropertyBindingResult(carDTO, "--"); 
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
                    tempBindingResult.addError(new FieldError("??", fe.getField(), fe.getRejectedValue(), true, new String[]{"typeMismatch"}, null, defMsg));
                }
            }
            mm.replace("org.springframework.validation.BindingResult.carFormData", tempBindingResult);

            Integer itemNo = carDTO.getItemNo();
            
            if (r.getParameter("act") != null) {

                mm.addAttribute("message", "Add a New Car");
                mm.addAttribute("button", "Add");
            } else {

                CarMakeModelDTO get = CarService.findCar_MakeAndModel_ById(itemNo);

                String msg = "Edit details for car id = %d, Make = %s, Model = %s";
 
                mm.addAttribute("message", String.format(msg, itemNo, get.getMake(), get.getModel().isEmpty() ? "unkown" : get.getModel()));
                mm.addAttribute("button", "Update");
            }

            populateDropDownList(mm);

            return "showAddOrUpdateFormPage";
        }

        String name = (String) mm.getAttribute("authorizedUser");

        Integer id = CarService.add_Update(carDTO, name, filee) ;

        String format = String.format("Car Id: %d  has successfully %s", id, carDTO.getItemNo() != null ? "updated" : "added");

        rediAtt.addFlashAttribute("addOrEditMsg", format);
        return "redirect:/cars/home/showallcars";
    }


    @GetMapping("allcars/notInDropDownListPage")
    public ModelAndView showNotInDropDownListPage(ModelMap mm, CarMakeModelDTO car, HttpServletRequest req) throws AccessDeniedException {
         
//        String name = (String) mm.getAttribute("authorizedUser");
        // OwnerService.userSessionValidity(name);
        
        referer = req.getHeader("Referer"); 
     
        boolean badRefer = referer == null || (!referer.contains("addpage")
                                                 && !referer.contains("updatepage"));
  
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
    public String addNewItemInDropDownList(@Valid @ModelAttribute("carFormData") CarWithDocsDTO car, BindingResult brc, //Resolved [org.springframework.web.HttpSessionRequiredException: Expected session attribute 'carForm']
                                           @Valid CarMakeModelDTO carL, BindingResult br, 
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

        boolean contId = referer.contains("id"); // Check if the request is reffered from Edit page so values of possible erroneous fields (price, horsePower, datePurchased) are brought back to their original ones

        CarStatsDTO get = null;
        
        if (contId) {
            get = CarService.findCar_Stats_ById(Integer.parseInt(referer.substring(referer.indexOf('=') + 1, referer.indexOf('&')))); 
        }

        if (brc.getFieldErrorCount("price") != 0) {

            car.setPrice(contId ? get.getPrice() : null);// When contId==false the request is referred from Add page.
        }
        if (brc.getFieldErrorCount("horsePower") != 0) {
            car.setHorsePower(contId ? get.getHp() : null);
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

//        String name = (String) mm.getAttribute("authorizedUser");
        // OwnerService.userSessionValidity(name);
 
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
         
        List<CarWithDocsDTO> cars = new ArrayList<>();
        queryBasedOnSearch.forEach(q -> cars.add((CarWithDocsDTO)q)); 
         
        String msg = "";

        msg =  actualSearchInput.getPrice() > actualSearchInput.getPriceTo() ?  msg.concat("invalid input for price : Price From > Price To-"):msg;
        msg =  actualSearchInput.getYear() > actualSearchInput.getYearTo() ? msg.concat("invalid input for year : Year From > Year To-"):msg;
        msg = actualSearchInput.getDatePurchased().isAfter(actualSearchInput.getDatePurchasedTo()) ? msg.concat("invalid input for date : Date From > Date To-" ): msg;

        mm.addAttribute("resultMsg", msg);
        Collection<List<CarWithDocsDTO>> values = CarService.arrangDataForView(cars); 
        mm.addAttribute("showAll", values);
        
        List<String[]> listOfProperties = (List<String[]>) mm.getAttribute("listOfProperties");
        
        mm.addAttribute("noValue", CarService.columnEntirelyHasNoValueSort(cars, listOfProperties));
        
        return "showSearchResultPage";
    }

    @GetMapping("allcars/deletepage/{id}")
    public String deleteCars(@PathVariable Integer[] id, ModelMap mm, 
                              RedirectAttributes redAtt, HttpServletRequest r) throws AccessDeniedException {
 
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

        redAtt.addFlashAttribute("addOrEditMsg", "Car Id: " + Arrays.toString(id).substring(1, Arrays.toString(id).length() - 1) + " has successfully deleted");
        return "redirect:/cars/home/showallcars";

    }

    @GetMapping({"home/{by}", "showsearchresult/{by}"})
    public String orderBy(ModelMap mm, @PathVariable String by, HttpServletRequest request) throws AccessDeniedException {
 
//        String name = (String) mm.getAttribute("authorizedUser");
        // OwnerService.userSessionValidity(name);

        String refer = request.getHeader("Referer");

        boolean badRefer = refer == null || (!refer.contains("showsearchresult")&& 
                                             !refer.contains("home") && 
                                             !refer.contains("updatepage")); // !refer.contains("updatepage") -> in case after user orders recordes (either in Home or Search page), user tries to edite a record but then presses the 'cancel' button in the update page (showAddOrUpdateFormPage), hence the refferer becomes the update page URL
        
         if (badRefer) {
            throw new AccessDeniedException("Invalid Data Or Direct Access Denied !");
        }
         
        if (CarService.numberOfRecors() == 0) {

            return refer.contains("home") ? "showListingCarsPage" : "showSearchResultPage";
        }

        Collection<List<CarWithDocsDTO>> listOFCars = (Collection<List<CarWithDocsDTO>>) mm.getAttribute("showAll");
        List<CarWithDocsDTO> listOFCarsToBeSorted = new ArrayList<>();
        listOFCars.forEach(c -> listOFCarsToBeSorted.addAll(c));
        List<CarWithDocsDTO> findAllOrderby = CarService.getCarsSortBy(listOFCarsToBeSorted, by);
        
        Collection<List<CarWithDocsDTO>> arrangDataForView = CarService.arrangDataForView(findAllOrderby);
        
        List<String[]> listOfProperties = (List<String[]>) mm.getAttribute("listOfProperties");
        mm.addAttribute("sortMsg", "Sorted by " + by + (!by.contains("-") ? "-ascend" : ""));
        mm.addAttribute("noValue", CarService.columnEntirelyHasNoValueSort(findAllOrderby, listOfProperties));
        mm.addAttribute("showAll", arrangDataForView);
 
        if (refer.contains("home")) {
            return "showListingCarsPage";
        }
        if (refer.contains("updatepage")) {
            return request.getRequestURL().toString().contains("home") ? "showListingCarsPage" : "showSearchResultPage";
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
         * Using Resource interface :
         *
         * The 'new File("src/main/resources/...")' approach looks for the file
         * in the filesystem relative to the application’s working directory.
         * When application is packaged into a JAR/WAR, the src/main/resources
         * directory does not exist as a filesystem path inside that JAR/WAR
         * (file.exists() returns false).After packaging, resources under
         * src/main/resources are bundled inside the JAR/WAR and
         * "src/main/resources" directory itself no longer exists but its
         * contents can still be accessed using classpath. 'classpath:' is a
         * keyword or prefix used to specify that the resource should be loaded
         * from the application's classpath. It tells Spring to look for the
         * resource inside the directories or archives (packaged files like
         * .jar/.war that contain compiled code and resources) that are included
         * in the classpath (e.g., src/main/resources, or inside the JAR/WAR).
         * The classpath is a list of locations the JVM uses to find compiled
         * classes and resources at runtime. classpath includes directories and
         * archives, so both can be part of the classpath. An archive means a
         * packaged file like a .jar or .war. Directories are plain folders on
         * your filesystem that contain compiled .class or resource files.
         * Examples are target/classes/ (When you run your program from an IDE,
         * it automatically adds or updates target/classes folder inside the project
         * directory), src/main/resources/ (at development time, before
         * packaging — automatically added to classpath by IDEs). When you run
         * application inside NetBeans, then: files from src/main/java → all
         * compiled into .class files → outputs go to target/classes and files from
         * src/main/resources → all copied to target/classes. After packaging
         * the application into a JAR/WAR the src/main/resources directory no
         * longer exists on the filesystem inside that JAR/WAR. Its contents are
         * embedded inside the JAR/WAR. The JAR/WAR itself becomes the classpath
         * entry — Java looks inside it when loading classes/resources.
         * 'Resource' interface can be used to load files from both local
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
