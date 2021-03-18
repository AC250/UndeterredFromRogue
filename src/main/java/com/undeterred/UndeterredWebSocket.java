package com.undeterred;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebSocket
public class UndeterredWebSocket {

  private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
  private final Logger log = LoggerFactory.getLogger(UndeterredWebSocket.class);

  @OnWebSocketConnect
  public void connected(Session session) {
    log.info("did you just connect man?");
    sessions.add(session);
  }

  @OnWebSocketClose
  public void closed(Session session, int statusCode, String reason) {
    sessions.remove(session);
  }

  @OnWebSocketMessage
  public void message(Session session, String message) throws IOException, InterruptedException {
    log.info("Got a message!, " + message); // Print message
    implementExponentialBackOffForRequest(session);
    session.getRemote().sendString("42: The answer to life, the universe!");
  }

  private Response getRogueResponse() {
    Client client = ClientBuilder.newClient();
    WebTarget resource = client.target("http://localhost:8080/rogue/hello");
    Builder rogueResponse = resource.request();
    rogueResponse.accept(MediaType.APPLICATION_JSON);
    return rogueResponse.get();
  }

  private void implementExponentialBackOffForRequest(final Session session)
      throws IOException, InterruptedException {
    long startTimeGap = 500;
    Response response = getRogueResponse();
    while (response.getStatus() != 200) {
      final String infoMEssage =
          "hmm.. couldnt get the response, retrying in " + startTimeGap / 1000 + " seconds";
      log.info(infoMEssage);
      session.getRemote().sendString(infoMEssage);
      Thread.sleep(startTimeGap);
      response = getRogueResponse();
      if (startTimeGap < 16000) {
        startTimeGap *= 2;
      }
    }
  }
}
