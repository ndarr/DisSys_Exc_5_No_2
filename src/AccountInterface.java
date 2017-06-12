import java.rmi.RemoteException;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by nicolasdarr on 28.05.17.
 */
public interface AccountInterface{
    public void addTransaction(float amount, Date trans_date, String acc_name);
    public void addTransaction(Transaction t) throws RemoteException;

    public String getName();

    public LinkedList<Transaction> getTransactions();

    /**
     * Find a transaction by a given amount
     * @param amount Amount to look up for in the transaction list
     * @return Returns the found transaction. Otherwise null
     */
    public Transaction findTransaction(float amount);
}
