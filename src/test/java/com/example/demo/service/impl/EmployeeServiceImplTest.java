package com.example.demo.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.config.JwtGenerator;
import com.example.demo.dto.request.EmployeeRequest;
import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.response.EmployeeResponse;
import com.example.demo.dto.response.LoginResponse;
import com.example.demo.dto.response.checkinout;
import com.example.demo.dto.response.listCheckinout;
import com.example.demo.entity.Employee;
import com.example.demo.entity.Role;
import com.example.demo.exception.EmailExistedException;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.catalina.connector.Response;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {EmployeeServiceImpl.class})
@ExtendWith(SpringExtension.class)
class EmployeeServiceImplTest {
    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private EmailService emailService;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeServiceImpl employeeServiceImpl;

    @MockBean
    private JwtGenerator jwtGenerator;

    @MockBean
    private PasswordEncoder passwordEncoder;

    /**
     * Method under test: {@link EmployeeServiceImpl#createEmployee(EmployeeRequest)}
     */
    @Test
    void testCreateEmployee() {
        when(employeeRepository.existsByEmail(Mockito.<String>any())).thenReturn(true);
        assertThrows(EmailExistedException.class,
                () -> employeeServiceImpl.createEmployee(new EmployeeRequest("jane.doe@example.org", "iloveyou", Role.ADMIN,
                        "Name", "Gender", "42 Main St", "Dob", "6625550144", "Position")));
        verify(employeeRepository).existsByEmail(Mockito.<String>any());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#createEmployee(EmployeeRequest)}
     */
    @Test
    void testCreateEmployee2() {
        when(employeeRepository.existsByEmail(Mockito.<String>any()))
                .thenThrow(new UsernameNotFoundException("kimoanhh0103@gmail.com"));
        assertThrows(UsernameNotFoundException.class,
                () -> employeeServiceImpl.createEmployee(new EmployeeRequest("jane.doe@example.org", "iloveyou", Role.ADMIN,
                        "Name", "Gender", "42 Main St", "Dob", "6625550144", "Position")));
        verify(employeeRepository).existsByEmail(Mockito.<String>any());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#login(LoginRequest)}
     */
    @Test
    void testLogin() throws AuthenticationException {
        Employee employee = new Employee();
        employee.setAddress("42 Main St");
        employee.setCheckin(1);
        employee.setDob("Dob");
        employee.setEmail("jane.doe@example.org");
        employee.setGender("Gender");
        employee.setId(1L);
        employee.setName("Name");
        employee.setPassword("iloveyou");
        employee.setPhone("6625550144");
        employee.setPosition("Position");
        employee.setRole(Role.ADMIN);
        Optional<Employee> ofResult = Optional.of(employee);
        when(employeeRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        when(jwtGenerator.generatorToken(Mockito.<Authentication>any())).thenReturn("ABC123");
        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        LoginResponse actualLoginResult = employeeServiceImpl.login(new LoginRequest("jane.doe@example.org", "iloveyou"));
        assertEquals("ABC123", actualLoginResult.getAccessToken());
        assertEquals("Bearer ", actualLoginResult.getTokenType());
        verify(employeeRepository).findByEmail(Mockito.<String>any());
        verify(jwtGenerator).generatorToken(Mockito.<Authentication>any());
        verify(authenticationManager).authenticate(Mockito.<Authentication>any());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#login(LoginRequest)}
     */
    @Test
    void testLogin2() throws AuthenticationException {
        Employee employee = new Employee();
        employee.setAddress("42 Main St");
        employee.setCheckin(1);
        employee.setDob("Dob");
        employee.setEmail("jane.doe@example.org");
        employee.setGender("Gender");
        employee.setId(1L);
        employee.setName("Name");
        employee.setPassword("iloveyou");
        employee.setPhone("6625550144");
        employee.setPosition("Position");
        employee.setRole(Role.ADMIN);
        Optional<Employee> ofResult = Optional.of(employee);
        when(employeeRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                .thenThrow(new EmailExistedException("Bearer "));
        assertThrows(EmailExistedException.class,
                () -> employeeServiceImpl.login(new LoginRequest("jane.doe@example.org", "iloveyou")));
        verify(employeeRepository).findByEmail(Mockito.<String>any());
        verify(authenticationManager).authenticate(Mockito.<Authentication>any());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#login(LoginRequest)}
     */
    @Test
    void testLogin3() {
        when(employeeRepository.findByEmail(Mockito.<String>any())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class,
                () -> employeeServiceImpl.login(new LoginRequest("jane.doe@example.org", "iloveyou")));
        verify(employeeRepository).findByEmail(Mockito.<String>any());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#login(LoginRequest)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testLogin4() throws AuthenticationException {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "com.example.demo.dto.request.LoginRequest.getEmail()" because "loginRequest" is null
        //       at com.example.demo.service.impl.EmployeeServiceImpl.login(EmployeeServiceImpl.java:84)
        //   See https://diff.blue/R013 to resolve this issue.

        Employee employee = new Employee();
        employee.setAddress("42 Main St");
        employee.setCheckin(1);
        employee.setDob("Dob");
        employee.setEmail("jane.doe@example.org");
        employee.setGender("Gender");
        employee.setId(1L);
        employee.setName("Name");
        employee.setPassword("iloveyou");
        employee.setPhone("6625550144");
        employee.setPosition("Position");
        employee.setRole(Role.ADMIN);
        Optional<Employee> ofResult = Optional.of(employee);
        when(employeeRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        when(jwtGenerator.generatorToken(Mockito.<Authentication>any())).thenReturn("ABC123");
        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        employeeServiceImpl.login(null);
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#logout(HttpServletRequest, HttpServletResponse)}
     */
    @Test
    void testLogout() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        assertEquals("Logout sucess", employeeServiceImpl.logout(request, new Response()));
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#logout(HttpServletRequest, HttpServletResponse)}
     */
    @Test
    void testLogout2() {
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        assertEquals("Logout sucess", employeeServiceImpl.logout(request, new Response()));
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#updateEmployee(EmployeeRequest, Long)}
     */
    @Test
    void testUpdateEmployee() {
        Employee employee = new Employee();
        employee.setAddress("42 Main St");
        employee.setCheckin(1);
        employee.setDob("Dob");
        employee.setEmail("jane.doe@example.org");
        employee.setGender("Gender");
        employee.setId(1L);
        employee.setName("Name");
        employee.setPassword("iloveyou");
        employee.setPhone("6625550144");
        employee.setPosition("Position");
        employee.setRole(Role.ADMIN);
        Optional<Employee> ofResult = Optional.of(employee);

        Employee employee2 = new Employee();
        employee2.setAddress("42 Main St");
        employee2.setCheckin(1);
        employee2.setDob("Dob");
        employee2.setEmail("jane.doe@example.org");
        employee2.setGender("Gender");
        employee2.setId(1L);
        employee2.setName("Name");
        employee2.setPassword("iloveyou");
        employee2.setPhone("6625550144");
        employee2.setPosition("Position");
        employee2.setRole(Role.ADMIN);
        when(employeeRepository.save(Mockito.<Employee>any())).thenReturn(employee2);
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        employeeServiceImpl.updateEmployee(new EmployeeRequest("jane.doe@example.org", "iloveyou", Role.ADMIN, "Name",
                "Gender", "42 Main St", "Dob", "6625550144", "Position"), 1L);
        verify(employeeRepository).save(Mockito.<Employee>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#updateEmployee(EmployeeRequest, Long)}
     */
    @Test
    void testUpdateEmployee2() {
        Employee employee = new Employee();
        employee.setAddress("42 Main St");
        employee.setCheckin(1);
        employee.setDob("Dob");
        employee.setEmail("jane.doe@example.org");
        employee.setGender("Gender");
        employee.setId(1L);
        employee.setName("Name");
        employee.setPassword("iloveyou");
        employee.setPhone("6625550144");
        employee.setPosition("Position");
        employee.setRole(Role.ADMIN);
        Optional<Employee> ofResult = Optional.of(employee);
        when(employeeRepository.save(Mockito.<Employee>any())).thenThrow(new EmailExistedException("Messsage"));
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(EmailExistedException.class,
                () -> employeeServiceImpl.updateEmployee(new EmployeeRequest("jane.doe@example.org", "iloveyou", Role.ADMIN,
                        "Name", "Gender", "42 Main St", "Dob", "6625550144", "Position"), 1L));
        verify(employeeRepository).save(Mockito.<Employee>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#updateEmployee(EmployeeRequest, Long)}
     */
    @Test
    void testUpdateEmployee3() {
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class,
                () -> employeeServiceImpl.updateEmployee(new EmployeeRequest("jane.doe@example.org", "iloveyou", Role.ADMIN,
                        "Name", "Gender", "42 Main St", "Dob", "6625550144", "Position"), 1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#updateEmployee(EmployeeRequest, Long)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testUpdateEmployee4() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "com.example.demo.dto.request.EmployeeRequest.getRole()" because "employeeRequest" is null
        //       at com.example.demo.service.impl.EmployeeServiceImpl.updateEmployee(EmployeeServiceImpl.java:112)
        //   See https://diff.blue/R013 to resolve this issue.

        Employee employee = new Employee();
        employee.setAddress("42 Main St");
        employee.setCheckin(1);
        employee.setDob("Dob");
        employee.setEmail("jane.doe@example.org");
        employee.setGender("Gender");
        employee.setId(1L);
        employee.setName("Name");
        employee.setPassword("iloveyou");
        employee.setPhone("6625550144");
        employee.setPosition("Position");
        employee.setRole(Role.ADMIN);
        Optional<Employee> ofResult = Optional.of(employee);

        Employee employee2 = new Employee();
        employee2.setAddress("42 Main St");
        employee2.setCheckin(1);
        employee2.setDob("Dob");
        employee2.setEmail("jane.doe@example.org");
        employee2.setGender("Gender");
        employee2.setId(1L);
        employee2.setName("Name");
        employee2.setPassword("iloveyou");
        employee2.setPhone("6625550144");
        employee2.setPosition("Position");
        employee2.setRole(Role.ADMIN);
        when(employeeRepository.save(Mockito.<Employee>any())).thenReturn(employee2);
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        employeeServiceImpl.updateEmployee(null, 1L);
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#getById(Long)}
     */
    @Test
    void testGetById() {
        Employee employee = new Employee();
        employee.setAddress("42 Main St");
        employee.setCheckin(1);
        employee.setDob("Dob");
        employee.setEmail("jane.doe@example.org");
        employee.setGender("Gender");
        employee.setId(1L);
        employee.setName("Name");
        employee.setPassword("iloveyou");
        employee.setPhone("6625550144");
        employee.setPosition("Position");
        employee.setRole(Role.ADMIN);
        Optional<Employee> ofResult = Optional.of(employee);
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        EmployeeResponse actualById = employeeServiceImpl.getById(1L);
        assertEquals("42 Main St", actualById.getAddress());
        assertEquals(Role.ADMIN, actualById.getRole());
        assertEquals("Position", actualById.getPosition());
        assertEquals("6625550144", actualById.getPhone());
        assertEquals("iloveyou", actualById.getPassword());
        assertEquals("Name", actualById.getName());
        assertEquals(1L, actualById.getId().longValue());
        assertEquals("Gender", actualById.getGender());
        assertEquals("jane.doe@example.org", actualById.getEmail());
        assertEquals("Dob", actualById.getDob());
        assertEquals(1, actualById.getCheckin());
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#getById(Long)}
     */
    @Test
    void testGetById2() {
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> employeeServiceImpl.getById(1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#getById(Long)}
     */
    @Test
    void testGetById3() {
        when(employeeRepository.findById(Mockito.<Long>any()))
                .thenThrow(new EmailExistedException("Khong tim thay nhan vien"));
        assertThrows(EmailExistedException.class, () -> employeeServiceImpl.getById(1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#getAllEmployee()}
     */
    @Test
    void testGetAllEmployee() {
        when(employeeRepository.findAll()).thenReturn(new ArrayList<>());
        assertTrue(employeeServiceImpl.getAllEmployee().isEmpty());
        verify(employeeRepository).findAll();
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#getAllEmployee()}
     */
    @Test
    void testGetAllEmployee2() {
        Employee employee = new Employee();
        employee.setAddress("42 Main St");
        employee.setCheckin(1);
        employee.setDob("Dob");
        employee.setEmail("jane.doe@example.org");
        employee.setGender("Gender");
        employee.setId(1L);
        employee.setName("Name");
        employee.setPassword("iloveyou");
        employee.setPhone("6625550144");
        employee.setPosition("Position");
        employee.setRole(Role.ADMIN);

        ArrayList<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee);
        when(employeeRepository.findAll()).thenReturn(employeeList);
        assertEquals(1, employeeServiceImpl.getAllEmployee().size());
        verify(employeeRepository).findAll();
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#getAllEmployee()}
     */
    @Test
    void testGetAllEmployee3() {
        when(employeeRepository.findAll()).thenThrow(new EmailExistedException("Messsage"));
        assertThrows(EmailExistedException.class, () -> employeeServiceImpl.getAllEmployee());
        verify(employeeRepository).findAll();
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#deleteEmployee(Long)}
     */
    @Test
    void testDeleteEmployee() {
        doNothing().when(employeeRepository).deleteById(Mockito.<Long>any());
        employeeServiceImpl.deleteEmployee(1L);
        verify(employeeRepository).deleteById(Mockito.<Long>any());
        assertTrue(employeeServiceImpl.getAllEmployee().isEmpty());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#deleteEmployee(Long)}
     */
    @Test
    void testDeleteEmployee2() {
        doThrow(new EmailExistedException("Messsage")).when(employeeRepository).deleteById(Mockito.<Long>any());
        assertThrows(EmailExistedException.class, () -> employeeServiceImpl.deleteEmployee(1L));
        verify(employeeRepository).deleteById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#findByName(String)}
     */
    @Test
    void testFindByName() {
        when(employeeRepository.findAll()).thenReturn(new ArrayList<>());
        assertTrue(employeeServiceImpl.findByName("Name").isEmpty());
        verify(employeeRepository).findAll();
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#findByName(String)}
     */
    @Test
    void testFindByName2() {
        Employee employee = new Employee();
        employee.setAddress("42 Main St");
        employee.setCheckin(1);
        employee.setDob("Dob");
        employee.setEmail("jane.doe@example.org");
        employee.setGender("Gender");
        employee.setId(1L);
        employee.setName("Name");
        employee.setPassword("iloveyou");
        employee.setPhone("6625550144");
        employee.setPosition("Position");
        employee.setRole(Role.ADMIN);

        ArrayList<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee);
        when(employeeRepository.findAll()).thenReturn(employeeList);
        assertEquals(1, employeeServiceImpl.findByName("Name").size());
        verify(employeeRepository).findAll();
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#findByName(String)}
     */
    @Test
    void testFindByName3() {
        Employee employee = new Employee();
        employee.setAddress("42 Main St");
        employee.setCheckin(1);
        employee.setDob("Dob");
        employee.setEmail("jane.doe@example.org");
        employee.setGender("Gender");
        employee.setId(1L);
        employee.setName("Name");
        employee.setPassword("iloveyou");
        employee.setPhone("6625550144");
        employee.setPosition("Position");
        employee.setRole(Role.ADMIN);

        Employee employee2 = new Employee();
        employee2.setAddress("17 High St");
        employee2.setCheckin(0);
        employee2.setDob("Dob");
        employee2.setEmail("john.smith@example.org");
        employee2.setGender("Gender");
        employee2.setId(2L);
        employee2.setName("com.example.demo.entity.Employee");
        employee2.setPassword("Name");
        employee2.setPhone("8605550118");
        employee2.setPosition("Position");
        employee2.setRole(Role.USER);

        ArrayList<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee2);
        employeeList.add(employee);
        when(employeeRepository.findAll()).thenReturn(employeeList);
        assertEquals(1, employeeServiceImpl.findByName("Name").size());
        verify(employeeRepository).findAll();
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#findByName(String)}
     */
    @Test
    void testFindByName4() {
        when(employeeRepository.findAll()).thenThrow(new EmailExistedException("Messsage"));
        assertThrows(EmailExistedException.class, () -> employeeServiceImpl.findByName("Name"));
        verify(employeeRepository).findAll();
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#findByName(String)}
     */
    @Test
    void testFindByName5() {
        Employee employee = mock(Employee.class);
        when(employee.getId()).thenThrow(new UsernameNotFoundException("Msg"));
        when(employee.getName()).thenReturn("Name");
        doNothing().when(employee).setAddress(Mockito.<String>any());
        doNothing().when(employee).setCheckin(anyInt());
        doNothing().when(employee).setDob(Mockito.<String>any());
        doNothing().when(employee).setEmail(Mockito.<String>any());
        doNothing().when(employee).setGender(Mockito.<String>any());
        doNothing().when(employee).setId(Mockito.<Long>any());
        doNothing().when(employee).setName(Mockito.<String>any());
        doNothing().when(employee).setPassword(Mockito.<String>any());
        doNothing().when(employee).setPhone(Mockito.<String>any());
        doNothing().when(employee).setPosition(Mockito.<String>any());
        doNothing().when(employee).setRole(Mockito.<Role>any());
        employee.setAddress("42 Main St");
        employee.setCheckin(1);
        employee.setDob("Dob");
        employee.setEmail("jane.doe@example.org");
        employee.setGender("Gender");
        employee.setId(1L);
        employee.setName("Name");
        employee.setPassword("iloveyou");
        employee.setPhone("6625550144");
        employee.setPosition("Position");
        employee.setRole(Role.ADMIN);

        ArrayList<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee);
        when(employeeRepository.findAll()).thenReturn(employeeList);
        assertThrows(UsernameNotFoundException.class, () -> employeeServiceImpl.findByName("Name"));
        verify(employeeRepository).findAll();
        verify(employee).getId();
        verify(employee).getName();
        verify(employee).setAddress(Mockito.<String>any());
        verify(employee).setCheckin(anyInt());
        verify(employee).setDob(Mockito.<String>any());
        verify(employee).setEmail(Mockito.<String>any());
        verify(employee).setGender(Mockito.<String>any());
        verify(employee).setId(Mockito.<Long>any());
        verify(employee).setName(Mockito.<String>any());
        verify(employee).setPassword(Mockito.<String>any());
        verify(employee).setPhone(Mockito.<String>any());
        verify(employee).setPosition(Mockito.<String>any());
        verify(employee).setRole(Mockito.<Role>any());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#findAllSortByName()}
     */
    @Test
    void testFindAllSortByName() {
        when(employeeRepository.findAllSortedByName()).thenReturn(new ArrayList<>());
        assertTrue(employeeServiceImpl.findAllSortByName().isEmpty());
        verify(employeeRepository).findAllSortedByName();
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#findAllSortByName()}
     */
    @Test
    void testFindAllSortByName2() {
        Employee employee = new Employee();
        employee.setAddress("42 Main St");
        employee.setCheckin(1);
        employee.setDob("Dob");
        employee.setEmail("jane.doe@example.org");
        employee.setGender("Gender");
        employee.setId(1L);
        employee.setName("Name");
        employee.setPassword("iloveyou");
        employee.setPhone("6625550144");
        employee.setPosition("Position");
        employee.setRole(Role.ADMIN);

        ArrayList<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee);
        when(employeeRepository.findAllSortedByName()).thenReturn(employeeList);
        assertEquals(1, employeeServiceImpl.findAllSortByName().size());
        verify(employeeRepository).findAllSortedByName();
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#findAllSortByName()}
     */
    @Test
    void testFindAllSortByName3() {
        when(employeeRepository.findAllSortedByName()).thenThrow(new EmailExistedException("Messsage"));
        assertThrows(EmailExistedException.class, () -> employeeServiceImpl.findAllSortByName());
        verify(employeeRepository).findAllSortedByName();
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#employeeCheckinoutDefault(Long)}
     */
    @Test
    void testEmployeeCheckinoutDefault() {
        ArrayList<checkinout> checkinoutList = new ArrayList<>();
        when(employeeRepository.employeeCheckinoutDefault(Mockito.<Long>any())).thenReturn(checkinoutList);
        List<checkinout> actualEmployeeCheckinoutDefaultResult = employeeServiceImpl.employeeCheckinoutDefault(1L);
        assertSame(checkinoutList, actualEmployeeCheckinoutDefaultResult);
        assertTrue(actualEmployeeCheckinoutDefaultResult.isEmpty());
        verify(employeeRepository).employeeCheckinoutDefault(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#employeeCheckinoutDefault(Long)}
     */
    @Test
    void testEmployeeCheckinoutDefault2() {
        when(employeeRepository.employeeCheckinoutDefault(Mockito.<Long>any()))
                .thenThrow(new EmailExistedException("Messsage"));
        assertThrows(EmailExistedException.class, () -> employeeServiceImpl.employeeCheckinoutDefault(1L));
        verify(employeeRepository).employeeCheckinoutDefault(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#listEmployeeCheckinoutDefault()}
     */
    @Test
    void testListEmployeeCheckinoutDefault() {
        ArrayList<listCheckinout> listCheckinoutList = new ArrayList<>();
        when(employeeRepository.listEmployeeCheckinoutDefault()).thenReturn(listCheckinoutList);
        List<listCheckinout> actualListEmployeeCheckinoutDefaultResult = employeeServiceImpl
                .listEmployeeCheckinoutDefault();
        assertSame(listCheckinoutList, actualListEmployeeCheckinoutDefaultResult);
        assertTrue(actualListEmployeeCheckinoutDefaultResult.isEmpty());
        verify(employeeRepository).listEmployeeCheckinoutDefault();
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#listEmployeeCheckinoutDefault()}
     */
    @Test
    void testListEmployeeCheckinoutDefault2() {
        when(employeeRepository.listEmployeeCheckinoutDefault()).thenThrow(new EmailExistedException("Messsage"));
        assertThrows(EmailExistedException.class, () -> employeeServiceImpl.listEmployeeCheckinoutDefault());
        verify(employeeRepository).listEmployeeCheckinoutDefault();
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#employeeCheckinoutInMonth(int, int, Long)}
     */
    @Test
    void testEmployeeCheckinoutInMonth() {
        ArrayList<checkinout> checkinoutList = new ArrayList<>();
        when(employeeRepository.employeeCheckinoutInMonth(anyInt(), anyInt(), Mockito.<Long>any()))
                .thenReturn(checkinoutList);
        List<checkinout> actualEmployeeCheckinoutInMonthResult = employeeServiceImpl.employeeCheckinoutInMonth(1, 1, 1L);
        assertSame(checkinoutList, actualEmployeeCheckinoutInMonthResult);
        assertTrue(actualEmployeeCheckinoutInMonthResult.isEmpty());
        verify(employeeRepository).employeeCheckinoutInMonth(anyInt(), anyInt(), Mockito.<Long>any());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#employeeCheckinoutInMonth(int, int, Long)}
     */
    @Test
    void testEmployeeCheckinoutInMonth2() {
        when(employeeRepository.employeeCheckinoutInMonth(anyInt(), anyInt(), Mockito.<Long>any()))
                .thenThrow(new EmailExistedException("Messsage"));
        assertThrows(EmailExistedException.class, () -> employeeServiceImpl.employeeCheckinoutInMonth(1, 1, 1L));
        verify(employeeRepository).employeeCheckinoutInMonth(anyInt(), anyInt(), Mockito.<Long>any());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#listEmployeeCheckinoutInMonth(int, int)}
     */
    @Test
    void testListEmployeeCheckinoutInMonth() {
        ArrayList<listCheckinout> listCheckinoutList = new ArrayList<>();
        when(employeeRepository.listEmployeeCheckinoutInMonth(anyInt(), anyInt())).thenReturn(listCheckinoutList);
        List<listCheckinout> actualListEmployeeCheckinoutInMonthResult = employeeServiceImpl
                .listEmployeeCheckinoutInMonth(1, 1);
        assertSame(listCheckinoutList, actualListEmployeeCheckinoutInMonthResult);
        assertTrue(actualListEmployeeCheckinoutInMonthResult.isEmpty());
        verify(employeeRepository).listEmployeeCheckinoutInMonth(anyInt(), anyInt());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#listEmployeeCheckinoutInMonth(int, int)}
     */
    @Test
    void testListEmployeeCheckinoutInMonth2() {
        when(employeeRepository.listEmployeeCheckinoutInMonth(anyInt(), anyInt()))
                .thenThrow(new EmailExistedException("Messsage"));
        assertThrows(EmailExistedException.class, () -> employeeServiceImpl.listEmployeeCheckinoutInMonth(1, 1));
        verify(employeeRepository).listEmployeeCheckinoutInMonth(anyInt(), anyInt());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#employeeCheckinoutInMonthError(int, int, Long)}
     */
    @Test
    void testEmployeeCheckinoutInMonthError() {
        ArrayList<checkinout> checkinoutList = new ArrayList<>();
        when(employeeRepository.employeeCheckinoutInMonthError(anyInt(), anyInt(), Mockito.<Long>any()))
                .thenReturn(checkinoutList);
        List<checkinout> actualEmployeeCheckinoutInMonthErrorResult = employeeServiceImpl
                .employeeCheckinoutInMonthError(1, 1, 1L);
        assertSame(checkinoutList, actualEmployeeCheckinoutInMonthErrorResult);
        assertTrue(actualEmployeeCheckinoutInMonthErrorResult.isEmpty());
        verify(employeeRepository).employeeCheckinoutInMonthError(anyInt(), anyInt(), Mockito.<Long>any());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#employeeCheckinoutInMonthError(int, int, Long)}
     */
    @Test
    void testEmployeeCheckinoutInMonthError2() {
        when(employeeRepository.employeeCheckinoutInMonthError(anyInt(), anyInt(), Mockito.<Long>any()))
                .thenThrow(new EmailExistedException("Messsage"));
        assertThrows(EmailExistedException.class, () -> employeeServiceImpl.employeeCheckinoutInMonthError(1, 1, 1L));
        verify(employeeRepository).employeeCheckinoutInMonthError(anyInt(), anyInt(), Mockito.<Long>any());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#listEmployeeCheckinoutInMonthError(int, int)}
     */
    @Test
    void testListEmployeeCheckinoutInMonthError() {
        ArrayList<listCheckinout> listCheckinoutList = new ArrayList<>();
        when(employeeRepository.listEmployeeCheckinoutInMonthError(anyInt(), anyInt())).thenReturn(listCheckinoutList);
        List<listCheckinout> actualListEmployeeCheckinoutInMonthErrorResult = employeeServiceImpl
                .listEmployeeCheckinoutInMonthError(1, 1);
        assertSame(listCheckinoutList, actualListEmployeeCheckinoutInMonthErrorResult);
        assertTrue(actualListEmployeeCheckinoutInMonthErrorResult.isEmpty());
        verify(employeeRepository).listEmployeeCheckinoutInMonthError(anyInt(), anyInt());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#listEmployeeCheckinoutInMonthError(int, int)}
     */
    @Test
    void testListEmployeeCheckinoutInMonthError2() {
        when(employeeRepository.listEmployeeCheckinoutInMonthError(anyInt(), anyInt()))
                .thenThrow(new EmailExistedException("Messsage"));
        assertThrows(EmailExistedException.class, () -> employeeServiceImpl.listEmployeeCheckinoutInMonthError(1, 1));
        verify(employeeRepository).listEmployeeCheckinoutInMonthError(anyInt(), anyInt());
    }
}

