package com.parking.Parking.controller;

import com.parking.Parking.model.Transaction;
import com.parking.Parking.model.User;
import com.parking.Parking.service.TransactionsService;
import com.parking.Parking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
public class TransactionController {

    @Autowired
    TransactionsService transactionsService;
    @Autowired
    UserService userService;

    @RequestMapping("/transactions")
    public String getTransactions(Model model){
        List<Transaction> findAll = transactionsService.findAllList();
        Map<LocalDate, BigDecimal> income=  transactionsService.dailyIncome();
        model.addAttribute("transactions", findAll);
        model.addAttribute("income", income);
        return "transactions";
    }

    @RequestMapping("/parking")
    public String getParkingData(Model model){
        List<User> findAll = userService.findAllDrivers();
        model.addAttribute("drivers", findAll);
        return "parking";
    }

    @RequestMapping("/income")
    public String getIncome(Model model){
        Map<LocalDate, BigDecimal> income=  transactionsService.dailyIncome();
        model.addAttribute("income", income);
        return "income";
    }

    @RequestMapping("/transaction")
    public String getTransaction(Principal principal, Model model){
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("driver", user);
        return "transaction";
    }
}
