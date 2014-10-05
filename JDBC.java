import java.sql.*;
import java.util.*;
import java.util.Date;

public class JDBC {

    private String url = "jdbc:mysql://127.0.0.1:3306/vk_friends";
    private Properties properties = new Properties();
    private String user = "root";
    private String password = "matrix";
    private String tableFrineds = "friends";
    private Connection connection = null;

    public JDBC() {
        properties.put("user", user);
        properties.put("password", password);
        //properties.put("charSet", "UTF-8");
        properties.put("useUnicode", "true");
        properties.put("characterEncoding", "UTF-8");
    }

    public String getTableFrineds() {
        return tableFrineds;
    }

    public void connect() throws ClassNotFoundException, SQLException {
        //Class.forName("org.postgresql.Driver");
        Class.forName("com.mysql.jdbc.Driver");
        //System.out.println("Драйвер подключен");
        connection = DriverManager.getConnection(url, properties);
        //System.out.println("Соединение установлено");
    }

    public ResultSet execute(String sql) throws SQLException, ClassNotFoundException {
        if (connection == null)
            connect();
        Statement statement = connection.createStatement();
        if (sql.toLowerCase().contains("insert") || sql.toLowerCase().contains("update") || sql.toLowerCase().contains("delete")) {
            statement.executeUpdate(sql);
            return null;
        } else
            return statement.executeQuery(sql);
    }

    public List<User> executeUsers(String sql) throws SQLException, ClassNotFoundException {
        ResultSet result = execute(sql);
        List<User> collection = new ArrayList<>(result.getFetchSize());
        while (result.next())
            collection.add(new User(result.getInt("id"), result.getString("pic"), result.getString("page"), result.getBoolean("gender") ? User.Gender.FEMALE : User.Gender.MALE, result.getString("name")));
        return collection;
    }

    public List<DelUser> executeDelUsers(String sql) throws SQLException, ClassNotFoundException {
        ResultSet result = execute(sql);
        List<DelUser> collection = new ArrayList<>(result.getFetchSize());
        while(result.next()) {
            Date date = new Date(result.getDate("del_date").getTime() + result.getTime("del_date").getTime());
            date.setHours(result.getTime("del_date").getHours());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date.getTime());
            collection.add(new DelUser(result.getInt("id"), result.getString("pic"), result.getString("page"), result.getBoolean("gender") ? User.Gender.FEMALE : User.Gender.MALE, result.getString("name"), calendar));
        }
        return collection;
    }

}