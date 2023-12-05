import java.sql.*;
import java.util.ArrayList;

public class OperacionesAccount {


    //Metodo para crear un account
    public static boolean crearAccount(Account account){

        // Configurar de la conexión a la base de datos PostgreSQL
        String urlConexion = "jdbc:postgresql://examenad.ci66saah1axn.us-east-1.rds.amazonaws.com/alvrub";
        String usuario = "postgres";
        String password = "qwerty1234";

        boolean resultado = false;
        try (Connection conexion = DriverManager.getConnection(urlConexion, usuario, password)) {
            String sentenciaSQL = "INSERT INTO accounts (accountid, iban, balance, clientid) VALUES (?,?,?,?)";
            PreparedStatement sentencia = conexion.prepareStatement(sentenciaSQL);
            sentencia.setInt(1, account.getAccountid());
            sentencia.setString(2, account.getIban());
            sentencia.setDouble(3, account.getBalance());
            sentencia.setInt(4, account.getClientid());

            int filasInsertadas = sentencia.executeUpdate();
            if (filasInsertadas > 0) {
                resultado = true;
            }
            sentencia.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultado;
    }

    //Metodo para borrar un account por su id
    public static boolean borrarAccount(int accountid){

        // Configurar de la conexión a la base de datos PostgreSQL
        String urlConexion = "jdbc:postgresql://examenad.ci66saah1axn.us-east-1.rds.amazonaws.com/alvrub";
        String usuario = "postgres";
        String password = "qwerty1234";

        boolean resultado = false;
        try (Connection conexion = DriverManager.getConnection(urlConexion, usuario, password)) {
            String sentenciaSQL = "DELETE FROM accounts WHERE accountid = ?";
            PreparedStatement sentencia = conexion.prepareStatement(sentenciaSQL);
            sentencia.setInt(1, accountid);

            int filasBorradas = sentencia.executeUpdate();
            if (filasBorradas > 0) {
                resultado = true;
            }
            sentencia.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultado;
    }



    //Metodo para actualizar pasándole un account
    public static boolean actualizarAccount(Account account){

        // Configurar de la conexión a la base de datos PostgreSQL
        String urlConexion = "jdbc:postgresql://examenad.ci66saah1axn.us-east-1.rds.amazonaws.com/alvrub";
        String usuario = "postgres";
        String password = "qwerty1234";

        boolean resultado = false;
        try (Connection conexion = DriverManager.getConnection(urlConexion, usuario, password)) {
            String sentenciaSQL = "UPDATE accounts SET iban = ?, balance = ?, clientid = ? WHERE accountid = ?";
            PreparedStatement sentencia = conexion.prepareStatement(sentenciaSQL);
            sentencia.setString(1, account.getIban());
            sentencia.setDouble(2, account.getBalance());
            sentencia.setInt(3, account.getClientid());
            sentencia.setInt(4, account.getAccountid());

            int filasActualizadas = sentencia.executeUpdate();
            if (filasActualizadas > 0) {
                resultado = true;
            }
            sentencia.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultado;
    }

    //Método que devuelve un ArrayList con todos los accounts
    public static ArrayList<Account> leerAccounts(){

        // Configurar de la conexión a la base de datos PostgreSQL
        String urlConexion = "jdbc:postgresql://examenad.ci66saah1axn.us-east-1.rds.amazonaws.com/alvrub";
        String usuario = "postgres";
        String password = "qwerty1234";

        ArrayList<Account> accounts = new ArrayList<>();
        try (Connection conexion = DriverManager.getConnection(urlConexion, usuario, password)) {
            String sentenciaSQL = "SELECT * FROM accounts";
            PreparedStatement sentencia = conexion.prepareStatement(sentenciaSQL);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                Account account = new Account();
                account.setAccountid(resultado.getInt("accountid"));
                account.setIban(resultado.getString("iban"));
                account.setBalance(resultado.getDouble("balance"));
                account.setClientid(resultado.getInt("clientid"));
                accounts.add(account);
            }
            sentencia.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return accounts;
    }

    //Método que devuelve un ArrayList con todos los accounts de un cliente

    public static ArrayList<Account> leerAccountsPorCliente(int clientid){

        // Configurar de la conexión a la base de datos PostgreSQL
        String urlConexion = "jdbc:postgresql://examenad.ci66saah1axn.us-east-1.rds.amazonaws.com/alvrub";
        String usuario = "postgres";
        String password = "qwerty1234";

        ArrayList<Account> accounts = new ArrayList<>();
        try (Connection conexion = DriverManager.getConnection(urlConexion, usuario, password)) {
            String sentenciaSQL = "SELECT * FROM accounts WHERE clientid = ?";
            PreparedStatement sentencia = conexion.prepareStatement(sentenciaSQL);
            sentencia.setInt(1, clientid);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                Account account = new Account();
                account.setAccountid(resultado.getInt("accountid"));
                account.setIban(resultado.getString("iban"));
                account.setBalance(resultado.getDouble("balance"));
                account.setClientid(resultado.getInt("clientid"));
                accounts.add(account);
            }
            sentencia.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return accounts;
    }

    //Finalmente, crea un método transaction(), que reciba como parámetros una cuenta de origen, una de
    //destino y una cantidad a transferir. Dicho método sustraerá la cantidad de la cuenta de origen para abonarla
    //en la cuenta de destino pero, en caso de que la cuenta de origen quede en números rojos o la cuenta de
    //destino no exista, tendrá que deshacer las operaciones anteriores que se hayan realizado y dejar la base de
    //datos en el estado previo al inicio del método.

    public static boolean transaction(Account accountOrigen, Account accountDestino, double cantidad){

        // Configurar de la conexión a la base de datos PostgreSQL
        String urlConexion = "jdbc:postgresql://examenad.ci66saah1axn.us-east-1.rds.amazonaws.com/alvrub";
        String usuario = "postgres";
        String password = "qwerty1234";

        boolean resultado = false;
        try (Connection conexion = DriverManager.getConnection(urlConexion, usuario, password)) {
            try {
                conexion.setAutoCommit(false);
                String sentenciaSQL = "UPDATE accounts SET balance = ? WHERE accountid = ?";
                PreparedStatement sentencia = conexion.prepareStatement(sentenciaSQL);

                if(accountOrigen.getBalance() - cantidad < 0){
                    conexion.rollback();
                    System.err.println("ROLLBACK ejecutado");
                    throw new SQLException("No hay suficiente dinero en la cuenta de origen");
                }
                sentencia.setDouble(1, accountOrigen.getBalance() - cantidad);
                sentencia.setInt(2, accountOrigen.getAccountid());
                int filasActualizadas = sentencia.executeUpdate();
                if (filasActualizadas > 0) {
                    sentencia.setDouble(1, accountDestino.getBalance() + cantidad);
                    sentencia.setInt(2, accountDestino.getAccountid());
                    sentencia.executeUpdate();
                }

                sentencia.close();
            }catch (SQLException e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                conexion.rollback();
                System.err.println("ROLLBACK ejecutado");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultado;
    }
}
