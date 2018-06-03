package com.parking.Parking.controller;

import com.parking.Parking.model.Transaction;
import com.parking.Parking.model.User;
import com.parking.Parking.model.security.UserRole;
import com.parking.Parking.repository.RoleRepository;
import com.parking.Parking.service.TransactionsService;
import com.parking.Parking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TransactionsService transactionService;


    @RequestMapping({"/login", "", "/"})
        public String index() {
        return "login";
        }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signup(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "signup";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signupPost(@ModelAttribute("user") User user, Model model) {

        if (userService.checkUserExists(user.getUsername(), user.getEmail())) {
            if (userService.checkEmailExists(user.getEmail())) {
                model.addAttribute("emailExists", true);
            }
            if (userService.checkUsernameExists(user.getUsername())) {
                model.addAttribute("usernameExists", true);
            }
            return "signup";
        } else {
            Set<UserRole> userRoles = new HashSet<>();
            userRoles.add(new UserRole(user, roleRepository.findByName("ROLE_USER")));
            userService.createUser(user, userRoles);
            return "redirect:/";
        }
    }

    @RequestMapping( "/driver")
    public String getDriverMenu(Principal principal, Model model){
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("driver", user);
        return "driver";
    }

    @PostMapping("driver/start")
    public String start(Principal principal){
        User user = userService.findByUsername(principal.getName());
        userService.startTime(user.getUserId());
        return "redirect:";
    }
    @PostMapping("driver/stop")
    public String stop(Principal principal){
        User user = userService.findByUsername(principal.getName());
        userService.endTime(user.getUserId());
        userService.setHours(user.getUserId());
        userService.setCost(user.getUserId());
        return "redirect:";
    }

    @PostMapping("/driver/pay")
    public String pay(Principal principal){
        User user = userService.findByUsername(principal.getName());
        user.setIsPay(true);
        Transaction transaction = new Transaction();
        transaction.setDate(user.getEndTime().toLocalDate());
        transaction.setCost(user.getCost());
        transaction.setUser(user);
        userService.resetTimes(user);
        transactionService.createTransaction(transaction);
        return "redirect:";
    }

    @RequestMapping("drivers")
    public String getIndexPage(Model model){
        List<User> findAll = userService.findAllDrivers();
        Map<LocalDate, BigDecimal> income=  transactionService.dailyIncome();
        model.addAttribute("drivers", findAll);
        model.addAttribute("income", income);
        return "drivers";
    }
}
