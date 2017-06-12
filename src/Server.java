/**
 * Server class which starts a new TCP Server and manages the transactions held by the account
 * @author nicolas darr
 */
import org.apache.log4j.varia.NullAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.LinkedList;
import java.util.Properties;

public class Server {
    static private Account acc;
    static private Logger logger = LoggerFactory.getLogger(Server.class);
    /**
     * Main method for server starting a new Registry with an account object
     * @param args Command line arguments
     */
    public static void main(String[] args) {
	// write your code here
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


        } catch (NamingException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (JMSException e){
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Waiting for commands from client....");
        String cmd = "";
        acc = new Account("Test User");
        do{
            Container c = null;
            try {
                in_o_msg = (ObjectMessage)receiver.receive();
                c = (Container) in_o_msg.getObject();
                cmd = c.getCmd();
            } catch (JMSException e) {
                e.printStackTrace();
            }
            if(cmd.equals("ADD")){
                Transaction t = (Transaction)c.getObject();
                acc.addTransaction(t);
                System.out.println("Received transaction from client");
                try {
                    t_msg.setText("Transaction received");
                    sender.send(t_msg);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
            else if(cmd.equals("GET")){
                LinkedList<Transaction> transactions = acc.getTransactions();
                c = new Container(transactions, cmd);
                try {
                    out_o_msg.setObject(c);
                    sender.send(out_o_msg);
                    System.out.println("Sent transactions to client");

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
