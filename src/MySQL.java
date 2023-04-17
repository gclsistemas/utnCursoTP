import java.sql.*;

public class MySQL {
//    private String driver = "com.mysql.jdbc.Driver";
//    private String databaseURL = "jdbc:mysql://localhost:3306/";
    private String driver = "org.mariadb.jdbc.Driver";
    private String databaseURL = "jdbc:mariadb://localhost:3306/";

    private String databaseName = "utn_curso_java"; //""test";
    private String user = "root";
    private String password = "";

    private static Connection connection = null;
    private static Statement statement = null;

    public Connection getConnection() {
        return connection;
    }

    private static void showSQLException(SQLException e) {
        SQLException next = e;
        while (next != null) {
            System.out.println(next.getMessage());
            System.out.println("Error" + next.getErrorCode());
            if (next.getErrorCode() == 1064) {
                System.out.println("Do that");
            }
            System.out.println("Errore" + next.getSQLState());
            next = next.getNextException();
        }
    }

    public MySQL() {
    }

    /**
     *
     * @param databaseURL
     * @param databaseName
     * @param userName
     * @param password
     */
    public MySQL(String databaseURL, String databaseName, String userName, String password) {
        this.driver = driver;
        this.databaseURL = databaseURL;
        this.databaseName = databaseName;
        this.user = userName;
        this.password = password;
    }

    /**
     * @param databaseName
     * @param userName
     * @param password
     */
    public MySQL(String driver, String databaseURL, String databaseName, String userName, String password) {
        this.driver = driver;
        this.databaseURL = databaseURL;
        this.databaseName = databaseName;
        this.user = userName;
        this.password = password;
    }

    public void closeConn() {
        try {
            connection.close();
        } catch (SQLException ex) {
            System.out.println("MySQL closeConn SQLException");
            showSQLException(ex);
        }
    }

    public void openConn() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(databaseURL + this.databaseName, this.user, this.password);
            System.out.println("Stablished connection");
            connection.setAutoCommit(true);
        } catch (ClassNotFoundException ex) {
            System.out.println("MySQL openConn ClassNotFoundException");
            System.out.println(ex.getMessage());
        } catch (SQLException ex) {
            System.out.println("MySQL openConn SQLException");
            showSQLException(ex);
        }
    }

    /**
     * @param query
     * @return
     */
    public ResultSet execQuery(String query) {
        try {
            statement = connection.createStatement();
            ResultSet rpta = statement.executeQuery(query);
            statement.close();
            return rpta;
        } catch (SQLException ex) {
            System.out.println("MySQL execQuery SQLException");
            showSQLException(ex);
            return null;
        }
    }

    /**
     * @param query
     * @return
     */
    public int execUpdate(String query) {
        try {
            statement = connection.createStatement();
            int rpta = statement.executeUpdate(query);
            statement.close();
            return rpta;
        } catch (SQLException ex) {
            System.out.println("MySQL execQuery SQLException");
            showSQLException(ex);
            return ex.getErrorCode();
        }
    }
}
