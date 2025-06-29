package com.example.CarDealerShip.Repository;

import com.example.CarDealerShip.Models.Car;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CarRepository extends JpaRepository<Car, Integer> {
    
    final String QUARY =  """
                             SELECT car FROM availableCars car WHERE car.owner.username = :username 
                             AND car.make= :make 
                             AND car.model IN :modelList
                             AND (car.price IS NULL OR car.price BETWEEN :priceFrom and :priceTo)
                             AND (car.year IS NULL OR car.year BETWEEN :yearFrom and :yearTo)
                             AND (car.datePurchased IS NULL OR car.datePurchased BETWEEN :dateFrom and :dateTo)
                        """;
    
    final String QUARY_NO_MODEL = """
                                     SELECT car FROM availableCars car WHERE car.owner.username = :username 
                                     AND car.make= :make 
                                     AND (car.price is null OR car.price BETWEEN :priceFrom and :priceTo)
                                     AND (car.year is null OR car.year BETWEEN :yearFrom and :yearTo)
                                     AND (car.datePurchased is null OR car.datePurchased BETWEEN :dateFrom and :dateTo)
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
     
    @Query(QUARY)
    public List<Car> queryBasedOnSearch(@Param("username")String user, String make, List<String> modelList,
                                        Double priceFrom, Double priceTo,
                                        Integer yearFrom, Integer yearTo,
                                        LocalDate dateFrom, LocalDate dateTo);
    
    @Query(QUARY_NO_MODEL)
    public List<Car> queryBasedOnSearchNoModel(@Param("username")String user,String make, 
                                               Double priceFrom, Double priceTo, 
                                               Integer yearFrom, Integer yearTo, 
                                               LocalDate dateFrom, LocalDate dateTo);
                                                             
    /**
     * org.springframework.dao.InvalidDataAccessApiUsageException: 
     * You're trying to execute a streaming query method without a surrounding transaction that
     * keeps the connection open so that the Stream can actually be consumed; Make sure the code consuming
     * the stream uses @Transactional or any other way of declaring a (read-only) transaction] with root cause
     */
    public Stream<Car> findByOwnerUsername(String username);
    
    @Query("SELECT car.itemNo FROM availableCars car WHERE car.owner.username = :username")
    public List<Integer> findCarIdsByOwnerUsername(String username);

}
