package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column
    private String email;
    @Column
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column
    private String name;
    @Column
    private String gender;
    @Column
    private String address;
    @Column
    private String dob;
    @Column
    private String phone;
    @Column
    private String position;
    @Column
    private int checkin;

//    @OneToMany(mappedBy = "employee", cascade = CascadeType.PERSIST)
//    private List<Checkin> books = new ArrayList<>();

    public Employee(String email, String password, Role role, String name, String gender, String address, String dob, String phone, String position, int checkin) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.name = name;
        this.gender = gender;
        this.address = address;
        this.dob = dob;
        this.phone = phone;
        this.position = position;
        this.checkin = checkin;
    }

    public Employee(Long id, int checkin) {
        Id = id;
        this.checkin = checkin;
    }
}
