package com.example.demo.dto.request;

import com.example.demo.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequest {
    private String email;
    private String password;
    private Role role;
    private String name;
    private String gender;
    private String address;
    private String dob;
    private String phone;
    private String position;
}
