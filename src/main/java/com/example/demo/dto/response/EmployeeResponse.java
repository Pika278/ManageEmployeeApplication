package com.example.demo.dto.response;

import com.example.demo.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeResponse {
    private Long id;
    private String email;
    private String password;
    private Role role;
    private String name;
    private String gender;
    private String address;
    private String dob;
    private String phone;
    private String position;
    private int checkin;
}
