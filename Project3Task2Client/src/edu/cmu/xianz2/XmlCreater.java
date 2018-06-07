/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.cmu.xianz2;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Administrator
 */
public class XmlCreater {

    private String xmlInit;
    private Document doc;
    private String[] index = {"number", "transaction", "difficulty"};
    private String[] value;

    public XmlCreater(String nub, String diff, String tran) {
        xmlInit
                = "<BockRequest>\n"
                + "  <number>example</number>\n"
                + "  <transaction>example</transaction>\n"
                + "  <difficulty>example</difficulty>\n"
                + "</BockRequest>";

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.parse(new InputSource(new StringReader(xmlInit)));
            value = new String[3];
            value[0] = nub;
            value[1] = diff;
            value[2] = tran;
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(XmlCreater.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String stringXML() {
        Element requestEle = doc.getDocumentElement();
        //  NodeList blockRequest = doc.getElementsByTagName("blockRequest"); 
        for (int i = 0; i < index.length; i++) {
            requestEle.getElementsByTagName(index[i]).item(0).setTextContent(value[i]);

        }
        String xmlString = fromdocToStr(doc);
        return xmlString;
    }

    // reference http://stackoverflow.com/questions/2567416/xml-document-to-string
    private static String fromdocToStr(Document doc) {
        try {
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
                    "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            return sw.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Error converting to String", ex);
        }
    }

    public static void main(String[] argus) {
        XmlCreater li = new XmlCreater("2", "1", "3");
        System.out.println(li.stringXML());
    }

}
