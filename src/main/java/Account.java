/*1
Examen SQL y MongoDB
Tras finalizar el periodo de FCT, te contratan en la caja rural para desarrollar el sistema informático que
controla los movimientos bancarios de tus clientes.
La base de datos de las cuentas bancarias está en ubicada en AWS RDS bajo un gestor PostgreSQL. Cada
cuenta bancaria tiene, además de su id de registro, su iban, su balance y el id del cliente.
Los clientes, por otro lado, están ubicados en una base de datos MongoDB, gestionados en una instancia
AWS EC2 con el servidor MongoDB instalado. Cada cliente almacenará su información personal.
Implementa los métodos necesarios para poder desarrollar una aplicación que permita crear, borrar,
modificar y listar clientes y cuentas bancarias. Las cuentas bancarias deben estar vinculadas a un cliente.
Finalmente, crea un método transaction(), que reciba como parámetros una cuenta de origen, una de
destino y una cantidad a transferir. Dicho método sustraerá la cantidad de la cuenta de origen para abonarla
en la cuenta de destino pero, en caso de que la cuenta de origen quede en números rojos o la cuenta de
destino no exista, tendrá que deshacer las operaciones anteriores que se hayan realizado y dejar la base de
datos en el estado previo al inicio del método.
Los detalles de la base de datos PostgreSQL en AWS son los siguientes:
• Punto de enlace: examenad.ci66saah1axn.us-east-1.rds.amazonaws.com
• Usuario: postgres
• Contraseña: qwerty1234
• Base de datos: Cada uno tiene su propia base de datos, cuyo nombre está formado por sus tres
primeras letras de primer y segundo apellido. Buscad entre los siguientes el vuestro: alvrub, arrsan,
cricoz, fulest, garbel, herill, maltor, morseg, munsot, nebcle, nebflo, redper, sanpin, tenmar, urefor.
• Tabla: accounts
Los detalles de la base de datos MongoDB son los siguientes:
• Punto de enlace: ec2-54-146-188-92.compute-1.amazonaws.com
• Usuario: De nuevo, tus tres primeras letras de primer y segundo apellido
• Contraseña: qwerty1234
• Base de datos: Tus tres primeras letras de primer y segundo apellido
• Colección: clients*/


public class Account {

    private int accountid;

    private String iban;

    private double balance;

    private int clientid;

    public Account(int accountid, String iban, double balance, int clientid) {
        this.accountid = accountid;
        this.iban = iban;
        this.balance = balance;
        this.clientid = clientid;
    }

    public Account() {

    }

    public int getAccountid() {
        return accountid;
    }

    public void setAccountid(int accountid) {
        this.accountid = accountid;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getClientid() {
        return clientid;
    }

    public void setClientid(int clientid) {
        this.clientid = clientid;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountid='" + accountid + '\'' +
                ", iban='" + iban + '\'' +
                ", balance='" + balance + '\'' +
                ", clientid='" + clientid + '\'' +
                '}';
    }
}
