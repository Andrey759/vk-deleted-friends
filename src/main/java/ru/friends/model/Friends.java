package ru.friends.model;

import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "friends", schema = "", catalog = "vk_friends")
public class Friends {
    private int id;
    private String pic;
    private String page;
    private byte gender;
    private String name;
    private List<Transactions> transactions;

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "pic", nullable = false, insertable = true, updatable = true, length = 256)
    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    @Basic
    @Column(name = "page", nullable = false, insertable = true, updatable = true)
    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    @Basic
    @Column(name = "gender", nullable = false, insertable = true, updatable = true, length = 100)
    public byte getGender() {
        return gender;
    }

    public void setGender(byte gender) {
        this.gender = gender;
    }

    @Basic
    @Column(name = "name", nullable = true, insertable = true, updatable = true, length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //@OneToMany(mappedBy = "friends")
    @OneToMany
    public List<Transactions> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transactions> transactions) {
        this.transactions = transactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Friends that = (Friends) o;

        if (id != that.id) return false;
        if (page != null ? !page.equals(that.page) : that.page != null) return false;
        if (gender != that.gender) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (pic != null ? !pic.equals(that.pic) : that.pic != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (pic != null ? pic.hashCode() : 0);
        result = 31 * result + (page != null ? page.hashCode() : 0);
        result = 31 * result + (int) gender;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Friends{" +
                "id=" + id +
                ", pic='" + pic + '\'' +
                ", page=" + page +
                ", gender='" + gender + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
