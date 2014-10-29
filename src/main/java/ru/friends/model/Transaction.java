package ru.friends.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "transactions", schema = "", catalog = "vk_friends")
public class Transaction {
    private int id;
    private Timestamp dateMin;
    private Timestamp dateMax;
    private byte deleted;
    private Friend friend_id;
    private User owner;

    public Transaction() { }

    public Transaction(int id, Timestamp dateMin, Timestamp dateMax, byte deleted, Friend friend_id, User owner) {
        this.id = id;
        this.dateMin = dateMin;
        this.dateMax = dateMax;
        this.deleted = deleted;
        this.friend_id = friend_id;
        this.owner = owner;
    }

    public Transaction(int id, Timestamp dateMin, Timestamp dateMax, int deleted, Friend friend_id, User owner) {
        this(id, dateMin, dateMax, (byte)deleted, friend_id, owner);
    }

    public Transaction(int id, Timestamp dateMin, Timestamp dateMax, boolean deleted, Friend friend_id, User owner) {
        this(id, dateMin, dateMax, deleted ? (byte)1 : (byte)0, friend_id, owner);
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
    @Column(name = "date_min", nullable = false, insertable = true, updatable = true)
    public Timestamp getDateMin() {
        return dateMin;
    }

    public void setDateMin(Timestamp dateMin) {
        this.dateMin = dateMin;
    }

    @Basic
    @Column(name = "date_max", nullable = false, insertable = true, updatable = true)
    public Timestamp getDateMax() {
        return dateMax;
    }

    public void setDateMax(Timestamp dateMax) {
        this.dateMax = dateMax;
    }

    @Basic
    @Column(name = "deleted", nullable = false, insertable = true, updatable = true)
    public byte getDeleted() {
        return deleted;
    }

    public void setDeleted(byte deleted) {
        this.deleted = deleted;
    }
    public void setDeleted(boolean deleted) {
        setDeleted(deleted ? (byte)1 : (byte)0);
    }
    public void setDeleted(int deleted) {
        setDeleted((byte)deleted);
    }

    //@Column(name = "friend_id", nullable = false, insertable = true, updatable = true)
    @ManyToOne
    public Friend getFriend() {
        return friend_id;
    }

    public void setFriend(Friend friend) {
        this.friend_id = friend;
    }

    @ManyToOne
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (deleted != that.deleted) return false;
        if (id != that.id) return false;
        if (dateMax != null ? !dateMax.equals(that.dateMax) : that.dateMax != null) return false;
        if (dateMin != null ? !dateMin.equals(that.dateMin) : that.dateMin != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (dateMin != null ? dateMin.hashCode() : 0);
        result = 31 * result + (dateMax != null ? dateMax.hashCode() : 0);
        result = 31 * result + (int) deleted;
        return result;
    }

    @Override
    public String toString() {
        return "Transactions{" +
                "id=" + id +
                ", dateMin=" + dateMin +
                ", dateMax=" + dateMax +
                ", deleted=" + deleted +
                ", friend_id=" + friend_id +
                ", owner=" + owner +
                '}';
    }
}
