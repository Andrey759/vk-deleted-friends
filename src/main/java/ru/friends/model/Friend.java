package ru.friends.model;

import javax.persistence.*;
import java.util.List;

// select * from schema_name.friends

@Entity
@Table(name = "friends", schema = "schema_name", catalog = "vk_friends")
public class Friend {
    private int id;
    private String pic;
    private String page;
    private byte gender;
    private String name;
    private List<Transaction> transactions;

    public Friend() { }

    public Friend(int id, String pic, String page, byte gender, String name) {
        this.id = id;
        this.pic = pic;
        this.page = page;
        this.gender = gender;
        this.name = name;
    }
    public Friend(int id, String pic, String page, Gender gender, String name) {
        this(id, pic, page, gender.getByte(), name);
    }

    public Friend(String id, String pic, String page, String gender, String name) {
        this(Integer.valueOf(id), pic, page, Gender.getGender(gender), name);
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
    public void setGender(Gender gender) {
        setGender(gender.getByte());
    }
    public void setGender(boolean gender) {
        setGender(Gender.getGender(gender));
    }
    public void setGender(int gender) {
        setGender(Gender.getGender(gender));
    }
    public void setGender(String gender) {
        setGender(Gender.getGender(gender));
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

        Friend that = (Friend) o;

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
