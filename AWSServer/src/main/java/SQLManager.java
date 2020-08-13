import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class SQLManager {


    Connection conn;

    public SQLManager() throws Exception
    {
        this.conn = this.sqlConnection();
    }

    public void initiate() throws SQLException {
        try
        {
            Statement openingStatement = conn.createStatement();
            openingStatement.executeUpdate("CREATE TABLE IF NOT EXISTS people ( id INT NOT NULL, name VARCHAR (255), PRIMARY KEY (id) ) ;");
        }
        catch(SQLException e){
            throw new SQLException("Creation of table went wrong!");
        }

    }

    public boolean contains(String entryID) throws SQLException
    {
        Statement statement = conn.createStatement();
        ResultSet rs =  statement.executeQuery("SELECT NAME FROM people WHERE id="+entryID+";");
        return rs.next();
    }
    public String getName(String entryID) throws SQLException
    {
        Statement statement = conn.createStatement();
        ResultSet rs =  statement.executeQuery("SELECT NAME FROM people WHERE id="+entryID+";");
        if(rs.next())
        {
            return rs.getString("name");
        }
        return "no entry with this id";
    }

    public void addEntry(String entryID, String name) throws SQLException
    {
        Statement statement = conn.createStatement();
        statement.executeUpdate("INSERT IGNORE INTO people (id, name) VALUES ("+entryID+", '"+name+"');");
    }

    public  Connection sqlConnection() throws Exception
    {
        Properties props = new Properties();
        InputStream input = ServerDriver.class.getClassLoader().getResourceAsStream("db.properties");
        props.load(input);
        String db_hostname = props.getProperty("db_hostname");
        String db_username = props.getProperty("db_username");
        String db_password = props.getProperty("db_password");
        String db_database = props.getProperty("db_database");
        String url = "jdbc:mysql://"+db_hostname+":3306/"+db_database;
        Connection conn = DriverManager.getConnection(url, db_username, db_password);
        return conn;
    }
}
