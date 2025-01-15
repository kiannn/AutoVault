
package com.example.CarDealerShip.Controllers;

import com.example.CarDealerShip.Models.Car;
import com.example.CarDealerShip.Models.DocumentDTO;
import com.example.CarDealerShip.Models.Documents;
import com.example.CarDealerShip.Models.FileExtension;
import com.example.CarDealerShip.Services.CarServices;
import com.example.CarDealerShip.Services.DocumentService;
import com.example.CarDealerShip.Services.OwnerService;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/document")
@SessionAttributes({"authorizedUser","carForm","docSearchInput"})
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
        System.out.println("+++++++++++++++++++viewDocument()\n"+referer);
       boolean reject = referer==null || (!referer.contains("home") && 
                                           !referer.contains("updatepage")&& 
                                           !referer.contains("showsearchresult")&& 
                                           !referer.contains("document/searchdocs"));
       
       boolean validFileId = DocumentService.isValidDocumentId(name, id);
  
        if (validFileId && !reject) {

            Documents documnet = DocumentService.getDocumnetById(id);
            
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+documnet.getName());
            /*
            Some hosting platforms, including Railway might detect file types like .docx, .xlsx, .pptx, and route 
            requests to external services (such as Microsoft Office Online at view.officeapps.live.com) to enable 
            file previews.
            When deployed, The URL https://view.officeapps.live.com/ is triggered before accessing the actual endpoint of your 
            application when trying to download a file. Instead of downloading the file directly, the browser may 
            integrate with Microsoft Office Online (via https://view.officeapps.live.com/) to provide document preview.
            When a .docx file is requested for download, the browser might redirect the request through the Office 
            Online viewer to display the document before downloading it.
            The browser uses your endpoint URL as a parameter for Microsoft's viewer.
            How to Fix This:
            Ensure the HTTP response header Content-Disposition is set to attachment to enforce the file to 
            be treated as a direct download, not a preview.
            */
            ResponseEntity<byte[]> response = ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.valueOf(documnet.getType()))
                    .headers(headers) 
                    .body(documnet.getData());

            return response;
        }

        throw new AccessDeniedException("Document Access Denied !");

    }

    @PostMapping("/edite/{id}/delete") 
    public String deleteDocument(@PathVariable Integer id, @ModelAttribute("carForm")Car car, 
                                 ModelMap ModelMap, HttpServletRequest req) {
     
            String referer = req.getHeader("Referer");
            
            String documnet = DocumentService.getDocumnetById(id).getName();
            DocumentService.deletSingleDocument(id);
            
            List<Documents> docs =DocumentService.getDocumnetsByCar(car.getItemNo());
            car.setDocs(docs); // update form-backing obj (modelAttribute="carForm"), which is used to display docs in the update page, after delete 
            
            referer = referer.substring(0, referer.indexOf("carFormstate"))+"carFormstate=false";
            int indexOf = referer.indexOf('?');
            
            referer= referer.substring(0, indexOf+1)+"docdelet=document "+documnet+" deleted successfully&"+referer.substring(indexOf+1);
            
            return "redirect:"+referer;

    }

    @GetMapping("/searchdocs")
    public String searchDocument(ModelMap mp, DocumentDTO DocumentDTO) {

        String name = (String) mp.getAttribute("authorizedUser");
        OwnerService.userSessionValidity(name);

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
           
        List<Car> searchForDocumentResult = DocumentService.searchForDocument(name, extension,isIsSensitive ,username);

        mp.addAttribute("showAll", searchForDocumentResult);
        mp.addAttribute("noValue", CarServices.columnEntirelyHasNoValueSort(searchForDocumentResult)); 
        mp.addAttribute("showTable", "");
        
        return "documnetSearchPage";
    }

    @GetMapping("/{by}")
    public String peformSearchDocumentOrderBy(ModelMap mp, @PathVariable String by, HttpServletRequest request) throws AccessDeniedException {

        String name = (String) mp.getAttribute("authorizedUser");
        OwnerService.userSessionValidity(name); 

        String refer = request.getHeader("Referer");

        boolean badRefer = refer == null || !refer.contains("document");
        
        if (badRefer) {
            throw new AccessDeniedException("Requested Resources Access Denied");
        }
        
        if (CarServices.numberOfRecors() == 0) {
            return "showSearchResultPage";
        }
        String suby = by.substring(0, by.indexOf('-') == -1 ? by.length() : by.indexOf('-'));
        
        DocumentDTO DocumentDTO = (DocumentDTO) mp.getAttribute("docSearchInput");
 
        String fileName = DocumentDTO.getName();
        String isSensitive = DocumentDTO.getCaseSensitive();
        FileExtension extension = DocumentDTO.getExtension();
        
        String username = (String) mp.getAttribute("authorizedUser");
         
        List<Car> searchForDocument = DocumentService.searchForDocumentOrderBy(fileName, extension, isSensitive, username, suby);
        
        if (by.endsWith("desc")) {

            Collections.reverse(searchForDocument);
        }
        
        mp.addAttribute("showAll", searchForDocument);
        mp.addAttribute("noValue", CarServices.columnEntirelyHasNoValueSort(searchForDocument)); 
        mp.addAttribute("allExten", EnumSet.range(FileExtension.PDF, FileExtension.ICS));
        mp.addAttribute("any", FileExtension.ANY);
        mp.addAttribute("showTable", "");
        
        return "documnetSearchPage";
    }
    
}
 