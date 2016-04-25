package cs601.webmail.mailServer;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by xyz on 10/28/14.
 */
public class MailManager {
    private  String server;
    private  int port;
    //private  Socket socket;
    private SSLSocket socket;

    private BufferedReader br;
    private  DataOutputStream dot;
    private  String username, pwd;
    public MailManager(String server, int port) {

        this.server = server;
        this.port = port;
        // Security.addProvider(
        //         new com.sun.net.ssl.internal.ssl.Provider());

        SSLSocketFactory factory =
                (SSLSocketFactory)SSLSocketFactory.getDefault();

        try {
            socket =                (SSLSocket)factory.createSocket(server, port);
            socket.startHandshake();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dot = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public  boolean judge(String username, String pwd) throws IOException{
//        while (br.ready()) {
//            System.out.println("wait");
//        }
        System.out.println(br.readLine());
        String s1 = "USER "+username+"\r\n";
        String s2 = "PASS "+pwd+"\r\n";
        dot.writeBytes(s1);
        String response1 = br.readLine();
        if(response1.startsWith("+OK")) {
            System.out.println(response1);
            dot.writeBytes(s2);
            String response2 = br.readLine();
            if(response2.startsWith("+OK")) {
                System.out.println(response2);
                dot.writeBytes("QUIT\r\n");
                return true;
            }
        }
        dot.writeBytes("QUIT\r\n");
        return false;
    }

//    public static void main(String[] args) {
//        MailManager m = new MailManager("pop.qq.com", 995);
//        try {
//            System.out.println(m.judge("834208094@qq.com",""));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
