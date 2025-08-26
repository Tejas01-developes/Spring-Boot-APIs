package com.practice.keepgowing.service;

import com.practice.keepgowing.entity.userschema;
import com.practice.keepgowing.repository.userrepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class userservice {
    @Autowired
    private userrepository userrepository;

    @Autowired
    private BCryptPasswordEncoder bcrypt=new BCryptPasswordEncoder();


    public boolean save(userschema userschema){
        String hash=bcrypt.encode(userschema.getPassword());
        userschema.setPassword(hash);

        userrepository.save(userschema);
        return true;
    }

    public Optional<userschema> findusername(String username){
        Optional<userschema> find=userrepository.findbyusername(username);
        if(find.isPresent()){
           userschema user= find.get();

        }
        return find;


    }

    public Optional<userschema> findemail(String email){
        Optional<userschema> find=userrepository.findbyemail(email);
        if(find.isPresent()){
            userschema user= find.get();

        }
        return find;


    }



    public String update(String currentusername, String newusername) throws IOException {
        Optional<userschema> find=userrepository.findbyusername(currentusername);
        if(find.isPresent()){
           userschema user= find.get();

           if(!userrepository.findbyusername(newusername).isPresent() && !currentusername.equals(newusername)) {

            user.setUsername(newusername);
            userrepository.save(user);

           }
           return "username is already in use";


        }

return "username is not registered";

    }

    public boolean delete(String username){
        Optional<userschema> find=userrepository.findbyusername(username);

        if(find.isPresent()){
            userschema user= find.get();
            userrepository.deletebyusername(String.valueOf(user));
        }
        return false;

    }

    public String resettoken(String email){
        Optional<userschema> find=userrepository.findbyemail(email);
        if(find.isEmpty()){
            return "email not registered";
        }
        userschema user= find.get();
        String token= String.valueOf(UUID.randomUUID());
        user.setResettoken(token);
        user.setTokenexiry(LocalDateTime.parse(String.valueOf(LocalDateTime.now().plusMinutes(10))));
        userrepository.save(user);
        return token;
    }


    public Optional<userschema> validatetoken(String token) {
        Optional<userschema> find = userrepository.findbytoken(token);

        if (find.isEmpty()) {
            throw new RuntimeException("invalid token");
        }
        userschema user = find.get();
        if(user.getTokenexiry().isBefore(LocalDateTime.now())){
            throw new RuntimeException("token expired");
        }
        return find;

    }



    public String forgotpass(String token,String newpassword){
     Optional<userschema> set=validatetoken(token);
     set.get().setPassword(bcrypt.encode(newpassword));
     set.get().setResettoken(null);
     set.get().setTokenexiry(null);
     set.get().getEmail();
     return set.get().getUsername();
    }
}
