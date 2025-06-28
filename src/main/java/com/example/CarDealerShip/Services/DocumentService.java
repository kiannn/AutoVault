
package com.example.CarDealerShip.Services;

import com.example.CarDealerShip.Models.Car;
import com.example.CarDealerShip.Models.Documents;
import com.example.CarDealerShip.Models.FileExtension;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.CarDealerShip.Repository.DocumentRepository;
import java.util.ArrayList;
import org.springframework.data.domain.Sort;

@Service
public class DocumentService {

    @Autowired
    DocumentRepository DocumentRepository; 
    
    @Autowired
    CompressionService CompressionService;
            
    public List<Documents> retreiveDocument(MultipartFile[] documents, Car car) throws IOException {
        List<Documents> DocsList = new ArrayList<>();
        
        for (MultipartFile file : documents) {
 
            if (file.getContentType().equals("application/octet-stream")) { 
              
                continue;
            } 
            
            String originalFilename = file.getOriginalFilename();
            
            FileExtension extn = null;
            
            for (FileExtension ex : FileExtension.values()) {
                if (originalFilename.substring(originalFilename.lastIndexOf('.')).equals(ex.getEx())) {
                    extn = ex;
                    break;
                }
            }            
            
            Documents Document = Documents.builder()
                    .name(originalFilename)
                    .extension(extn)
                    .size(file.getSize())
                    .type(file.getContentType())
                    .data(CompressionService.compress(file.getBytes()))
                    .car(car)
                    .build();

            DocsList.add(Document);
        }
        
        return DocsList;
    }

    public void deletSingleDocument(Integer id) {

        DocumentRepository.deleteById(id);

    }

    public Documents getDocumnetById(Integer id){
    
        Documents findById = DocumentRepository.findById(id).get();
        byte[] fileData = findById.getData();
        
        fileData = findById.getName().endsWith(".zip") ? fileData : CompressionService.decompress(fileData) ;
        
        findById.setData(fileData);
        
        return findById;
    
    }

    public boolean isValidDocumentId(String username, Integer id) {

        List<Integer> fileIds = DocumentRepository.findByCarOwnerUsername(username);

        boolean contains = fileIds.contains(id);
        
        return contains;
    }

    public List<Documents> getDocumnetsByCar(Integer itenNo) {
        
        List<Documents> findByCarItemNo = DocumentRepository.findByCarItemNo(itenNo);

        return findByCarItemNo;
    }

    public List<Car> searchForDocument(String name, FileExtension extension, String caseSensitive, String username) {

        if (name.trim().isEmpty()) {
            List<Car> cars = DocumentRepository.searchByExtension(extension, username);

            return cars;
        } 
        if(extension!=null && extension!=FileExtension.ANY) {

            String fileFullName = name.concat(extension.getEx());
            List<Car> cars = caseSensitive != null ? DocumentRepository.searchByNameAndExtension(fileFullName, username)
                    : DocumentRepository.searchByNameAndExtensionCaseInSensitive(fileFullName.toLowerCase(), username);
            return cars;
        }

        List<Car> cars = caseSensitive != null ? DocumentRepository.searchBySubName(name, username)
                    : DocumentRepository.searchBySubNameCaseInSensitive(name, username);
        
        return cars;
    }
//
//    public List<Car> searchForDocumentOrderBy(String name, FileExtension extension, String caseSensitive, String userName, String suby) {
//
//        Sort by = Sort.by(suby);
//        List<Car> cars;
//
//        if (name.trim().isEmpty()) {
//            cars = DocumentRepository.searchByExtensionOrderBy(extension, userName, by);
//            return cars;
//
//        } 
//        else if (extension != null && extension!=FileExtension.ANY) {
//
//            String fileFullName = name.concat(extension.getEx());
//
//            cars = caseSensitive != null ? DocumentRepository.searchByNameAndExtensionOrderBy(fileFullName, userName, by)
//                    : DocumentRepository.searchByNameAndExtensionCaseInSensitiveOrderBy(fileFullName.toLowerCase(), userName, by);
//            return cars;
//        }
//        
//        cars = caseSensitive != null ? DocumentRepository.searchBySubNameOrderBy(name, userName,by)
//                    : DocumentRepository.searchBySubNameCaseInSensitiveOrderBy(name, userName, by);
//        
//        return cars;
//    }
}
