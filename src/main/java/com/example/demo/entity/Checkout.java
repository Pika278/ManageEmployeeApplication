package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name = "checkout")
@Data
@AllArgsConstructor
public class Checkout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.DATE)
    private LocalDate date;
    @Temporal(TemporalType.TIME)
    private LocalTime time;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public Checkout(LocalDate date, LocalTime time, Employee employee) {
        this.date = date;
        this.time = time;
        this.employee = employee;
    }
}
