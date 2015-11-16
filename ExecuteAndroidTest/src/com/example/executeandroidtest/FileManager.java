package com.example.executeandroidtest;

import java.io.File;
import java.io.FileInputStream;

import org.apache.http.util.EncodingUtils;

import android.util.Log;

public class FileManager {
	public static final String TAG = "ExtcuteAndroidTest - FileManager";
	
	public String readFromSDcard(String fileName){
		String res="";
		try{ 
			File file = new File(fileName);
			if (file.exists()){
					FileInputStream fin = new FileInputStream(fileName);
					int length = fin.available(); 
					byte [] buffer = new byte[length]; 
					fin.read(buffer);     
					res = EncodingUtils.getString(buffer, "UTF-8"); 
					fin.close();     
			    }
			else{
				Log.v(TAG, "TestcaseConfig.cfg file not exists!");
			}
			}catch(Exception e){ 
			           e.printStackTrace(); 
			} 
	    return res;
		}
	}

