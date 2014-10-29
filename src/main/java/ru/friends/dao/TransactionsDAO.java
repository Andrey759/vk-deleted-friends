package ru.friends.dao;

import ru.friends.model.Transaction;

public interface TransactionsDAO {

    public Transaction getById(int id);

    public void save(Transaction transaction);

}
