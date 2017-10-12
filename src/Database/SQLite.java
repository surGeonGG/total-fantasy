package Database;


import java.sql.*;

public class SQLite {

    private static Connection connection;



    public void getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:test.db");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initialize();
    }

    private void initialize() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='events'");
            if (!resultSet.next()) {
                System.out.println("Building table...");
                statement.clearBatch();
                statement.execute("CREATE TABLE events(id integer, eventname varchar(60), eventtext text, primary key(id));");
            }
/*
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO events values(?,?,?);");
            preparedStatement.setString(2, "old_man");
            preparedStatement.setString(3, "An old man comes up to you and hands you a bar of soap. \"You need this more than I do.\"");
            preparedStatement.execute();*/

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public ResultSet getEvent(String eventName) {

        try {

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT eventname, eventtext FROM events");
            while(resultSet.next()) {
                System.out.println(resultSet.getString("eventname") + " " + resultSet.getString("eventtext"));
            }
            return resultSet;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
