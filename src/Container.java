import java.io.Serializable;

/**
 * Used to send an object with a command to the server
 * @author nicolas darr
 */
public class Container implements Serializable{
    private Object o;
    private String cmd;

    Container(String cmd){
        this.cmd = cmd;
    }

    Container(Object o, String cmd){
        this.o = o;
        this.cmd = cmd;
    }

    public void setCmd(String cmd){
        this.cmd = cmd;
    }

    public void setObject(Object o){
        this.o = o;
    }

    public String getCmd(){
        return this.cmd;
    }

    public Object getObject(){
        return this.o;
    }
}
