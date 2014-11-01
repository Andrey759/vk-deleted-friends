package ru.friends.service;

import java.io.IOException;
import java.util.List;

interface RequestDAO<E> {

    public List<E> getResponseAsList(String page) throws IOException;

}
