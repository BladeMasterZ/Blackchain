
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        if (!varify.equals(chainHash)){
            String index1  = getLatestBlock().getIndex() + "";
            String diff1 = getAim(getLatestBlock().getDifficulty());
         System.out.println("Verifying");
         System.out.println("Improper hash on node "+ index1 + 
                        " Does not begin with " + diff1
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
        if (list.size() == 1){
            Block block = list.get(0);
            chainHash = block.proofOfWork(block.getDifficulty());
        }

    }


    @Override
    public String toString() {
        JSONObject obj = new JSONObject();
        obj.put("chain", jsonOutputHelper (list));
        obj.put("chainHash", chainHash);
        String outPut = "{\"chain\":" + jsonOutputHelper (list) +","+" \"chainHash\":"+"\""+chainHash + "\""+ "}";

        return outPut;
    }

    public static void main(String[] args) {
        // initial block 0 
        BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));
        BlockChain bc = new BlockChain();
        Block init = new Block("Genesis", "", 2);
        bc.addBlock(init);

        while (true) {
            try {
                System.out.println(" ");
                System.out.println("1. Add a transaction to the blockchain.");
                System.out.println("2. Verify the blockchain.");
                System.out.println("3. View the blockchain.");
                System.out.println("4. Corrupt the chain.");
                System.out.println("5. Hide the corruption by re-computing hashes.");
                System.out.println("6. Exit.");
                String number = typed.readLine().trim();
                //Add a transaction to the blockchain.
                if (number.equals("1")) {
                    System.out.println("Enter difficulty");
                    String dif = typed.readLine();
                    System.out.println("Enter transaction");
                    String trans = typed.readLine();
                    int diff = Integer.parseInt(dif);
                    Block init1 = new Block(trans, bc.getLatestBlock().calculateHash(), diff);
                    long time1 = new Date().getTime();
                    bc.addBlock(init1);
                    long time2 = new Date().getTime();
                    String time = (time2 - time1) + "";
                    System.out.println("Total execution time to add this block " + time + " milliseconds");
                    //Verify the blockchain
                } else if (number.equals("2")) {
                    long time3 = new Date().getTime();
                    boolean flag = bc.isChainValid();
                    long time4 = new Date().getTime();
                    String time5 = (time4 - time3) + "";
                    System.out.println("Chain verification: " + flag);
                    System.out.println("Total execution time to verify the chain is "+ time5 + " milliseconds");
                    // View the blockchain
                } else if (number.equals("3")) {
                    System.out.println("View the blockchain.");
                    System.out.println(bc.toString());
              
                 // Corrupt the chain.  
                } else if (number.equals("4")) {
                    System.out.println("Enter block to corrupt");
                    String blockIndex = typed.readLine();
                    int blockIndex1 = Integer.parseInt(blockIndex);
                    while (blockIndex1 > bc.list.size() - 1) {
                        System.out.println("Invaild block, Re-enter block to corrupt again ");
                        blockIndex = typed.readLine();
                        blockIndex1 = Integer.parseInt(blockIndex);
                    }
                    Block choose = bc.list.get(blockIndex1);
                    System.out.println("Enter new data for block " + blockIndex);
                    String newData = typed.readLine();
                    choose.setData(newData);
                    System.out.println("Block " + blockIndex + " now holds " + newData);
                    // Hide the corruption by re-computing hashes.
                } else if (number.equals("5")) {
                    System.out.println("Repair the Blockchain");
                    bc.repairChain();
                    System.out.println("Repair complete");
                    // Exit
                } else if (number.equals("6")) {
                    break;
                } else {
                    System.out.println("Invalid Input! ");
                }

            } catch (IOException ex) {
                Logger.getLogger(BlockChain.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    } 
    // helper to get number of "0"
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
