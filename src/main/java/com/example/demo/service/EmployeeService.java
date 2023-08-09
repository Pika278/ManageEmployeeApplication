package com.example.demo.service;

import com.example.demo.dto.request.EmployeeRequest;
import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.response.EmployeeResponse;
import com.example.demo.dto.response.LoginResponse;
import com.example.demo.dto.response.checkinout;
import com.example.demo.dto.response.listCheckinout;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface EmployeeService {
    int createEmployee(EmployeeRequest employeeRequest);
    LoginResponse login(LoginRequest loginRequest);
    String logout(HttpServletRequest request, HttpServletResponse response);
    void updateEmployee(EmployeeRequest employeeRequest, Long id);
    EmployeeResponse getById(Long id);
    List<EmployeeResponse> getAllEmployee();
    void deleteEmployee(Long id);
    List<EmployeeResponse> findByName(String name);
    List<EmployeeResponse> findAllSortByName();
    List<checkinout> employeeCheckinoutDefault(Long id);
    List<listCheckinout> listEmployeeCheckinoutDefault();
    List<checkinout> employeeCheckinoutInMonth(int year, int month, Long id);
    List<listCheckinout> listEmployeeCheckinoutInMonth(int year, int month);
    List<checkinout> employeeCheckinoutInMonthError(int year, int month, Long id);
    List<listCheckinout> listEmployeeCheckinoutInMonthError(int year, int month);


}
