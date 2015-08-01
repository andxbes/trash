/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.rtdeal.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;


/**
 *
 * @author Andr
 */
public class Start {

    
    
    
    
    static  String url = "ws://rtdeal.com:8082/socket.io/?EIO=3&transport=websocket",
	    /*cookie*/
	   _identity = "3528f575dbe29f4c16e0eb2d15fe5da15209f4bd699844e2defc5c3175fe372aa%3A2%3A%7Bi%3A0%3Bs%3A9%3A%22_identity%22%3Bi%3A1%3Bs%3A48%3A%22%5B315%2C%22QI3caisV2ehZFAKgm-3MmO6ZkW7eF0CH%22%2C2592000%5D%22%3B%7D; ";
    static final Map<String, String> header = new HashMap<>();
    static List<String> coo= new ArrayList<>(1);
    
    public static void main(String... args) throws URISyntaxException, MalformedURLException {
        String sid = null;
	Query q = new Query();
	q.queryGet(new URL("http://rtdeal.com:8082/socket.io/?EIO=3&transport=polling&t=console"),
		"_identity="+_identity);
	
	sid = q.get("sid");
	
	
	setCookie(sid, _identity);
	setUrl(sid);
	

        C2 c = new C2(url, header);
	c.con();
       //SimpleEchoSocket.run(url,coo);
    }

    static void setUrl(String sid){
        url +="&sid=" + sid;
	System.out.println(url);
    }
    
    
    static  void setCookie(String io,String _identity){
        
	String cookie = "io="        + io        + "; " + 
		        "_identity=" + _identity ;                             
	System.out.println(cookie);
	header.put("Cookie", cookie);
	coo.add(cookie);
    }
   
   
    
}


class Query{
   // private String cookie;
    private String response;
    private int code ;
   
    public void queryGet(URL url,String cookie){
        StringBuilder result = new StringBuilder();
	HttpURLConnection conn = null;
	BufferedReader br = null;
	String line;
	
	try {
	    conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestProperty("Cookie", cookie);
	    conn.setRequestMethod("GET");
	    
	    code = conn.getResponseCode();
	    if(code == 200){
		br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	    }else {
	        br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
	    }
	    while ((line = br.readLine())!=null) {
		
		if(code == 200){
		  result.append(line).append("\n");
		}
	    }
	    response = result.toString();
	    System.out.println(response);
	} catch (IOException ex) {
	    Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
	}
	
    
    }
    public String get(String val){

	response = "{"+response.split("[\\{\\}]")[1]+"}";
	System.out.println(response);
      JSONObject jo = new JSONObject(response);
       return jo.getString(val);
    }


}
