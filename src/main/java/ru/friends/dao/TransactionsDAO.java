package ru.friends.dao;

import ru.friends.model.Transaction;

import java.util.List;

public interface TransactionsDAO {

    Transaction getById(int id);
    void save(Transaction transaction);
    List<Transaction> getAll();
    List<Transaction> getTransactions(int userId);

}
