package com.example.demo.repository;

import com.example.demo.entity.Checkout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;

@Repository
public interface CheckoutRepository extends JpaRepository<Checkout,Long> {
    @Query(value = "SELECT checkout.time FROM checkout WHERE checkout.date = curdate() and checkout.employee_id = :id",
          nativeQuery = true)
    LocalTime checkCheckedOut(@Param("id") Long id);
}
