package com.basketstats.basketstats;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Hashtable;

public class RecordLog {
    private OutputStreamWriter osw;
    private FileInputStream fis;
    private Hashtable ht;
    public static final int info = 0;
    public static final int command = 1;


    public RecordLog(OutputStreamWriter writer, FileInputStream reader) throws Exception{
        osw = writer;
        fis = reader;
        ht = new Hashtable();
        ht.put(info, "<info>");
        ht.put(command, "<command>");
    }

    public void log(int type, String str){
        String tag = (String) ht.get(type);
        try{
            osw.write(tag);
            osw.write(str);
            osw.write("\n");
        }catch (IOException e){
            e.printStackTrace();
        }
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
