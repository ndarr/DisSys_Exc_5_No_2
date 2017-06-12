import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

/**
 * Class to hold a transaction managed by an account
 * @author nicolas darr
 */
public class Transaction implements Serializable{
    private float amount;
    private Date trans_date;
    private String account_name;

    Transaction(float amount, Date trans_date, String account_name){
        this.amount = amount;
        this.trans_date = trans_date;
        this.account_name = account_name;
    }
    Transaction(Transaction t){
        this.amount = t.getAmount();
        this.trans_date = t.getTrans_date();
        this.account_name = t.getAccount_name();
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Date getTrans_date(){
        return this.trans_date;
    }

    public float getAmount(){
        return this.amount;
    }

    public String getAccount_name(){
        return this.account_name;
    }


    /**
     * Prints all information of the transaction
     */
    public void printInfo(){
        System.out.println("Account name: " + account_name);
        System.out.println("Amount: " + Float.toString(amount));
        System.out.println("Date: " + trans_date.toString());
    }
}
