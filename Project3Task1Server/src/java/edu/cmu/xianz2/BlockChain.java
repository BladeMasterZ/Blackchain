package edu.cmu.xianz2;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONObject;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrator
 */
public class BlockChain {

    private ArrayList<Block> list;
    private String chainHash;
    private int counter = 0;

    public BlockChain() {
        list = new ArrayList();

    }

    public void setChainHash(String chainHash) {
        this.chainHash = chainHash;
    }

    public void addBlock(Block newBlock) {

        chainHash = newBlock.proofOfWork(newBlock.getDifficulty());
        newBlock.setIndex(counter);
        list.add(newBlock);
        ++counter;
    }

    public Block getLatestBlock() {

        return list.get(list.size() - 1);
    }

    public long getTime() {
        return new Date().getTime();
    }

    public boolean isChainValid() {
        String veryStr1 = null;
        String veryStr2 = null;
        String varify = getLatestBlock().calculateHash();
        if (!varify.equals(chainHash)) {
            String index1 = getLatestBlock().getIndex() + "";
            String diff1 = getAim(getLatestBlock().getDifficulty());
            System.out.println("Verifying");
            System.out.println("Improper hash on node " + index1
                    + " Does not begin with " + diff1
            );
            return false;
        }

        for (int i = 1; i < list.size(); i++) {
            Block beforeBlock = list.get(i - 1);
            Block afterBlock = list.get(i);
            veryStr1
                    = beforeBlock.calculateHash();
            veryStr2 = afterBlock.getPreviousHash();
            String index = beforeBlock.getIndex() + "";
            String diff = getAim(beforeBlock.getDifficulty());
            if (!veryStr1.equals(veryStr2)) {
                System.out.println("Verifying");
                System.out.println("Improper hash on node " + index
                        + " Does not begin with " + diff
                );
                return false;

            }
        }
        System.out.println("Verifying");
        return true;
    }

    public void repairChain() {
        for (int i = 0; i < list.size() - 1; i++) {
            Block block = list.get(i);
            String Str = block.proofOfWork(block.getDifficulty());
            Block afterBlock = list.get(i + 1);
            afterBlock.setPreviousHash(Str);
            chainHash = afterBlock.proofOfWork(afterBlock.getDifficulty());
        }

    }

    public String getAim(int diff) {
        StringBuilder sb = new StringBuilder();
        ArrayList<String> s1 = new ArrayList<>();
        for (int i = 0; i < diff; i++) {
            s1.add("0");
        }
        for (String s2 : s1) {
            sb.append(s2);
        }

        String aim = sb.toString();
        return aim;
    }

    @Override
    public String toString() {
        JSONObject obj = new JSONObject();
        obj.put("chain", list);
        obj.put("chainHash", chainHash);
         String outPut = "{\"chain\":" + jsonOutputHelper (list) +","+" \"chainHash\":"+"\""+chainHash + "\""+ "}";
        return outPut;
    }
         // helper to get output format right 
    public ArrayList jsonOutputHelper (ArrayList<Block> list ){
    ArrayList<String> helper = new ArrayList<>();
    for (Block list1 : list){
    String outPut = "{\"index\":"+ list1.getIndex() + ",\"time stamp\":\""+ list1.getTimeStamp() + "\",\"Tx \":\""+ list1.getData() + "\",\"PrevHash\":\""+ list1.getPreviousHash() + "\",\"nonce\":"+ list1.getNonce() + ",\"difficulty\":"+ list1.getDifficulty() + "}";
    helper.add(outPut);
    }
    return helper;
    }

}
