package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "checkin")
@Data
@AllArgsConstructor
public class Checkin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.DATE)
    private LocalDate date;
    @Temporal(TemporalType.TIME)
    private LocalTime time;
//    @ManyToMany
//    @JoinTable(name = "checkinEmployee", joinColumns = @JoinColumn(name = "checkin_id", referencedColumnName = "id"),
//                inverseJoinColumns = @JoinColumn(name = "employee_id", referencedColumnName = "id"))
//    private List<Employee> employee;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public Checkin(LocalDate date, LocalTime time, Employee employee) {
        this.date = date;
        this.time = time;
        this.employee = employee;
    }
}
