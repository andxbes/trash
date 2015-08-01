/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.rtdeal.io;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;


/**
 * Basic Echo Client Socket
 */
@WebSocket(maxTextMessageSize = 64 * 1024)
public class SimpleEchoSocket {
    private static List<String> cookie;

    private final CountDownLatch closeLatch;

    @SuppressWarnings("unused")
    private Session session;

    public SimpleEchoSocket() {
	this.closeLatch = new CountDownLatch(1);
    }

    public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException {
	return this.closeLatch.await(duration, unit);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
	System.out.printf("Connection closed: %d - %s%n", statusCode, reason);
	this.session = null;
	this.closeLatch.countDown();
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
	System.out.printf("Got connect: %s%n", session);
	this.session = session;
	try {
	    Future<Void> fut;
	    fut = session.getRemote().sendStringByFuture("Hello");
	    fut.get(2, TimeUnit.SECONDS);
	    fut = session.getRemote().sendStringByFuture("Thanks for the conversation.");
	    fut.get(2, TimeUnit.SECONDS);
	    session.close(StatusCode.NORMAL, "I'm done");
	} catch (Throwable t) {
	    t.printStackTrace();
	}
    }

    @OnWebSocketMessage
    public void onMessage(String msg) {
	System.out.printf("Got msg: %s%n", msg);
    }
    
    public static void run(String url,List<String> cookie){
    
    WebSocketClient client = new WebSocketClient();
	SimpleEchoSocket socket = new SimpleEchoSocket();
        try {
            client.start();
            URI echoUri = new URI(url);
            ClientUpgradeRequest request = new ClientUpgradeRequest();
	    request.setHeader("Cookie", cookie);
            client.connect(socket, echoUri, request);
            System.out.printf("Connecting to : %s%n", echoUri);
            socket.awaitClose(5, TimeUnit.SECONDS);
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                client.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	
    
    }
}
