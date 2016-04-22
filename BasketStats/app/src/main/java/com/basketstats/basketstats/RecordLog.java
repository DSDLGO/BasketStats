package com.basketstats.basketstats;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Hashtable;

public class RecordLog {
    private OutputStreamWriter osw;
    private FileInputStream fis;
    private ArrayList<ArrayList> logList;
    private ArrayList<String> playbyplayList;
    private Hashtable ht;
    public static final int info = 0;
    public static final int commandLog = 1;
    public static final int playLog = 2;

    public RecordLog(OutputStreamWriter writer, FileInputStream reader) throws Exception{
        osw = writer;
        fis = reader;
        logList = new ArrayList<>();
        playbyplayList = new ArrayList<>();
        ht = new Hashtable();
        ht.put(info, "<info>");
        ht.put(commandLog, "<command>");
        ht.put(playLog, "<play>");
    }

    public void log(int type, String str){
        String tag = (String) ht.get(type);
        if(type == playLog) return;
        try{
            osw.write(tag + str + "\n");
        }catch (IOException e){
            e.printStackTrace();
        }
        ArrayList<String> logStr = new ArrayList<>();
        logStr.add(tag);
        logStr.add(str);
        logList.add(logStr);
    }

    public void log(int type, String player, String action){
        String tag = (String) ht.get(type);
        if(type != playLog) return;
        String str = "player=" + player + "&action=" + action;
        try{
            osw.write(tag + str + "\n");
        }catch (IOException e){
            e.printStackTrace();
        }
        playbyplayList.add(player + ": " + action);

        ArrayList<String> logStr = new ArrayList<>();
        logStr.add(tag);
        logStr.add(player);
        logStr.add(action);
        logList.add(logStr);
    }

    public String getPlayByPlayList(){
        String ret = new String();
        for (int i = 0; i < playbyplayList.size(); i++){
            ret = ret + playbyplayList.get(i) + "\n";
        }
        return ret;
    }

    public ArrayList<String> undo(){
        ArrayList<String> ret = new ArrayList<>();
        if(!logList.isEmpty()) {
            if(logList.get(logList.size()-1).get(0) == ht.get(info).toString()){
                return ret;
            }
            else if(logList.get(logList.size()-1).get(0) == ht.get(commandLog).toString()){
                // do nothing
            }
            else if(logList.get(logList.size()-1).get(0) == ht.get(playLog).toString()) {
                playbyplayList.remove(playbyplayList.size() - 1);
            }
            ret = logList.get(logList.size() - 1);
            logList.remove(logList.size() - 1);
        }
        return ret;
    }

    public String getTag(int type){
        return ht.get(type).toString();
    }

    public int getType(String tag){
        int ret = -1;
        if(tag == ht.get(info).toString()) ret = info;
        else if(tag == ht.get(commandLog).toString()) ret = commandLog;
        else if(tag == ht.get(playLog).toString()) ret = playLog;
        return ret;
    }

    public String readDataFromFile(){
        String result = null;
        try {
            StringBuilder sb = new StringBuilder();
            byte[] data = new byte[fis.available()];
            while (fis.read(data) != -1) {
                sb.append(new String(data));
            }
            fis.close();
            result = sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
