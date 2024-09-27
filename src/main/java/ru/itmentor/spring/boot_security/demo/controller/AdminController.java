package ru.itmentor.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itmentor.spring.boot_security.demo.models.Role;
import ru.itmentor.spring.boot_security.demo.models.User;
import ru.itmentor.spring.boot_security.demo.service.UserService;

import java.util.stream.Collectors;

@Controller
@ComponentScan("service")
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/", ""})
    public String redirect(){
        return "redirect:/admin/users";
    }

    @GetMapping("/users")
    public String printUsers(ModelMap model){
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("roles",
                userService.getAllUsers().stream()
                        .map(User::getRoles)
                        .map(roles ->
                                roles.stream()
                                .map(Role::getRole)
                                .collect(Collectors.joining(", "))
                            )
                        .collect(Collectors.toList()));

        return "users";
    }

    @GetMapping(value = "/users/update/{id}")
    public String updateUserAdminForm(@PathVariable("id") Long id, ModelMap model){
        User user = userService.getUserById(id);
        model.addAttribute("user",user);
        model.addAttribute("roles",
                user.getRoles().stream()
                .map(Role::getRole)
                .collect(Collectors.joining(", ")));
        return "update_user_admin_form";
    }

    @GetMapping(value = "/users/create")
    public String addUserAdminForm(){
        return "register_user_admin_form";
    }


    @PostMapping(value = "/users/update/{id}")
    public String changeUser(@PathVariable("id") Long id, @ModelAttribute User user) {
        user.setId(id);
        userService.updateUser(user);
        return "redirect:/admin/users";
    }

    @PostMapping(value = "/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id){
        userService.deleteUserById(id);
        return "redirect:/admin/users";
    }

    @PostMapping(value = "/users/create")
    public String createUser(@ModelAttribute User user){
        userService.createUser(user);
        return "redirect:/admin/users";
    }
}
