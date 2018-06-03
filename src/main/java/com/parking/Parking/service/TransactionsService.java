package com.parking.Parking.service;

import com.parking.Parking.model.Transaction;

import com.parking.Parking.repository.TransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionsService {
    @Autowired
    TransactionsRepository transactionsRepository;

    public void findAll(){
        transactionsRepository.findAll();
    }

    public List<Transaction> findAllList(){
        return (List<Transaction>)transactionsRepository.findAll();

    }

    public void createTransaction(Transaction transaction){
        transactionsRepository.save(transaction);
    }

    public Map<LocalDate, BigDecimal> dailyIncome(){

        Map<LocalDate, BigDecimal> income = new HashMap<>();
        List<Transaction> transaction = (List<Transaction>)transactionsRepository.findAll();
        BigDecimal sum = BigDecimal.ZERO;

        for (Transaction d: transaction){
                LocalDate key =  d.getDate();
                if(income.containsKey(key)){
                    BigDecimal cost = income.get(key).add(d.getCost());
                    income.put(d.getDate(), cost);
                }else {
                    income.put(d.getDate(), d.getCost());
                }
        }
        return income;
    }


}
