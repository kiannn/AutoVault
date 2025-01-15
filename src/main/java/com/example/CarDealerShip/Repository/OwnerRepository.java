
package com.example.CarDealerShip.Repository;

import com.example.CarDealerShip.Models.MakeAndModel;
import com.example.CarDealerShip.Models.Owner;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OwnerRepository extends JpaRepository<Owner, String>{ 

    @Query("SELECT u.email FROM users u WHERE LOWER(u.email) = :email")
    public String uniqueEmailSignUp(String email);
    
    @Query("SELECT u.email FROM users u WHERE LOWER(u.email) = :email AND u.username != :username")
    public String uniqueEmailUpdate(String email, String username);

    @Query("SELECT u.username FROM users u WHERE u.username = :username")
    public Optional<String> findByID(String username);
    
    @Query("SELECT o.MakeAndModel FROM users o WHERE o.username = :username")
    public List<MakeAndModel> getAvailableMakesAndModels(String username);
    
}
