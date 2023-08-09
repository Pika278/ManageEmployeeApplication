package com.example.demo.service.impl;

import com.example.demo.config.JwtGenerator;
import com.example.demo.dto.request.EmployeeRequest;
import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.response.EmployeeResponse;
import com.example.demo.dto.response.LoginResponse;
import com.example.demo.dto.response.checkinout;
import com.example.demo.dto.response.listCheckinout;
import com.example.demo.entity.Employee;
import com.example.demo.exception.EmailExistedException;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.EmailService;
import com.example.demo.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;

    @Override
    @Transactional
    public int createEmployee(EmployeeRequest employeeRequest) {
        if (employeeRepository.existsByEmail(employeeRequest.getEmail())) {
            throw new EmailExistedException("Email existed");
        }
        int min = 1000;
        int max = 9999;
        Random random = new Random();
        int randomNumber = 0;
        while (true) {
            randomNumber = randomNumber = random.nextInt(max - min + 1) + min;
            if (!employeeRepository.existsByCheckin(randomNumber)) {
                break;
            }
        }

        Employee employee = new Employee(
                employeeRequest.getEmail(),
                this.passwordEncoder.encode(employeeRequest.getPassword()),
                employeeRequest.getRole(),
                employeeRequest.getName(),
                employeeRequest.getGender(),
                employeeRequest.getAddress(),
                employeeRequest.getDob(),
                employeeRequest.getPhone(),
                employeeRequest.getPosition(),
                randomNumber
        );
        employeeRepository.save(employee);

        String to = employee.getEmail();
        String[] cc = {"kimoanhh0103@gmail.com"};
        String subject = "Welcom to NCC";
        String body = "This is your checkin code: " + randomNumber;
        emailService.sendMail(to, cc, subject, body);

        return randomNumber;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Employee employee = employeeRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Email not Found"));
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generatorToken(authentication);
        return new LoginResponse(token);
    }

    @Override
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "Logout sucess";
    }

    @Override
    public void updateEmployee(EmployeeRequest employeeRequest, Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (!optionalEmployee.isPresent()) {
            throw new UsernameNotFoundException("Khong tim thay nhan vien");
        }
        Employee employee = optionalEmployee.get();
//        employee.setEmail(employeeRequest.getEmail());
//        employee.setPassword(this.passwordEncoder.encode(employeeRequest.getPassword()));
        employee.setRole(employeeRequest.getRole());
        employee.setName(employeeRequest.getName());
        employee.setGender(employeeRequest.getGender());
        employee.setAddress(employeeRequest.getAddress());
        employee.setDob(employeeRequest.getDob());
        employee.setPhone(employeeRequest.getPhone());
        employee.setPosition(employeeRequest.getPosition());
//        EmployeeResponse employeeResponse = new EmployeeResponse(
//                employee.getId(),
//                employee.getEmail(),
//                employee.getPassword(),
//                employee.getRole(),
//                employee.getName(),
//                employee.getGender(),
//                employee.getAddress(),
//                employee.getDob(),
//                employee.getPhone(),
//                employee.getPosition(),
//                employee.getCheckin()
//        );
        employeeRepository.save(employee);
//        return employeeResponse;
    }

    @Override
    public EmployeeResponse getById(Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (!optionalEmployee.isPresent()) {
            throw new UsernameNotFoundException("Khong tim thay nhan vien");
        }
        Employee employee = optionalEmployee.get();
        EmployeeResponse employeeResponse = new EmployeeResponse(
                employee.getId(),
                employee.getEmail(),
                employee.getPassword(),
                employee.getRole(),
                employee.getName(),
                employee.getGender(),
                employee.getAddress(),
                employee.getDob(),
                employee.getPhone(),
                employee.getPosition(),
                employee.getCheckin()
        );
        return employeeResponse;
    }

    @Override
    public List<EmployeeResponse> getAllEmployee() {
        List<Employee> list = employeeRepository.findAll();
        List<EmployeeResponse> responseList = new ArrayList<>();
        for (Employee employee : list) {
            EmployeeResponse employeeResponse = new EmployeeResponse(
                    employee.getId(),
                    employee.getEmail(),
                    employee.getPassword(),
                    employee.getRole(),
                    employee.getName(),
                    employee.getGender(),
                    employee.getAddress(),
                    employee.getDob(),
                    employee.getPhone(),
                    employee.getPosition(),
                    employee.getCheckin()
            );
            responseList.add(employeeResponse);
        }
        return responseList;
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public List<EmployeeResponse> findByName(String name) {
        List<Employee> list = employeeRepository.findAll();
        List<EmployeeResponse> responseList = new ArrayList<>();
        for (Employee employee : list) {
            if (employee.getName().equals(name)) {
                EmployeeResponse employeeResponse = new EmployeeResponse(
                        employee.getId(),
                        employee.getEmail(),
                        employee.getPassword(),
                        employee.getRole(),
                        employee.getName(),
                        employee.getGender(),
                        employee.getAddress(),
                        employee.getDob(),
                        employee.getPhone(),
                        employee.getPosition(),
                        employee.getCheckin()
                );
                responseList.add(employeeResponse);
            }
        }
        return responseList;
    }

    @Override
    public List<EmployeeResponse> findAllSortByName() {
//        List<Employee> list = employeeRepository.findAll();
        List<Employee> list = employeeRepository.findAllSortedByName();
        List<EmployeeResponse> responseList = new ArrayList<>();
        for (Employee employee : list) {
            EmployeeResponse employeeResponse = new EmployeeResponse(
                    employee.getId(),
                    employee.getEmail(),
                    employee.getPassword(),
                    employee.getRole(),
                    employee.getName(),
                    employee.getGender(),
                    employee.getAddress(),
                    employee.getDob(),
                    employee.getPhone(),
                    employee.getPosition(),
                    employee.getCheckin()
            );
            responseList.add(employeeResponse);
        }
        return responseList;
    }

    @Override
    public List<checkinout> employeeCheckinoutDefault(Long id) {
        List<checkinout> list = employeeRepository.employeeCheckinoutDefault(id);
        return list;
    }

    @Override
    public List<listCheckinout> listEmployeeCheckinoutDefault() {
        List<listCheckinout> list = employeeRepository.listEmployeeCheckinoutDefault();
        return list;
    }

    @Override
    public List<checkinout> employeeCheckinoutInMonth(int year, int month, Long id) {
        List<checkinout> list = employeeRepository.employeeCheckinoutInMonth(year, month, id);
        return list;
    }

    @Override
    public List<listCheckinout> listEmployeeCheckinoutInMonth(int year, int month) {
        List<listCheckinout> list = employeeRepository.listEmployeeCheckinoutInMonth(year, month);
        return list;
    }

    @Override
    public List<checkinout> employeeCheckinoutInMonthError(int year, int month, Long id) {
        List<checkinout> list = employeeRepository.employeeCheckinoutInMonthError(year, month, id);
        return list;
    }

    @Override
    public List<listCheckinout> listEmployeeCheckinoutInMonthError(int year, int month) {
        List<listCheckinout> list = employeeRepository.listEmployeeCheckinoutInMonthError(year, month);
        return list;
    }
}
