import java.sql.SQLException;

public class start {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        RequestManager.getInstance().run();
        //RequestManager.getInstance().getDeletedFriends();
    }

}
