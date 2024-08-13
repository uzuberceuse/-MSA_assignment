package com.sparta.msa_exam.auth.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user")
public class User {

    @Id
    private String user_id;
    private String username;
    private String password;
}
