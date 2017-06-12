import javax.jms.Queue;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.jms.*;
import org.apache.activemq.jndi.ActiveMQInitialContextFactory;
import org.apache.log4j.varia.NullAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Client class to add transactions to the account on the server and to query all transactions from the server
 * @author nicolas darr
 */
public class Client {
    static private Logger logger = LoggerFactory.getLogger(Server.class);
    /**
     * Reads the need information from the user and creats a new transaction object with the given input
     * @return Transaction object
     */
    public static Transaction newTransaction(){
        //Read data from user
        Scanner sc = new Scanner(System.in);
        System.out.println("Account name: ");
        String acc_name = sc.nextLine();
        System.out.println("Amount: ");
        float amount = sc.nextFloat();
        System.out.println("Day: ");
        int day = sc.nextInt();
        System.out.println("Month: ");
        int mon = sc.nextInt();
        System.out.println("Year: ");
        int year = sc.nextInt();
        Date d = new Date(year, mon, day);
        //Create new transaction object
        Transaction t = new Transaction(amount, d, acc_name);
        return t;
    }

    public static void printTransactions(LinkedList<Transaction> transactions){
        for(Transaction t: transactions){
            System.out.println("-----------------------");
            t.printInfo();
            System.out.println("-----------------------");
        }
    }

    public static void main(String[] args) {
        //org.apache.log4j.BasicConfigurator.configure();
        org.apache.log4j.BasicConfigurator.configure(new NullAppender());
        Properties p = new Properties();
        p.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        p.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
        MessageProducer sender = null;
        MessageConsumer receiver = null;
        ObjectMessage in_o_msg = null;
        ObjectMessage out_o_msg = null;
        TextMessage t_msg = null;
        Scanner sc = null;
        try {
            Context ctx = new InitialContext(p);
            ConnectionFactory factory = (ConnectionFactory)ctx.lookup("ConnectionFactory");
            Connection connection = factory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination q = (Destination) ctx.lookup("dynamicQueues/Test");
            sender = session.createProducer(q);
            receiver = session.createConsumer(q);
            in_o_msg = session.createObjectMessage();
            out_o_msg = session.createObjectMessage();
            t_msg = session.createTextMessage();
            sc = new Scanner(System.in);
        } catch (NamingException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (JMSException e){
            e.printStackTrace();
            System.exit(1);
        }
        String cmd;
        do{
            System.out.println("Command: ");
            cmd = sc.nextLine();
            if(cmd.equals("ADD")){
                Date d = new Date();
                Transaction t = newTransaction();
                Container c = new Container(t, cmd);
                try {
                    out_o_msg.setObject(c);
                    sender.send(out_o_msg);
                    t_msg = (TextMessage) receiver.receive();
                    System.out.println(t_msg.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
            else if(cmd.equals("GET")){
                Container c = new Container(cmd);
                try {
                    out_o_msg.setObject(c);
                    sender.send(out_o_msg);
                    in_o_msg = (ObjectMessage) receiver.receive(1000);
                    if(in_o_msg != null) {
                        c = (Container) in_o_msg.getObject();
                        printTransactions((LinkedList)c.getObject());
                    }
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
            else if(!cmd.equals("QUIT")){
                System.out.println("Command not found!");
            }
        }while (!cmd.equals("QUIT"));

    }
}
