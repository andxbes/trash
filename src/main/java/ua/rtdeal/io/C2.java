/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.rtdeal.io;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;

/**
 *
 * @author Andr
 */
public class C2 {
private String url;
    Map<String, String> headler;
    public C2(String url, Map<String,String> headler) {
	this.headler=headler;
	this.url=url;
	
    }
    
    
     void con() throws JSONException, URISyntaxException {
	WebSocketImpl.DEBUG = true;
	WebSocketClient mWs;
	mWs = new WebSocketClient(new URI(url), new Draft_17(), headler, 6000) {
	    @Override
	    public void onMessage(String message) {
//		JSONObject obj = new JSONObject(message);
//		String channel = obj.getString("channel");
		System.out.println(message);
	    }
	    
	    

	    @Override
	    public void onOpen(ServerHandshake handshake) {
		System.out.println("opened connection");
		
	    }

	    public void onFragment(Framedata fragment) {
		System.out.println("received fragment: " + new String(fragment.getPayloadData().array()));
	    }

	    @Override
	    public void onClose(int code, String reason, boolean remote) {
		System.out.println("closed connection , remote:" + remote);
	    }

	    @Override
	    public void onError(Exception ex) {
		ex.printStackTrace();
	    }
	    
	    
	    
	};

	mWs.connect();
	System.out.println("=============" + mWs.getConnection().isOpen() + "==============");
//	JSONObject obj = new JSONObject();
//	obj.put("event", "addChannel");
//	obj.put("channel", "ok_btccny_ticker");
//	String message = obj.toString();
	//send message
	//mWs.send("\"g_message\",{\"cmd\":\"global_msg_new\",\"from\":315,\"to\":null,\"data\":{\"user\":{\"id\":315,\"username\":\"coder4test\"},\"message\":\"test\",\"created_at\":1438377200},\"time\":1438375200}");
    }
}
