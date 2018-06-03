package com.parking.Parking.utils;

import com.parking.Parking.model.User;

import java.math.BigDecimal;

public class Cost {

    public static BigDecimal countCost(User user){
        long hours = user.getHours();
        BigDecimal cost = new BigDecimal("0");
        BigDecimal prev = new BigDecimal("1");
        BigDecimal next = new BigDecimal("2");
        BigDecimal sumRegular = new BigDecimal("3");
        BigDecimal sumVip = new BigDecimal("2");

        if(user.getIsVip() == false){
            if(hours <= 1){
                return new BigDecimal("1");
            } else if (hours > 1 && hours <= 2){
                return new BigDecimal("3");
            } else {
                for (long i = 3; i <= hours; i++) {
                    prev = next;
                    next = prev.multiply(new BigDecimal("2"));
                    sumRegular = sumRegular.add(next);
                }
            }
            cost = sumRegular;
        }else{
            if(hours <= 1){
                return new BigDecimal("0");
            } else if (hours > 1 && hours <= 2){
                return new BigDecimal("2");
            } else {
                for (long i = 3; i <= hours; i++) {
                    prev = next;
                    next = prev.multiply(new BigDecimal("1.5"));
                    sumVip = sumVip.add(next);
                }
            }
            cost = sumVip;
        }
        return cost;
    }
}
