package com.example.demo.controller;

import com.example.demo.dto.request.CheckinRequest;
import com.example.demo.dto.request.CheckoutRequest;
import com.example.demo.service.CheckinService;
import com.example.demo.service.CheckoutService;
import com.example.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
public class CheckoutController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private CheckoutService checkoutService;
    @PostMapping("/checkout")
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestBody CheckoutRequest checkoutRequest){
        return checkoutService.checkout(checkoutRequest);
    }
}
