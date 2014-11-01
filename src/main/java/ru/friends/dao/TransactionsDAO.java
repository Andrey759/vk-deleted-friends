package ru.friends.dao;

import ru.friends.model.Transaction;

public interface TransactionsDAO {

    Transaction getById(int id);
    void save(Transaction transaction);

}
