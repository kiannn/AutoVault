package com.example.CarDealerShip.Controllers;

import com.example.CarDealerShip.Models.CarWithDocsDTO;
import com.example.CarDealerShip.Models.DocumentDTO;
import com.example.CarDealerShip.Models.Documents;
import com.example.CarDealerShip.Models.FileExtension;
import com.example.CarDealerShip.Services.CarServices;
import com.example.CarDealerShip.Services.DocumentService;
import com.example.CarDealerShip.Services.OwnerService;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/document")
@SessionAttributes({"authorizedUser","carFormData","carFormDocs", "docSearchInput", "showAll","listOfProperties"})
public class DocumentController {

    @Autowired
    DocumentService DocumentService;
    
    @Autowired 
    CarServices CarServices;
    
    @Autowired
    OwnerService OwnerService;
 
    @GetMapping("/download") 
    public ResponseEntity<byte[]> viewDocument(Integer id, ModelMap ModelMap, HttpServletRequest r) throws AccessDeniedException {

       String name = (String) ModelMap.getAttribute("authorizedUser");

       String referer = r.getHeader("Referer");

       boolean reject = referer==null || (!referer.contains("home") && 
                                           !referer.contains("updatepage")&& 
                                           !referer.contains("showsearchresult")&& 
                                           !referer.contains("document"));
       
       boolean validFileId = DocumentService.isValidDocumentId(name, id);
  
        if (validFileId && !reject) {

            Documents documnet = DocumentService.getDocumnetById(id);
            
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+documnet.getName());
            /**
             *
             * Some hosting platforms, including Railway might detect file types
             * like .docx, .xlsx, .pptx, and route requests to external services
             * (such as Microsoft Office Online at view.officeapps.live.com) to
             * enable file previews. When deployed, The URL
             * https://view.officeapps.live.com/ is triggered before accessing
             * the actual endpoint of your application when trying to download a
             * file. Instead of downloading the file directly, the browser may
             * integrate with Microsoft Office Online (via
             * https://view.officeapps.live.com/) to provide document preview.
             * When a .docx file is requested for download, the browser might
             * redirect the request through the Office Online viewer to display
             * the document before downloading it. The browser uses your
             * endpoint URL as a parameter for Microsoft's viewer. How to Fix
             * This: Ensure the HTTP response header Content-Disposition is set
             * to attachment to enforce the file to be treated as a direct
             * download, not a preview.
             */
            ResponseEntity<byte[]> response = ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.valueOf(documnet.getType()))
                    .headers(headers) 
                    .body(documnet.getData());

            return response;
        }

        throw new AccessDeniedException("Document Access Denied !");

    }

    /**
     * @ModelAttribute("carFormData")carWithDocsDTO_List car, BindingResult br,
     *
     * There is a scenario where a user updates a car record and, before
     * deleting a document, modifies some form field values. For example, the
     * user may enter new values in various fields and then click the delete
     * button to remove a document. In this case, we want the newly entered
     * values to be displayed after the document is deleted and the update page
     * is returned again. Since the form-backing object for the update/add form
     * is named 'carFormData' in the model, we include
     * '@ModelAttribute("carFormData") carWithDocsDTO_List car' in the
     * 'deleteDocument' method parameters. This ensures that when the delete
     * request is submitted (a POST request to 'deleteDocument'), the car object
     * (which represents the model attribute object of the update/add form) is
     * populated with the latest user-entered values. Because these values may
     * contain validation errors, a BindingResult br is also declared
     * immediately after the '@ModelAttribute("carFormData") carWithDocsDTO_List
     * car' parameter.
     */
    @PostMapping("/edite/{id}/delete") 
    public String deleteDocument(@PathVariable Integer id, //@ModelAttribute("carFormData")carWithDocsDTO_List car, BindingResult br, 
                                 ModelMap ModelMap, RedirectAttributes rediAtt, HttpServletRequest req) {

            String referer = req.getHeader("Referer");
            
            String documnet = DocumentService.getDocumnetById(id).getName();
            DocumentService.deletSingleDocument(id);
            
            List<CarWithDocsDTO> carWithDocsDTO_List = (List<CarWithDocsDTO>) ModelMap.getAttribute("carFormDocs");
            carWithDocsDTO_List.forEach( c -> System.out.println("DocumentController.deleteDocument() CarWithDocsDTO="+c+"\n"));
            System.out.println("CarWithDocsDTO.size() = "+carWithDocsDTO_List.size()+"\n------------------");
            
        /**
         * if (carWithDocsDTO_List.size() == 1) {
         *      carWithDocsDTO_List.getFirst().setDocId(null);
         *   }
         * In form.jspf at at line 141 we have:
         * <c:if test="${carFormDocs.getFirst().docId == null}">. The method
         * getFirst() throws a NoSuchElementException if the carWithDocsDTO_List
         * collection is empty. To prevent this exception in form.jsp, when
         * 'carWithDocsDTO_List.size() == 1', we should not remove the
         * CarWithDocsDTO element with the matching docId (docId=id). Removing
         * it would leave the carWithDocsDTO_List collection empty and cause
         * getFirst() to fail. Instead, we retain the single element in the
         * collection and set its docId to null.
         */
            if (carWithDocsDTO_List.size() == 1) {
                carWithDocsDTO_List.getFirst().setDocId(null);
                System.out.println("\nCarWithDocsDTO.size() == 1\n" + carWithDocsDTO_List + "\n" + carWithDocsDTO_List.size() + "\n");
            } else {
                for (CarWithDocsDTO c : carWithDocsDTO_List) {
                    System.out.println("\nCarWithDocsDTO=" + c + "\n" + carWithDocsDTO_List.size() + "\n");
                    if (Objects.equals(c.getDocId(), id)) {
                        //                    c.setDocId(null);
                        System.out.println(">>>>> before remove >>>>>");
                        carWithDocsDTO_List.remove(c);
                        break;
                    }
                }
            }
//                carWithDocsDTO_List.stream().forEach(c -> {
//                    System.out.println("\nCarWithDocsDTO=" + c + "\n" + carWithDocsDTO_List.size() + "\n");
//                    if (Objects.equals(c.getDocId(), id)) {
////                    c.setDocId(null);
//                        System.out.println(">>>>> before remove >>>>>");
//                        carWithDocsDTO_List.remove(c);
//
//                    }
//                });
//            carWithDocsDTO_List.removeIf( c -> carWithDocsDTO_List.size()>1 && Objects.equals(c.getDocId(), null));
//            List<Documents> docs =DocumentService.getDocumnetsByCar(car.getItemNo());
//            car.setDocs(docs); // update form-backing obj (modelAttribute="carFormData"), which is used to display docs in the update page, after delete 
            
            referer = referer.replace("true", "false");
            rediAtt.addFlashAttribute( "docdelet","document "+documnet+" deleted successfully");
                        
            return "redirect:"+referer;

    }

    @GetMapping("/searchdocs")
    public String searchDocument(ModelMap mp, DocumentDTO DocumentDTO) {

//        String name = (String) mp.getAttribute("authorizedUser");
        // OwnerService.userSessionValidity(name);

        mp.addAttribute("docSearchInput", DocumentDTO);
        mp.addAttribute("allExten", EnumSet.range(FileExtension.PDF, FileExtension.AVI));
        mp.addAttribute("any", FileExtension.ANY);

        return "documnetSearchPage";
    }
 
    @PostMapping("/searchdocs")
    public String peformSearchDocument(ModelMap mp,@ModelAttribute("docSearchInput") DocumentDTO DocumentDTO) {
   
        String name = DocumentDTO.getName();
        FileExtension extension = DocumentDTO.getExtension();
         
        mp.addAttribute("allExten", EnumSet.range(FileExtension.PDF, FileExtension.AVI));
        mp.addAttribute("any", FileExtension.ANY);
 
        if (name.isEmpty() && (extension == null || extension==FileExtension.ANY)) {
            return "documnetSearchPage";
        }
        String isIsSensitive = DocumentDTO.getCaseSensitive();
        
        String username = (String) mp.getAttribute("authorizedUser");
           
        List<CarWithDocsDTO> searchForDocumentResult = DocumentService.searchForDocument(name, extension,isIsSensitive ,username);

        Collection<List<CarWithDocsDTO>> values = CarServices.arrangDataForView(searchForDocumentResult); 
          
        mp.addAttribute("showAll", values);
        List<String[]> listOfProperties = (List<String[]>) mp.getAttribute("listOfProperties");
        mp.addAttribute("noValue", CarServices.columnEntirelyHasNoValueSort(searchForDocumentResult, listOfProperties)); 
        mp.addAttribute("showTable", "");
 
        return "documnetSearchPage";
    }

    @GetMapping("/{by}")
    public String peformSearchDocumentOrderBy(ModelMap mp, @PathVariable String by, HttpServletRequest request) throws AccessDeniedException {

//        String name = (String) mp.getAttribute("authorizedUser");
//        OwnerService.userSessionValidity(name); 

        String refer = request.getHeader("Referer");

        boolean badRefer = refer == null || (!refer.contains("document")&&!refer.contains("updatepage"));
        
        if (badRefer) {
            throw new AccessDeniedException("Requested Resources Access Denied");
        }
        
        if (CarServices.numberOfRecors() == 0) {
            return "showSearchResultPage";
        }
        
        Collection<List<CarWithDocsDTO>> listOFCars = (Collection<List<CarWithDocsDTO>>) mp.getAttribute("showAll");
        List<CarWithDocsDTO> listOFCarsToBeSorted = new ArrayList<>();
        listOFCars.forEach(c -> listOFCarsToBeSorted.addAll(c));
        
        List<CarWithDocsDTO> findAllOrderby = CarServices.getCarsSortBy(listOFCarsToBeSorted, by);
        
        Collection<List<CarWithDocsDTO>> values = CarServices.arrangDataForView(findAllOrderby); 

        mp.addAttribute("sortMsg", "Sorted by " + by + (!by.contains("-") ? "-ascend" : ""));
        mp.addAttribute("showAll", values);
        
        List<String[]> listOfProperties = (List<String[]>) mp.getAttribute("listOfProperties");
        
        mp.addAttribute("noValue", CarServices.columnEntirelyHasNoValueSort(findAllOrderby, listOfProperties)); 
        mp.addAttribute("allExten", EnumSet.range(FileExtension.PDF, FileExtension.ICS));
        mp.addAttribute("any", FileExtension.ANY);
        mp.addAttribute("showTable", "");
        
        return "documnetSearchPage";
    }
    
}
 