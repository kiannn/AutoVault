package com.example.CarDealerShip.Repository;

import com.example.CarDealerShip.Models.Car;
import com.example.CarDealerShip.Models.CarMakeModelDTO;
import com.example.CarDealerShip.Models.CarStatsDTO;
import com.example.CarDealerShip.Models.CarWithDocsDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CarRepository extends JpaRepository<Car, Integer> {

    final String SELECT = """
                            SELECT new com.example.CarDealerShip.Models.CarWithDocsDTO(car.itemNo, car.make, car.model, car.year, car.datePurchased, car.powerTrain, car.price, car.condn ,car.horsePower ,doc.id ,doc.name ,doc.size)  
                           """;   
    final String CONDITIONS_TO_FILTER = """
                                          AND (car.price IS NULL OR car.price BETWEEN :priceFrom and :priceTo)
                                          AND (car.year IS NULL OR car.year BETWEEN :yearFrom and :yearTo)
                                          AND (car.datePurchased IS NULL OR car.datePurchased BETWEEN :dateFrom and :dateTo)
                                        """;
    final String SEARCH_WITH_MODEL = SELECT + """
                                                FROM availableCars car LEFT JOIN car.docs doc
                                                WHERE car.owner.username = :username 
                                                AND car.make= :make 
                                                AND car.model IN :modelList
                                               """ + CONDITIONS_TO_FILTER;
 
    final String SEARCH_WITHOUT_MODEL = SELECT + """
                                                    FROM availableCars car LEFT JOIN car.docs doc 
                                                    WHERE car.owner.username = :username 
                                                    AND car.make= :make 
                                                  """ +CONDITIONS_TO_FILTER; 
    final String FIND_BY_OWNER =SELECT + """
                                          FROM availableCars car LEFT JOIN car.docs doc 
                                          WHERE car.owner.username= :username
                                         """;

    final String FIND_BY_CARID = SELECT + """
                                            FROM availableCars car LEFT JOIN car.docs doc 
                                            WHERE car.itemNo= :id
                                          """;
            
    @Query("SELECT min(price) FROM availableCars")
    public Double findPriceMin();

    @Query("SELECT max(price) FROM availableCars")
    public Double findPriceMax();

    @Query("SELECT min(year) FROM availableCars")
    public Integer findyearMin();

    @Query("SELECT max(year) FROM availableCars")
    public Integer findyearMax();
 
    @Query("SELECT min(datePurchased) FROM availableCars")
    public LocalDate findDateMin();

    @Query("SELECT max(datePurchased) FROM availableCars")
    public LocalDate findDateMax();
     
    @Query(SEARCH_WITH_MODEL)
    public List<CarWithDocsDTO> queryBasedOnSearch(@Param("username")String user, String make, List<String> modelList,
                                        Double priceFrom, Double priceTo,
                                        Integer yearFrom, Integer yearTo,
                                        LocalDate dateFrom, LocalDate dateTo);
    
    @Query(SEARCH_WITHOUT_MODEL)
    public List<CarWithDocsDTO> queryBasedOnSearchNoModel(@Param("username")String user,String make, 
                                               Double priceFrom, Double priceTo, 
                                               Integer yearFrom, Integer yearTo, 
                                               LocalDate dateFrom, LocalDate dateTo);
                                                             
    /**
     * org.springframework.dao.InvalidDataAccessApiUsageException: 
     * You're trying to execute a streaming query method without a surrounding transaction that
     * keeps the connection open so that the Stream can actually be consumed; Make sure the code consuming
     * the Stream uses @Transactional or any other way of declaring a (read-only) transaction] with root cause
     */
    @Query(FIND_BY_OWNER)
    public Stream<CarWithDocsDTO> findByOwnerUsername(String username);

    @Query(FIND_BY_CARID)
    public List<CarWithDocsDTO> findCarById(int id);

    @Query("""
             SELECT new com.example.CarDealerShip.Models.CarMakeModelDTO(car.make, car.model) 
             FROM availableCars car WHERE car.itemNo= :id
           """)
    public CarMakeModelDTO findCar_MakeAndModel_ById(int id);

    @Query("""
             SELECT new com.example.CarDealerShip.Models.CarStatsDTO(car.datePurchased, car.price, car.horsePower) 
             FROM availableCars car WHERE car.itemNo= :id
           """)
    public CarStatsDTO findCar_Stats_ById(int id);

    @Query("SELECT car.itemNo FROM availableCars car WHERE car.owner.username = :username")
    public List<Integer> findCarIdsByOwnerUsername(String username);

}
