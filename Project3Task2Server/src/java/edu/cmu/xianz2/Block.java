package edu.cmu.xianz2;


import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

import java.util.ArrayList;

import javax.xml.bind.DatatypeConverter;
import org.json.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author xianz2
 */
public class Block {

    private String previousHash;
    private String data;
    private Timestamp timeStamp;
    private BigInteger nonce;
    private int difficulty;
    private int index;
    private String hash;

    public Block(int index1, long timestamp1, String data1, int difficulty1) {
        data = data1;
        timeStamp = new Timestamp(System.currentTimeMillis());
        index = index1;
        difficulty = difficulty1;

    }

    public Block(String data1, String hash1, int difficulty1) {
        data = data1;
        timeStamp = new Timestamp(System.currentTimeMillis());
        previousHash = hash1;
        difficulty = difficulty1;
        nonce = new BigInteger("0");
    }

    Block() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the previousHash
     */
    public String getPreviousHash() {
        return previousHash;
    }

    public String getHash() {
        return hash;
    }

    /**
     * @param previousHash the previousHash to set
     */
    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    /**
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * @return the timeStamp
     */
    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    /**
     * @param timeStamp the timeStamp to set
     */
    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * @return the nonce
     */
    public BigInteger getNonce() {
        return nonce;
    }

    /**
     * @param nonce the nonce to set
     */
    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }

    /**
     * @return the difficulty
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * @param difficulty the difficulty to set
     */
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    public String calculateHash() {
        MessageDigest md;
        byte[] dt = null;
        String result = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            String nonc = nonce.toString();

            String hashBefore = previousHash
                    + timeStamp
                    + data + String.valueOf(difficulty) + nonc;
            md.reset();
            // Updates the digest using the byte array of originalString.
            md.update(hashBefore.getBytes(StandardCharsets.UTF_8));
            dt = md.digest();
            result = DatatypeConverter.printHexBinary(dt);
            return result;
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex);
            return null;

        }
    }

    public String proofOfWork(int difficulty) {
        nonce = new BigInteger("0");
        StringBuilder sb = new StringBuilder();
        ArrayList<String> s1 = new ArrayList<>();
        for (int i = 0; i < difficulty; i++) {
            s1.add("0");
        }
        for (String s2 : s1) {
            sb.append(s2);
        }
        String hash = calculateHash();
        String aim = sb.toString();
        while (!hash.substring(0, difficulty).equals(aim)) {
            nonce = nonce.add(BigInteger.ONE);
            hash = calculateHash();
        }
        return hash;

    }

    @Override
    public String toString() {
        JSONObject obj = new JSONObject();
        obj.put("index", index);
        obj.put("timeStamp", timeStamp);
        obj.put("Tx", data);
        obj.put("prevHash", previousHash);
        obj.put("nonce", nonce);
        obj.put("difficulty", difficulty);
        
    
        return obj.toString();
    }

}
