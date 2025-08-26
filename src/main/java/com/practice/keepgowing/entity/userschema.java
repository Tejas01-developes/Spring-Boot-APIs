package com.practice.keepgowing.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class userschema {
    @Id
    private ObjectId userid;
    private String username;
    private String email;
    private String password;
    private String resettoken;
    private LocalDateTime tokenexiry;

    public LocalDateTime getTokenexiry() {
        return tokenexiry;
    }

    public void setTokenexiry(LocalDateTime tokenexiry) {
        this.tokenexiry = tokenexiry;
    }

    public ObjectId getUserid() {
        return userid;
    }

    public String getResettoken() {
        return resettoken;
    }

    public void setResettoken(String resettoken) {
        this.resettoken = resettoken;
    }



    public void setUserid(ObjectId userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;                                        // this all getter and setter are not written they can be generated with simple steps
                                                                // right click go to 'generat' then 'getter and setter' select all and it will give both
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
