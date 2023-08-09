package com.example.demo.repository;

import com.example.demo.dto.response.checkinout;
import com.example.demo.dto.response.listCheckinout;
import com.example.demo.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    Optional<Employee> findByEmail(String email);
    Boolean existsByEmail(String Email);
//    Optional<Employee> findByCheckin(int checkinNumber);
    Boolean existsByCheckin(int checkin);
    @Query("SELECT e FROM Employee e ORDER BY name")
    List<Employee> findAllSortedByName();

    @Query(value = "SELECT\n" +
            "  all_dates.`ngay` AS date,\n" +
            "  checkin.time AS checkin,\n" +
            "  checkout.time AS checkout\n" +
            "FROM (\n" +
            "  SELECT DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY) + INTERVAL t.n DAY AS ngay\n" +
            "  FROM (SELECT 0 n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6) t\n" +
            "  WHERE DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY) + INTERVAL t.n DAY < CURDATE()\n" +
            ") AS all_dates\n" +
            "LEFT JOIN checkin ON DATE(checkin.date) = all_dates.ngay AND checkin.employee_id = :id\n" +
            "LEFT JOIN checkout ON DATE(checkout.date) = all_dates.ngay AND checkout.employee_id = :id", nativeQuery = true)
    List<checkinout> employeeCheckinoutDefault(@Param("id") Long id);
    @Query(value = "SELECT\n" +
            "  all_dates.`ngay` AS date,\n" +
            "  employee.id,\n" +
            "  employee.name,\n" +
            "  employee.position,\n" +
            "  checkin.time AS checkin,\n" +
            "  checkout.time AS checkout\n" +
            "FROM (\n" +
            "  SELECT DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY) + INTERVAL t.n DAY AS ngay\n" +
            "  FROM (SELECT 0 n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6) t\n" +
            "  WHERE DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY) + INTERVAL t.n DAY < CURDATE()\n" +
            "  AND WEEKDAY(DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY) + INTERVAL t.n DAY) != 6\n" +
            ") AS all_dates\n" +
            "cross join employee\n" +
            "LEFT JOIN checkin ON DATE(checkin.date) = all_dates.ngay and checkin.employee_id = employee.id\n" +
            "LEFT JOIN checkout ON DATE(checkout.date) = all_dates.ngay and checkout.employee_id = employee.id\n" +
            "ORDER BY date",
     nativeQuery = true)
    List<listCheckinout> listEmployeeCheckinoutDefault();
    @Query(value = "SELECT\n" +
            "  all_dates.`ngay` AS date,\n" +
            "  checkin.time AS checkin,\n" +
            "  checkout.time AS checkout\n" +
            "FROM (\n" +
            "  SELECT DATE_FORMAT(CONCAT(:year,'-',:month,'-01'), '%Y-%m-01') + INTERVAL t.n DAY AS ngay\n" +
            "  FROM (SELECT 0 n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 \n" +
            "  UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12 UNION ALL SELECT 13 UNION ALL SELECT 14 \n" +
            "  UNION ALL SELECT 15 UNION ALL SELECT 16 UNION ALL SELECT 17 UNION ALL SELECT 18 UNION ALL SELECT 19 UNION ALL SELECT 20 UNION ALL SELECT 21 \n" +
            "  UNION ALL SELECT 22 UNION ALL SELECT 23 UNION ALL SELECT 24 UNION ALL SELECT 25 UNION ALL SELECT 26 UNION ALL SELECT 27\n" +
            "  UNION ALL SELECT 28 UNION ALL SELECT 29 UNION ALL SELECT 30) t\n" +
            "  WHERE DATE_FORMAT(CONCAT(:year,'-',:month,'-01'), '%Y-%m-01') + INTERVAL t.n DAY <= LAST_DAY(CURDATE())\n" +
            "  AND WEEKDAY(DATE_FORMAT(CONCAT(:year,'-',:month,'-01'), '%Y-%m-01') + INTERVAL t.n DAY) != 6" +
            ") AS all_dates\n" +
            "LEFT JOIN checkin ON DATE(checkin.date) = all_dates.ngay AND checkin.employee_id = :id\n" +
            "LEFT JOIN checkout ON DATE(checkout.date) = all_dates.ngay AND checkout.employee_id = :id",
    nativeQuery = true)
    List<checkinout> employeeCheckinoutInMonth(@Param("year") int year, @Param("month") int month, @Param("id") Long id);
    @Query(value = "SELECT\n" +
            "  all_dates.`ngay` AS date,\n" +
            "  employee.id,\n" +
            "  employee.name,\n" +
            "  checkin.time AS checkin,\n" +
            "  checkout.time AS checkout\n" +
            "FROM (\n" +
            "  SELECT DATE_FORMAT(CONCAT(:year,'-',:month,'-01'), '%Y-%m-01') + INTERVAL t.n DAY AS ngay\n" +
            "  FROM (SELECT 0 n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 \n" +
            "  UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12 UNION ALL SELECT 13 UNION ALL SELECT 14 \n" +
            "  UNION ALL SELECT 15 UNION ALL SELECT 16 UNION ALL SELECT 17 UNION ALL SELECT 18 UNION ALL SELECT 19 UNION ALL SELECT 20 UNION ALL SELECT 21 \n" +
            "  UNION ALL SELECT 22 UNION ALL SELECT 23 UNION ALL SELECT 24 UNION ALL SELECT 25 UNION ALL SELECT 26 UNION ALL SELECT 27\n" +
            "  UNION ALL SELECT 28 UNION ALL SELECT 29 UNION ALL SELECT 30) t\n" +
            "  WHERE DATE_FORMAT(CONCAT(:year,'-',:month,'-01'), '%Y-%m-01') + INTERVAL t.n DAY <= LAST_DAY(CURDATE())\n" +
            "  AND WEEKDAY(DATE_FORMAT(CONCAT(:year,'-',:month,'-01'), '%Y-%m-01') + INTERVAL t.n DAY) != 6\n" +
            ") AS all_dates\n" +
            "CROSS JOIN employee\n" +
            "LEFT JOIN checkin ON DATE(checkin.date) = all_dates.ngay AND checkin.employee_id = employee.id\n" +
            "LEFT JOIN checkout ON DATE(checkout.date) = all_dates.ngay AND checkout.employee_id = employee.id",
    nativeQuery = true)
    List<listCheckinout> listEmployeeCheckinoutInMonth(@Param("year") int year, @Param("month") int month);
    @Query(value = "SELECT\n" +
            "  all_dates.`ngay` AS date,\n" +
            "  checkin.time AS checkin,\n" +
            "  checkout.time AS checkout\n" +
            "FROM (\n" +
            "  SELECT DATE_FORMAT(CONCAT(:year,'-',:month,'-01'), '%Y-%m-01') + INTERVAL t.n DAY AS ngay\n" +
            "  FROM (SELECT 0 n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 \n" +
            "  UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12 UNION ALL SELECT 13 UNION ALL SELECT 14 \n" +
            "  UNION ALL SELECT 15 UNION ALL SELECT 16 UNION ALL SELECT 17 UNION ALL SELECT 18 UNION ALL SELECT 19 UNION ALL SELECT 20 UNION ALL SELECT 21 \n" +
            "  UNION ALL SELECT 22 UNION ALL SELECT 23 UNION ALL SELECT 24 UNION ALL SELECT 25 UNION ALL SELECT 26 UNION ALL SELECT 27\n" +
            "  UNION ALL SELECT 28 UNION ALL SELECT 29 UNION ALL SELECT 30) t\n" +
            "  WHERE DATE_FORMAT(CONCAT(:year,'-',:month,'-01'), '%Y-%m-01') + INTERVAL t.n DAY <= LAST_DAY(CURDATE())\n" +
            "  AND WEEKDAY(DATE_FORMAT(CONCAT(:year,'-',:month,'-01'), '%Y-%m-01') + INTERVAL t.n DAY) != 6" +
            ") AS all_dates\n" +
            "LEFT JOIN checkin ON DATE(checkin.date) = all_dates.ngay AND checkin.employee_id = :id\n" +
            "LEFT JOIN checkout ON DATE(checkout.date) = all_dates.ngay AND checkout.employee_id = :id " +
            "WHERE (checkin.time > '09:00:00' OR checkout.time < '18:00:00' OR checkin.time is null or checkout.time is null)",
            nativeQuery = true)
    List<checkinout> employeeCheckinoutInMonthError(@Param("year") int year, @Param("month") int month, @Param("id") Long id);
    @Query(value = "SELECT\n" +
            "  all_dates.`ngay` AS date,\n" +
            "  employee.id,\n" +
            "  employee.name,\n" +
            "  checkin.time AS checkin,\n" +
            "  checkout.time AS checkout\n" +
            "FROM (\n" +
            "  SELECT DATE_FORMAT(CONCAT(:year,'-',:month,'-01'), '%Y-%m-01') + INTERVAL t.n DAY AS ngay\n" +
            "  FROM (SELECT 0 n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 \n" +
            "  UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12 UNION ALL SELECT 13 UNION ALL SELECT 14 \n" +
            "  UNION ALL SELECT 15 UNION ALL SELECT 16 UNION ALL SELECT 17 UNION ALL SELECT 18 UNION ALL SELECT 19 UNION ALL SELECT 20 UNION ALL SELECT 21 \n" +
            "  UNION ALL SELECT 22 UNION ALL SELECT 23 UNION ALL SELECT 24 UNION ALL SELECT 25 UNION ALL SELECT 26 UNION ALL SELECT 27\n" +
            "  UNION ALL SELECT 28 UNION ALL SELECT 29 UNION ALL SELECT 30) t\n" +
            "  WHERE DATE_FORMAT(CONCAT(:year,'-',:month,'-01'), '%Y-%m-01') + INTERVAL t.n DAY <= LAST_DAY(CURDATE())\n" +
            "  AND WEEKDAY(DATE_FORMAT(CONCAT(:year,'-',:month,'-01'), '%Y-%m-01') + INTERVAL t.n DAY) != 6\n" +
            ") AS all_dates\n" +
            "CROSS JOIN employee\n" +
            "LEFT JOIN checkin ON DATE(checkin.date) = all_dates.ngay AND checkin.employee_id = employee.id\n" +
            "LEFT JOIN checkout ON DATE(checkout.date) = all_dates.ngay AND checkout.employee_id = employee.id " +
            "WHERE (checkin.time > '09:00:00' OR checkout.time < '18:00:00' OR checkin.time is null or checkout.time is null)",
            nativeQuery = true)
    List<listCheckinout> listEmployeeCheckinoutInMonthError(@Param("year") int year, @Param("month") int month);

}
