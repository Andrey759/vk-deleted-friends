package ru.friends;

import java.util.Calendar;

@Deprecated
public class DelUser extends User {

    private Calendar date;

    public DelUser(int id, String pic, String page, Gender gender, String name, Calendar date) {
        super(id, pic, page, gender, name);
        this.date = date;
    }

    public Calendar getDate() {
        return date;
    }

    @Override
    public String toString() {
        return super.toString() + " date: " + String.format("%tY-%<tm-%<td %<tH:%<tM:%<tS", date);
    }
}
