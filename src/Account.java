import java.io.Serializable;
import java.lang.reflect.Array;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Class to hold and manage transactions and identified by an account name
 * @author nicolas darr
 */
public class Account implements AccountInterface{
    private String name;
    private LinkedList<Transaction> transactions = new LinkedList<>();

    Account(String name){
        this.name = name;
    }

    public void addTransaction(float amount, Date trans_date, String acc_name){
        transactions.add(new Transaction(amount, trans_date, acc_name));
    }
    public void addTransaction(Transaction t){
        transactions.add(t);
    }

    public String getName(){
        return this.name;
    }

    public LinkedList<Transaction> getTransactions(){
        return this.transactions;
    }

    /**
     * Find a transaction by a given amount
     * @param amount Amount to look up for in the transaction list
     * @return Returns the found transaction. Otherwise null
     */
    public Transaction findTransaction(float amount){
        Iterator<Transaction> it = transactions.iterator();
        while(it.hasNext()){
            Transaction next = it.next();
            if(next.getAmount() == amount){
                return next;
            }
        }
        return null;
    }

}
