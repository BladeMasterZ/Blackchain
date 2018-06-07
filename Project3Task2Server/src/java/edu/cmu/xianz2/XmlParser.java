/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.cmu.xianz2;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class XmlParser {

    private Document doc;
    private String transaction;
    private String difficulty;
    private String number;
    private String[] index = {"number", "transaction", "difficulty"};
    private String[] value;
   

    public XmlParser(String StingXMl ) {

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.parse(new InputSource(new StringReader(StingXMl)));
         value = new  String[3]; 
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(XmlParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
        public void getstringXML() {
        Element requestEle = doc.getDocumentElement();
        //  NodeList blockRequest = doc.getElementsByTagName("blockRequest"); 
        for (int i = 0; i < index.length; i++) {
            String ls = requestEle.getElementsByTagName(index[i]).item(0).getTextContent();
           value [i] =ls;
        }
         number = value [0];
         transaction = value [1];
         difficulty =  value [2];

    }
        
        public String getNumber() {
        return number;
    }
        
        public String getTransaction() {
        return transaction;
    }
        public String getDifficulty() {
        return difficulty;
    }

}
