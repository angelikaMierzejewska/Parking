package com.parking.Parking.repository;

import com.parking.Parking.model.Transaction;
import com.parking.Parking.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionsRepository  extends CrudRepository<Transaction, Long > {
}
