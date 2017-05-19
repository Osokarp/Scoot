/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author PrakosoNB
 */
public class Log {
    public static String[] getAllLogs(){
        File f = new File("C:\\");
        String[] list = f.list();
        return list;
    }
    
    public static File[] getAllLogFiles(){
        File f = new File("C:\\");
        File[] list = f.listFiles();
        return list;
    }
    
    public static List<String> getLogLines(String name) throws FileNotFoundException, IOException{
        File[] files = Log.getAllLogFiles();
        List<String> lines = new ArrayList();
           for (File f: files){
               if (f != null && f.isFile() && f.getName().equals(name)){
                   BufferedReader bufferreader = new BufferedReader(new FileReader(f));
                   String s = "";
                   while ((s = bufferreader.readLine()) != null){
                       lines.add(s);
                   }
               }
        }
           return lines;
    }
}
