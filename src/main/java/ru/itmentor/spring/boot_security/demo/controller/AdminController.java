package ru.itmentor.spring.boot_security.demo.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.itmentor.spring.boot_security.demo.models.User;
import ru.itmentor.spring.boot_security.demo.service.UserService;

import java.io.IOException;
import java.util.List;


@RestController
@ComponentScan("service")
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/", ""})
    public void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/admin/users");
    }

    @GetMapping("/users")
    public List<User> printUsers(){
        return userService.getAllUsers();
    }


    @PutMapping(value = "/users/update/{id}")
    public void changeUser(HttpServletResponse response, @PathVariable("id") Long id, @RequestBody User user) throws IOException {
        user.setId(id);
        userService.updateUser(user);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @DeleteMapping(value = "/users/delete/{id}")
    public void deleteUser(HttpServletResponse response, @PathVariable("id") Long id) throws IOException {
        userService.deleteUserById(id);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @PostMapping(value = "/users/create")
    public void createUser(HttpServletResponse response, @RequestBody User user) throws IOException {
        userService.createUser(user);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
