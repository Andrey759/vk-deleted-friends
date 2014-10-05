
public class User {

    private int id;
    private String pic;
    private String page;
    private Gender gender;
    private String name;

    public enum Gender {
        MALE,
        FEMALE;
    }

    public User(int id, String pic, String page, Gender gender, String name) {
        this.id = id;
        this.pic = pic;
        this.page = page;
        this.gender = gender;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getPic() {
        return pic;
    }

    public String getPage() {
        return page;
    }

    public boolean isFemale() {
        return gender.equals(User.Gender.FEMALE);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + "(id" + id + ") " + gender.name() + " page: vk.com" + page + " pic: " + pic;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof User)
            return id == ((User) obj).getId();
        else
            return false;
    }
}
