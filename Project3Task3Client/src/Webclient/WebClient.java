package Webclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONObject;

// A simple class to wrap a result.
class Result {

    String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

public class WebClient {

    public static void main(String[] args) throws Exception {

        String d = "2677008747886823137156706430613512007986613405164647738146423830416459608845238567809978631611925902987617685642216179562556504831714698652024528592537807680096986228796308044213381969440543846724342393666963015703947823538500911179024579673";
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
                    String signiture = rsa.encryptSignature(trans, d, n);
                    String fullTrans = trans + "#" + signiture;
                    String json = jsonWrapper(dif, fullTrans);
                    switch (doPost("j", json)) {
                        case 200:
                            System.out.println(read("t"));
                            break;
                        case 409:
                            System.out.println("invaildate signiture");
                            break;
                        default:
                            System.out.println("no variable");
                            break;
                    }

                } else if (number.equals("2")) {
                    
                    String re = read("2");
                    String[] sp = splitString(re);
                    System.out.println("Verifying");
                    System.out.println("Chain verification: " + sp [0]);
                    System.out.println("Total execution time to verify the chain is " + sp [1] +  " milliseconds");
                } else if (number.equals("3")) {
                    System.out.println("View the blockchain.");
                    System.out.println(read("3"));
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

    // read a value associated with a name from the server
    // return either the value read or an error message
    public static String read(String name) {
        Result r = new Result();
        int status = 0;
        if ((status = doGet(name, r)) != 200) {
            return "Error from server " + status;
        }
        return r.getValue();
    }

    // Low level routine to make an HTTP POST request
    // Note, POST does not use the URL line for its message to the server
    public static int doPost(String name, String value) {

        int status = 0;

        try {
            // Make call to a particular URL
            URL url = new URL("http://localhost:8080/Project3Task3Server/WebServer/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // set request method to POST and send name value pair
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            // write to POST data area
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write(name + "=" + value);
            out.close();

            // get HTTP response code sent by server
            status = conn.getResponseCode();

            //close the connection
            conn.disconnect();
        } // handle exceptions
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // return HTTP status
        return status;
    }

    public static int doGet(String name, Result r) {

        // Make an HTTP GET passing the name on the URL line
        r.setValue("");
        String response = "";
        HttpURLConnection conn;
        int status = 0;

        try {

            // pass the name on the URL line
            URL url = new URL("http://localhost:8080/Project3Task3Server/WebServer" + "//" + name);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // tell the server what format we want back
            conn.setRequestProperty("Accept", "text/xml");

            // wait for response
            status = conn.getResponseCode();

            // If things went poorly, don't try to read any response, just return.
            if (status != 200) {
                // not using msg
                String msg = conn.getResponseMessage();
                return conn.getResponseCode();
            }
            String output = "";
            // things went well so let's read the response
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            while ((output = br.readLine()) != null) {
                response += output;

            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // return value from server 
        // set the response object
        r.setValue(response);
        // return HTTP status to caller
        return status;
    }

    // Low level routine to make an HTTP PUT request
    // Note, PUT does not use the URL line for its message to the server
    public static String jsonWrapper(String diff, String fullSigniture) {
        JSONObject obj = new JSONObject();
        obj.put("diff", diff);
        obj.put("fullSigniture", fullSigniture);
        String jsonObj = obj.toString();
        return jsonObj;
    }
    public static String[] splitString(String n) {
        String[] str = n.split("#");
        return str;
    }
}
