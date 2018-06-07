/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import org.json.JSONObject;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "WebServer", urlPatterns = {"/WebServer/*"})
public class WebServer extends HttpServlet {

    private BlockChain bc;
    private Block bl;
    String e = "65537";
    String n = "4579445128298888777109422080945883335528142902411200929626012857247357505282827386874854991515512200258398378114221026863184955915944586074451415160103769003637807592507858279477523555638752667781576459815301437667887193310010391112298906191";
    private String time;

    public WebServer() {
        bc = new BlockChain();
        bl = new Block("Genesis", "", 2);
        bc.addBlock(bl);
    }

    // GET returns a value given a key
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("Console: doGET visited");

        String result = "false";

        // The name is on the path /name so skip over the '/'
        String name = (request.getPathInfo()).substring(1);
        PrintWriter out = response.getWriter();
        // return 401 if name not provided

        if (name.equals("2")) {
            response.setStatus(200);
            response.setContentType("text/plain;charset=UTF-8");
            long time3 = new Date().getTime();
            boolean flag =bc.isChainValid();
            long time4 = new Date().getTime();
            String time5 = (time4 - time3) + "";
           
            result = flag + "#"+ time5 ;
          
            out.println(result);
       

        } else if (name.equals("3")) {
            response.setStatus(200);
            response.setContentType("text/plain;charset=UTF-8");
            // return the value from a GET request
            result = bc.toString();
            out.println(result);
        } else if (name.equals("t")) {
            response.setStatus(200);
            response.setContentType("text/plain;charset=UTF-8");
            // return the value from a GET request
            result = time;
            out.println(result);
        } else {
            response.setStatus(401);
            return;
        }
    }

    // POST is used to create a new variable
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("Console: doPost visited");

        // To look at what the client accepts examine request.getHeader("Accept")
        // We are not using the accept header here.
        // Read what the client has placed in the POST data area
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String data = br.readLine();

        // extract variable name from request data (variable names are a single character)
        String variableName = "" + data.charAt(0);

        // extract value after the equals sign
        String valString = data.substring(2);

        if (variableName.equals("j")) {
            String[] jsonParm = jsonParse(valString);
            String dif = jsonParm[0];
            int diff = Integer.parseInt(dif);
            String fullsigniture = jsonParm[1];
            String[] str = splitString(fullsigniture);
            String input = str[0];
            String signiture = str[1];

            boolean flag = verify(input, signiture, e, n);
            if (flag) {
                long time1 = new Date().getTime();
                Block init1 = new Block(fullsigniture, bc.getLatestBlock().calculateHash(), diff);
                bc.addBlock(init1);
                response.setStatus(200);
                long time2 = new Date().getTime();
                time = "Total execution time to add this block " + (time2 - time1) + " milliseconds";
                return;
            } else {
                response.setStatus(409);
                return;
            }
        } else {
            response.setStatus(401);
            return;
        }
    }

    public static String[] jsonParse(String json) {
        JSONObject obj = new JSONObject(json);

        String diff = obj.get("diff").toString();
        String fullSigniture = obj.get("fullSigniture").toString();
        String[] jsonParm = {diff, fullSigniture};
        return jsonParm;
    }

    public String[] splitString(String n) {
        String[] str = n.split("#");
        return str;
    }

    public boolean verify(String data, String signiture, String e1, String n1) {
        BigInteger e = new BigInteger(e1);
        BigInteger n = new BigInteger(n1);
        byte[] bytes = DatatypeConverter.parseHexBinary(signiture);
        BigInteger biEncrypt = new BigInteger(bytes);
        BigInteger clear = biEncrypt.modPow(e, n);
        // start data
        BigInteger data1 = strToBig(data);
        if (data1.equals(clear)) {
            return true;
        } else {
            return false;
        }

    }

    public BigInteger strToBig(String data) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");

            md.reset();
            md.update(data.getBytes(StandardCharsets.UTF_8));
            // Performs a final update on the digest using the specified array of bytes, then completes the digest computation.
            byte[] dt = md.digest();

            byte[] dt1 = {0};
            byte[] newdt = new byte[dt.length + dt1.length];
            System.arraycopy(dt1, 0, newdt, 0, dt1.length);
            System.arraycopy(dt, 0, newdt, dt1.length, dt.length);
            BigInteger m = new BigInteger(newdt);
            return m;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
