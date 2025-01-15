package com.example.CarDealerShip.Services;

import com.example.CarDealerShip.Models.Authorities;
import com.example.CarDealerShip.Models.MakeAndModel;
import com.example.CarDealerShip.Models.Owner;
import com.example.CarDealerShip.Models.PasswordDTO;
import com.example.CarDealerShip.Repository.AuthoritiesRepository;
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

    @Autowired
    AuthoritiesRepository AuthoritiesRepository;

    public void createNewUSer(Owner Owner) {
        String encode = new BCryptPasswordEncoder().encode(Owner.getCredentials().getPassword());

        Owner.getCredentials().setPassword(encode);
        Owner.getCredentials().setConfirmPassword(encode);

        Owner.getCredentials().setEnabled(true);
        Owner.setFirstName(Owner.getFirstName().trim());
        Owner.setLastName(Owner.getLastName().trim());

        Authorities build = Authorities.builder()
                .authority("ROLE_USER")
                .carOwner(Owner)
                .build();

        Owner.getCredentials().setAuthorities(List.of(build));
 
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
        
        Owner.setMakeAndModel(MakeAndMode);
        
        OwnerRepository.save(Owner);


    }

    public boolean userExists(String username) {

        return JdbcUserDetailsManager.userExists(username.trim());

    }

    public Owner findUserById(String usernane) {

        return OwnerRepository.findById(usernane).get();
    }

    public boolean emailUniquenessForSignUp(Owner Owner) {

        if (Owner.getEmail().isBlank()) {
            return true;
        }

        String owner = OwnerRepository.uniqueEmailSignUp(Owner.getEmail().toLowerCase());

        return owner == null;

    }

    public boolean emailUniquenessForUpdate(Owner owner, String logedInUser) {

        if (owner.getEmail().isBlank()) {
            return true;
        }

        String uniqueEmailUpdate = OwnerRepository.uniqueEmailUpdate(owner.getEmail().toLowerCase(), logedInUser);

        return uniqueEmailUpdate == null;
    }

    public void updatePersonalInfo(String loggedInUsername, Owner owner) {

        owner.setFirstName(owner.getFirstName().trim());
        owner.setLastName(owner.getLastName().trim());

        Owner findById = OwnerRepository.findById(loggedInUsername).get();
        System.out.println(findById);
        
        findById.setFirstName(owner.getFirstName());
        findById.setLastName(owner.getLastName());
        findById.setDob(owner.getDob());
        findById.setEmail(owner.getEmail());
        
        
        OwnerRepository.save(findById);
    }

    public boolean passwordMatch(PasswordDTO passDto, String username) {
 
        UserDetails loadUserByUsername = JdbcUserDetailsManager.loadUserByUsername(username);

        String password = loadUserByUsername.getPassword();

        boolean matches = new BCryptPasswordEncoder().matches(passDto.getCurrentPass(), password);

        return matches;
    }

    public void updateUserPassword(String username, PasswordDTO passDto) {

        UserDetails loadUserByUsername = JdbcUserDetailsManager.loadUserByUsername(username);
        Collection<? extends GrantedAuthority> authorities = loadUserByUsername.getAuthorities();

        String encode = new BCryptPasswordEncoder().encode(passDto.getNewPass());

        UserDetails newUser = User.builder().password(encode)
                .username(username)
                .authorities(authorities)
                .build();

        JdbcUserDetailsManager.updateUser(newUser);

        Owner Owner = OwnerRepository.findById(username).get();
        Owner.getCredentials().setConfirmPassword(encode);
        OwnerRepository.save(Owner);
    }

    public void deleteAccount(String user) {
        OwnerRepository.deleteById(user);
    }

    public void userSessionValidity(String username) {

        OwnerRepository.findByID(username).orElseThrow(NoSuchElementException::new); 
    }
 
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
