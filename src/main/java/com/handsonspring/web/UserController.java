package com.handsonspring.web;

import com.handsonspring.model.Role;
import com.handsonspring.model.User;
import com.handsonspring.model.grid.GridEntries;
import com.handsonspring.service.SecurityService;
import com.handsonspring.service.UserService;
import com.handsonspring.service.grid.UserGridColumnsServiceImpl;
import com.handsonspring.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class UserController {

    private final UserService userService;

    private final SecurityService securityService;

    private final UserValidator userValidator;

    private final UserGridColumnsServiceImpl gridColumnsService;


    public static final String[] availableColumnsNames = {"Name", "E-mail", "Password", "Role"};

    @Autowired
    public UserController(UserService userService, SecurityService securityService, UserValidator userValidator, UserGridColumnsServiceImpl gridColumnsService) {
        this.userService = userService;
        this.securityService = securityService;
        this.userValidator = userValidator;
        this.gridColumnsService = gridColumnsService;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {

        model.addAttribute("userForm", new User());

        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userService.save(userForm);

        securityService.autologin(userForm.getUsername(), userForm.getPasswordConfirm());

        return "redirect:/users";
    }


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null) {
            model.addAttribute("error", "Your username and password is invalid.");
        }

        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }

        return "login";
    }

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String welcome(Model model) {

        return "welcome";
    }

    @RequestMapping(value = {"/users"}, method = RequestMethod.GET)
    public String users(Model model) {
        List<User> users = userService.findAll();
        List<Object> usersData = gridColumnsService.getGridValues(users);

        model.addAttribute("colHeaders", availableColumnsNames);
        model.addAttribute("usersData", usersData);

        Role[] roles = Role.class.getEnumConstants();
        model.addAttribute("roles", roles);

        return "users";
    }

    @RequestMapping(value="/saveUser", method=RequestMethod.POST,consumes = "application/json")
    @ResponseBody
    public String save(@RequestBody GridEntries data){

        gridColumnsService.saveEntities(data);
        return "{\"message\": \"Saved\"}";
    }
}
