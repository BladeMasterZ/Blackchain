/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.cmu.xianz2;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Administrator
 */
@WebService(serviceName = "WebServer")
public class WebServer {

    private BlockChain bc;
    private Block bl;
    String e = "65537";
    String n = "4579445128298888777109422080945883335528142902411200929626012857247357505282827386874854991515512200258398378114221026863184955915944586074451415160103769003637807592507858279477523555638752667781576459815301437667887193310010391112298906191";
    private XmlParser xmlparser;
    public WebServer() {
        bc = new BlockChain();
        bl = new Block("Genesis", "", 2);
        bc.addBlock(bl); 
    }


    /**
     * Web service operation
     */
   
    public String viewChain() {
        //TODO write your implementation code here:
        return bc.toString();
    }

    /**
     * Web service operation
     */
   
    public boolean isValidate() {
        boolean flag = bc.isChainValid();
        return flag;
    }

    /**
     * Web service operation
     */

    public String addBolck( String data, int diff) {

        String[] str = splitString(data);
        String input = str[0];
        String signiture = str[1];

        boolean flag = verify(input, signiture, e, n);
        if (flag) {
            long time1 = new Date().getTime();
            Block init1 = new Block(data, bc.getLatestBlock().calculateHash(), diff);
            bc.addBlock(init1);
            long time2 = new Date().getTime();
            String time = (time2 - time1) + "";
            return "Total execution time to add this block " + time + " milliseconds";
        } else {
            return "invaildate signiture";
        }
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

    /**
     * Web service operation
     */
    @WebMethod(operationName = "operationer")
    public String operationer(@WebParam(name = "xmlMsg") String xmlMsg) {
       xmlparser = new XmlParser (xmlMsg);
       xmlparser.getstringXML();
                if (xmlparser.getNumber().equals("1")) {
                    String dif = xmlparser.getDifficulty();
                    String trans = xmlparser.getTransaction();
                    int diff = Integer.parseInt(dif);
                    return addBolck(trans, diff);
                    
                } else if (xmlparser.getNumber().equals("2")) {
                    long time3 = new Date().getTime();
                    boolean flag = isValidate();
                    long time4 = new Date().getTime();
                    String time5 = (time4 - time3) + "";
                    return "Verifying\n"+"Chain verification: " + flag + "\n"+ "Total execution time to verify the chain is "+ time5 + " milliseconds";
                } else if (xmlparser.getNumber().equals("3")) {       
                    return viewChain();
                }  else {
                     return "Invalidate Input" ;
                }    
    }

}
