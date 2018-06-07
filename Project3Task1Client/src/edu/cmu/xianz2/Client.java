/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.cmu.xianz2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;



/**
 *
 * @author Administrator
 */
public class Client {

    public static void main(String[] args) {
        String d ="2677008747886823137156706430613512007986613405164647738146423830416459608845238567809978631611925902987617685642216179562556504831714698652024528592537807680096986228796308044213381969440543846724342393666963015703947823538500911179024579673";
        String n = "4579445128298888777109422080945883335528142902411200929626012857247357505282827386874854991515512200258398378114221026863184955915944586074451415160103769003637807592507858279477523555638752667781576459815301437667887193310010391112298906191";
        RSAExample rsa = new RSAExample();
        BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                System.out.println(" ");
                System.out.println("1. Add a transaction to the blockchain.");
                System.out.println("2. Verify the blockchain.");
                System.out.println("3. View the blockchain.");
                System.out.println("4. Exit.");
                String number = typed.readLine().trim();
                if (number.equals("1")) {
                    System.out.println("Enter difficulty");
                    String dif = typed.readLine();
                    System.out.println("Enter transaction");
                    String trans = typed.readLine().trim();
                    int diff = Integer.parseInt(dif);
                    
                    String signiture = rsa.encryptSignature(trans, d, n);
                    String fullTrans= trans + "#" + signiture;
                    String result = addBolck(fullTrans, diff);
                    System.out.println(result);

                } else if (number.equals("2")) {
//                    long time3 = new Date().getTime();
//                    boolean flag = isValidate();
//                    long time4 = new Date().getTime();
//                    String time5 = (time4 - time3) + "";
//                    System.out.println("Verifying");
//                    System.out.println("Chain verification: " + flag);
//                    System.out.println("Total execution time to verify the chain is "+ time5 + " milliseconds");
                        System.out.println(isValidateString());

                } else if (number.equals("3")) {
                    System.out.println("View the blockchain.");
                    System.out.println(viewChain());
                } else if (number.equals("4")) {
                    break;
                } else {
                    System.out.println("Invalid Input! ");
                }
            } catch (IOException ex) {
                System.out.println("IOException: " + ex);
            }

        }

    }

    private static String addBolck(java.lang.String data, int diff) {
        edu.cmu.xianz2.WebServer_Service service = new edu.cmu.xianz2.WebServer_Service();
        edu.cmu.xianz2.WebServer port = service.getWebServerPort();
        return port.addBolck(data, diff);
    }

    private static boolean isValidate() {
        edu.cmu.xianz2.WebServer_Service service = new edu.cmu.xianz2.WebServer_Service();
        edu.cmu.xianz2.WebServer port = service.getWebServerPort();
        return port.isValidate();
    }

    private static String viewChain() {
        edu.cmu.xianz2.WebServer_Service service = new edu.cmu.xianz2.WebServer_Service();
        edu.cmu.xianz2.WebServer port = service.getWebServerPort();
        return port.viewChain();
    }

    private static String isValidateString() {
        edu.cmu.xianz2.WebServer_Service service = new edu.cmu.xianz2.WebServer_Service();
        edu.cmu.xianz2.WebServer port = service.getWebServerPort();
        return port.isValidateString();
    }





}
