package Database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQLite {

    private static Connection connection;



    public SQLite() {

        Connection connection = null;
        ResultSet resultSet = null;
        Statement statement = null;


        try
        {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:D:\\testdb.db");
            statement = connection.createStatement();
            resultSet = statement
                    .executeQuery("SELECT EMPNAME FROM EMPLOYEEDETAILS");
            while (resultSet.next())
            {
                System.out.println("EMPLOYEE NAME:"
                        + resultSet.getString("EMPNAME"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                resultSet.close();
                statement.close();
                connection.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
