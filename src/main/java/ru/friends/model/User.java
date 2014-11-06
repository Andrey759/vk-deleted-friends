package ru.friends.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

@Entity
@Table(name = "users", schema = "", catalog = "vk_friends")
public class User {
    private int id;
    private String pass;
    private Calendar interval;
    private Calendar lastupdate;
    private List<Transaction> transactions;

    public User() { }

    public User(int id, String pass, Calendar interval) {
        this.id = id;
        this.pass = pass;
        this.interval = interval;
    }

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "pass", nullable = false, insertable = true, updatable = true, length = 32)
    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "`interval`", nullable = true, insertable = true, updatable = true)
    public Calendar getInterval() {
        return interval;
    }

    public void setInterval(Calendar interval) {
        this.interval = interval;
    }

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lastupdate", nullable = true, insertable = true, updatable = true)
    public Calendar getLastUpdate() {
        return lastupdate;
    }

    public void setLastUpdate(Calendar lastUpdate) {
        this.lastupdate = lastUpdate;
    }

    @OneToMany(mappedBy = "owner")
    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User that = (User) o;

        if (id != that.id) return false;
        if (interval != null ? !interval.equals(that.interval) : that.interval != null) return false;
        if (pass != null ? !pass.equals(that.pass) : that.pass != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (pass != null ? pass.hashCode() : 0);
        result = 31 * result + (interval != null ? interval.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "id" + id +
               //"   interval " + (interval.getTime() / (60 * 1000)) +
               "   interval " + interval.getTimeInMillis() +
               "   lastupdate " + lastupdate;
    }
}
