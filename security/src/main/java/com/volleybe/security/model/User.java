package com.volleybe.security.model;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "Users")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "username")
    private String username;

    @Column(name = "UPASSWORD", nullable = false, unique = true)
    private String password;

    @Column(name = "EMAIL", nullable = true)
    private String email;

    @Column(name = "FIRSTNAME", nullable = true)
    private String firstName;

    @Column(name = "LASTNAME", nullable = true)
    private String lastName;
}
