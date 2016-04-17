package com.basketstats.basketstats;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Hashtable;

public class RecordLog {
    private OutputStreamWriter osw;
    private Hashtable ht;
    public static final int info = 0;
    public static final int command = 1;


    public RecordLog(OutputStreamWriter writer) throws Exception{
        osw = writer;
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
}
