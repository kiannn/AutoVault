package com.example.CarDealerShip.Services;

import com.example.CarDealerShip.Models.Owner;
import com.example.CarDealerShip.Models.Authorities;
import com.example.CarDealerShip.Models.Credentials;
import com.example.CarDealerShip.Models.MakeAndModel;
import com.example.CarDealerShip.Models.OwnerSignUpDTO;
import com.example.CarDealerShip.Models.OwnerStatDTO;
import com.example.CarDealerShip.Models.PasswordDTO;
import com.example.CarDealerShip.Repository.OwnerRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {

    @Autowired
    JdbcUserDetailsManager JdbcUserDetailsManager;

    @Autowired
    OwnerRepository OwnerRepository;

    public void createNewUSer(OwnerSignUpDTO OwnerDTO) {
        String encode = new BCryptPasswordEncoder().encode(OwnerDTO.getCredentials().getPassword());

        Owner ownerPersist = Owner.builder()
                                    .firstName(OwnerDTO.getFirstName().trim())
                                    .lastName(OwnerDTO.getLastName().trim())
                                    .dob(OwnerDTO.getDob())
                                    .email(OwnerDTO.getEmail())
                                    .username(OwnerDTO.getUsername())
                                    .credentials(Credentials.builder()
                                                            .password(encode)
                                                            .confirmPassword(encode)
                                                            .enabled(true)
                                                            .build())
                                    .build();
        
        Authorities build = Authorities.builder()
                .authority("ROLE_USER")
                .carOwner(ownerPersist)
                .build();

        ownerPersist.getCredentials().setAuthorities(List.of(build));
 
        Map<String, List<String>> modelmake = new HashMap<>();

        modelmake.put("Mercedes-Benz", List.of("CLS-Class", "CLK-Class", "S-Class"));
        modelmake.put("BMW", List.of("Z4", "740Li", "X3"));
        
       List<MakeAndModel> MakeAndMode = new ArrayList<>();
        
        for (Map.Entry<String, List<String>> entry : modelmake.entrySet()) {

            for (String s : entry.getValue()) {

                MakeAndModel MakeAndModel= new MakeAndModel();
                MakeAndModel.setAvailableMake(entry.getKey());
                MakeAndModel.setAvailableModel(s);

                MakeAndMode.add(MakeAndModel);
            }
        }
        
        ownerPersist.setMakeAndModel(MakeAndMode);
        
        OwnerRepository.save(ownerPersist);


    }

    public boolean userExists(String username) {

        return JdbcUserDetailsManager.userExists(username.trim());

    }

    public OwnerStatDTO findUserById(String usernane) {

        return OwnerRepository.findByIdDTO(usernane).orElseThrow(NoSuchElementException::new);
    }

    public boolean emailUniquenessForSignUp(OwnerSignUpDTO Owner) {

        if (Owner.getEmail().isBlank()) {
            return true;
        }

        String owner = OwnerRepository.uniqueEmailSignUp(Owner.getEmail().toLowerCase());

        return owner == null;

    }

    public boolean emailUniquenessForUpdate(OwnerStatDTO owner, String logedInUser) {

        if (owner.getEmail().isBlank()) {
            return true;
        }

        String uniqueEmailUpdate = OwnerRepository.uniqueEmailUpdate(owner.getEmail().toLowerCase(), logedInUser);

        return uniqueEmailUpdate == null;
    }

    public void updatePersonalInfo(String loggedInUsername, OwnerStatDTO owner) {

        Owner findById = OwnerRepository.findById(loggedInUsername).get();
        
        findById.setFirstName(owner.getFirstName().trim());
        findById.setLastName(owner.getLastName().trim());
        findById.setDob(owner.getDob());
        findById.setEmail(owner.getEmail());
        
        
        OwnerRepository.save(findById);
    }

    public boolean verifyPasswordCorrectness(PasswordDTO passDto, String username) {
 
        UserDetails loadUserByUsername = JdbcUserDetailsManager.loadUserByUsername(username);

        String password = loadUserByUsername.getPassword();

        boolean matches = new BCryptPasswordEncoder().matches(passDto.getCurrentPass(), password);

        return matches;
    }

    public void updateUserPassword(String username, PasswordDTO passDto) {

        UserDetails loadUserByUsername = JdbcUserDetailsManager.loadUserByUsername(username);
        Collection<? extends GrantedAuthority> authorities = loadUserByUsername.getAuthorities();
        String[] roles = authorities.stream().map(a -> a.getAuthority().replace("ROLE_", "")).toArray(size -> new String[size]);
        String encode = new BCryptPasswordEncoder().encode(passDto.getNewPass());
 
        UserDetails newUser = User.builder().password(encode)
                .username(username)
                .roles(roles)
                .build();
  
        JdbcUserDetailsManager.updateUser(newUser);

        Owner Owner = OwnerRepository.findById(username).get();
        Owner.getCredentials().setConfirmPassword(encode);
        OwnerRepository.save(Owner);
    }

    public void deleteAccount(String user) {
        OwnerRepository.deleteById(user);
    }

//    public void userSessionValidity(String username) {
//
//        OwnerRepository.findByID(username).orElseThrow(NoSuchElementException::new); 
//    }
 
    public List<MakeAndModel> availableMakesAndModels(String name) {
        
    List<MakeAndModel> availableMakesAndModels = OwnerRepository.getAvailableMakesAndModels(name);
      
      return availableMakesAndModels;

    }

    public void updateMakeAndModelList(String name, List<MakeAndModel> availableMakesAndModels) {

        Owner owner = OwnerRepository.findById(name).get();
        owner.setMakeAndModel(availableMakesAndModels);
        OwnerRepository.save(owner);
    }
}
