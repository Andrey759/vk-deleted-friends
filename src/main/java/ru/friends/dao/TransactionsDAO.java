package ru.friends.dao;

import ru.friends.model.Transactions;

public interface TransactionsDAO {

    public Transactions getById(int id);

    public void save(Transactions transaction);

}
