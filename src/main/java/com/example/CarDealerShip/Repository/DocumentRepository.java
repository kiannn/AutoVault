package com.example.CarDealerShip.Repository;

import com.example.CarDealerShip.Models.CarWithDocsDTO;
import com.example.CarDealerShip.Models.Documents;
import com.example.CarDealerShip.Models.FileExtension;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

@Transactional
public interface DocumentRepository extends JpaRepository<Documents, Integer> {

    final String SELECT = """
                          SELECT new com.example.CarDealerShip.Models.CarWithDocsDTO(car.itemNo, car.make, car.model, car.year, car.datePurchased, car.powerTrain, car.price, car.condn ,car.horsePower ,doc.id ,doc.name ,doc.size) 
                          """;

    final String SEARCH_BY_EXTENSION = SELECT + """
                                                 FROM availableCars car JOIN car.docs doc 
                                                 WHERE doc.extension = :extensionName 
                                                 AND car.owner.username = :userName
                                                 """;
    final String SEARCH_BY_NAME_EXTENSION = SELECT + """
                                                     FROM availableCars car JOIN car.docs doc 
                                                     WHERE doc.name = :docName 
                                                     AND car.owner.username = :userName
                                                     """;
    final String SEARCH_BY_NAME_EXTENSION_CASE_INSENSITIVE = SELECT + """
                                                                     FROM availableCars car JOIN car.docs doc 
                                                                     WHERE LOWER(doc.name) = :docName 
                                                                     AND car.owner.username = :userName
                                                                     """;

    final String SEARCH_BY_SUB_NAME = SELECT + """
                                               FROM availableCars car JOIN car.docs doc 
                                               WHERE doc.name LIKE CONCAT(:docName,'.','%')  
                                               AND car.owner.username = :userName
                                             """;

    final String SEARCH_BY_SUB_NAME_CASE_INSENSITIVE = SELECT + """
                                                               FROM availableCars car JOIN car.docs doc 
                                                               WHERE LOWER(doc.name) LIKE LOWER(CONCAT(:docName,'.','%'))  
                                                               AND car.owner.username = :userName
                                                               """;

    @Query("SELECT doc.id from documnets doc WHERE doc.car.owner.username = :username")
    List<Integer> findByCarOwnerUsername(String username);
    
    List<Documents> findByCarItemNo(Integer username);
    
    void deleteByCarItemNo(Integer carId);
     
    @Modifying 
    @Query("DELETE FROM documnets doc WHERE doc.car.itemNo IN :carIds")
    void deleteAllByCarItenN(List<Integer> carIds);

    @Query(SEARCH_BY_EXTENSION)
    public List<CarWithDocsDTO> searchByExtension(FileExtension extensionName, String userName);

    @Query(SEARCH_BY_NAME_EXTENSION)
    public  List<CarWithDocsDTO> searchByNameAndExtension(String docName, String userName);
    
    @Query(SEARCH_BY_NAME_EXTENSION_CASE_INSENSITIVE)
    public  List<CarWithDocsDTO> searchByNameAndExtensionCaseInSensitive(String docName, String userName);
    
    @Query(SEARCH_BY_SUB_NAME)
    public List<CarWithDocsDTO> searchBySubName(String docName, String userName);
    
    @Query(SEARCH_BY_SUB_NAME_CASE_INSENSITIVE)
    public List<CarWithDocsDTO> searchBySubNameCaseInSensitive(String docName, String userName);
 
    /*
      *****************************
      ORDER BY
      *****************************
    */
    
//    @Query(SEARCH_BY_EXTENSION)
//    public List<Car> searchByExtensionOrderBy(FileExtension extensionName, String userName, Sort sort);
//
//    @Query(SEARCH_BY_NAME_EXTENSION)
//    public  List<Car> searchByNameAndExtensionOrderBy(String docName, String userName, Sort sort);
//    
//    @Query(SEARCH_BY_NAME_EXTENSION_CASE_INSENSITIVE)
//    public  List<Car> searchByNameAndExtensionCaseInSensitiveOrderBy(String docName, String userName, Sort sort);
//
//    @Query(SEARCH_BY_SUB_NAME)
//    public List<Car> searchBySubNameOrderBy(String docName, String userName, Sort sort);
//    
//    @Query(SEARCH_BY_SUB_NAME_CASE_INSENSITIVE)
//    public List<Car> searchBySubNameCaseInSensitiveOrderBy(String docName, String userName, Sort sort);
}
