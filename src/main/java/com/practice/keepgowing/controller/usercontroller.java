package com.practice.keepgowing.controller;

import com.practice.keepgowing.email.email;
import com.practice.keepgowing.entity.userschema;
import com.practice.keepgowing.generet.generat;
import com.practice.keepgowing.service.userservice;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;


@RequestMapping
@RestController
public class usercontroller {
    @Autowired
    private userservice userservice;

    @Autowired
    private email sendemail;


    @Autowired
    private BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

    @Autowired
    private generat generat;


    @PostMapping("/register")
    public ResponseEntity<String> registration(@RequestBody userschema userschema) throws MessagingException {
        Optional<userschema> find = userservice.findemail(userschema.getEmail());
        if (find.isPresent()) {
            ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body("email is already registered");
        }

        userservice.save(userschema);
        try {
            sendemail.sendmail(
                    find.get().getEmail(),
                    "welcome to our page be connected with us",
                    "Welcome-Template"
            );


        } catch (MessagingException e) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("unable to send email");
        }

        return ResponseEntity.status(HttpStatus.OK).body("registration done succesfull");

    }

    @PostMapping("/login")
    public ResponseEntity<String> userlogin(@RequestBody userschema userschema, HttpServletResponse resp)   {
        Optional<userschema> find = userservice.findemail(userschema.getEmail());
        if (find.isEmpty()) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("email is not registered");
        }
        userschema user = find.get();
        if (!bcrypt.matches(user.getPassword(), userschema.getPassword())) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("password is incorrect");
        }
        String accesstoken = generat.accesstoken(userschema.getUsername());
        ResponseCookie cookie = ResponseCookie.from("accesstoken", accesstoken)
                .httpOnly(true)
                .secure(true)
                .maxAge(60 * 60)
                .build();


        String refreshtoken = generat.refreshtoken(userschema.getUsername());
        ResponseCookie cookie1 = ResponseCookie.from("refreshtoken", refreshtoken)
                .httpOnly(true)
                .secure(true)
                .maxAge(7 * 24 * 60 * 60)
                .build();

        resp.setHeader("Set-cookie", cookie.toString());
        resp.setHeader("Set-cookie", cookie1.toString());

        return ResponseEntity.status(HttpStatus.OK).body("login succesfully done");


    }

@PostMapping("/logout")
public ResponseEntity<String>logoutuser(HttpServletResponse resp){
        ResponseCookie cookie=ResponseCookie.from("accesstoken",null)
                .httpOnly(true)
                .secure(true)
                .maxAge(0)
                .build();

    ResponseCookie cookie1=ResponseCookie.from("refreshtoken",null)
            .httpOnly(true)
            .secure(true)
            .maxAge(0)
            .build();

    return ResponseEntity.status(HttpStatus.OK).body("user logout successful");



}

@DeleteMapping("/delete")
    public ResponseEntity<String>deleteuser(String username, HttpServletResponse resp, HttpServletRequest req){
        Optional<userschema> find=userservice.findusername(username);

        if(find.isEmpty()){
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("registered username not found");
        }
        userschema user= find.get();
        userservice.delete(String.valueOf(user));

        return ResponseEntity.status(HttpStatus.OK).body("user is successfully deleted");


}
@PutMapping("/update")
    public ResponseEntity<String>updateuser(@RequestBody userschema userschema,HttpServletResponse resp,HttpServletRequest req) throws IOException {
       String currentusername=(String) req.getAttribute("username");


        userservice.update(currentusername,userschema.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body("update successful");
    }

    @PostMapping("/forgotpassword")
    public ResponseEntity<String> resetpagelink(@RequestParam String email ,HttpServletRequest req,HttpServletResponse resp) throws MessagingException {
     String token=userservice.resettoken(email);
     if(token.isEmpty()){
         ResponseEntity.status(HttpStatus.NOT_FOUND).body("email is not registered");
     }

     String resetlink=req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/resetpage?token=" + token;


    sendemail.sendmail(
            email,
            "Reset link hase been send" + resetlink,
            "reset- link"


    );

     return  ResponseEntity.status(HttpStatus.OK).body("resetpage link send to your email");

    }

    @GetMapping("/resetpage")
    public ResponseEntity<String> resetpage(@RequestParam String token ,HttpServletResponse resp,HttpServletRequest req){
        try {
          userservice.validatetoken(token);
            return ResponseEntity.status(HttpStatus.OK).body("resetpage is valid you can change your password");
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @PostMapping("/forgotpassword/reset")
    public ResponseEntity<String>forgotpass(@RequestParam String token ,String newpass,HttpServletResponse resp) throws IOException {
        try {
            String update = userservice.forgotpass(token, newpass);
            ResponseEntity.status(HttpStatus.OK).body("password updated successful");
        } catch (Exception e) {
           resp.getWriter().write("password update failed");
        }
       return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("password can not be updated");
    }





}
