package com.example.demo.controller;

import com.example.demo.config.JwtGenerator;
import com.example.demo.dto.request.EmployeeRequest;
import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.response.EmployeeResponse;
import com.example.demo.dto.response.LoginResponse;
import com.example.demo.dto.response.checkinout;
import com.example.demo.dto.response.listCheckinout;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.EmailService;
import com.example.demo.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("api/v1/")
public class EmployeeController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/admin/register")
    @ResponseStatus(HttpStatus.CREATED)
    public int addEmployee(@RequestBody EmployeeRequest request) {
        return employeeService.createEmployee(request);
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse loginEmployee(@RequestBody LoginRequest loginRequest) {
        LoginResponse token = employeeService.login(loginRequest);
        return token;
    }

    @GetMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        return employeeService.logout(request, response);
    }

    @PutMapping("admin/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String updateEmployee(@RequestBody EmployeeRequest employeeRequest, @PathVariable("id") Long id) {
        employeeService.updateEmployee(employeeRequest, id);
        return "update success";

    }

    @GetMapping("admin/get/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeResponse getById(@PathVariable("id") Long id) {
        return employeeService.getById(id);
    }

    @GetMapping("admin/getAll")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeResponse> getAll() {
        return employeeService.getAllEmployee();
    }

    @DeleteMapping("admin/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable("id") Long id) {
        employeeService.deleteEmployee(id);
    }

    @GetMapping("admin/findByName")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeResponse> findByName(@RequestParam(value = "name", required = false) String name) {
        return employeeService.findByName(name);
    }

    @GetMapping("admin/getAllSortByName/")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeResponse> findAllSortByName() {
        return employeeService.findAllSortByName();
    }

    @GetMapping("admin/getListCheckinoutDefault")
    @ResponseStatus(HttpStatus.OK)
    public List<listCheckinout> getListCheckinoutDefault() {
        return employeeService.listEmployeeCheckinoutDefault();
    }

    @GetMapping("/getCheckinoutDefault/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<checkinout> getCheckinoutDefault(@PathVariable("id") Long id) {
        return employeeService.employeeCheckinoutDefault(id);
    }

    @GetMapping("/getCheckinoutInMonth/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<checkinout> getCheckinoutInMonth(@RequestParam int year, int month, @PathVariable("id") Long id) {
        return employeeService.employeeCheckinoutInMonth(year, month, id);
    }

    @GetMapping("admin/getListCheckinoutInMonth")
    @ResponseStatus(HttpStatus.OK)
    public List<listCheckinout> getListCheckinoutInMonth(@RequestParam int year, int month) {
        return employeeService.listEmployeeCheckinoutInMonth(year, month);
    }

    @GetMapping("/getCheckinoutInMonthError/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<checkinout> getCheckinoutInMonthError(@RequestParam int year, int month, @PathVariable("id") Long id) {
        return employeeService.employeeCheckinoutInMonthError(year, month, id);
    }

    @GetMapping("admin/getListCheckinoutInMonthError")
    @ResponseStatus(HttpStatus.OK)
    public List<listCheckinout> getListCheckinoutInMonthError(@RequestParam int year, int month) {
        return employeeService.listEmployeeCheckinoutInMonthError(year, month);
    }

//    @PostMapping("admin/send")
//    public String sendMail(@RequestParam String to, String[] cc, String subject, String body) {
//        return emailService.sendMail(to, cc, subject, body);
//    }
}
