package com.example.demo.repository;

import com.example.demo.entity.Checkin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;

@Repository
public interface CheckinRepository extends JpaRepository<Checkin,Long> {
    @Query(value = "SELECT checkin.time FROM checkin WHERE checkin.date = curdate() and checkin.employee_id = :id",
    nativeQuery = true)
    LocalTime checkCheckedIn(@Param("id") Long id);
}
