package com.rogue;

import java.util.Random;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@Path("/rogue")
public class RogueController {

  public RogueController() {}

  private static long TIME_AT_INIT = System.currentTimeMillis();
  private static long RANDOM_WAIT = (long) (Math.random() * 10000);
  private static int RESPONSE = new Random().nextInt(3);

  private static void setRandomTimeAndResponse() {
    RESPONSE = new Random().nextInt(3);
    RANDOM_WAIT = (long) (Math.random() * 10000);
    TIME_AT_INIT = System.currentTimeMillis();
  }

  @GET
  @Path("/hello")
  public Response getRogueResponse() {
    if (System.currentTimeMillis() - TIME_AT_INIT < RANDOM_WAIT) {
      switch (RESPONSE) {
        case 1:
          return Response.status(503).entity("response should be 503").build();
        case 2:
          return Response.status(504).entity("response should be 504").build();
        default:
          return Response.status(200).entity("response should be 200").build();
      }
    } else {
      setRandomTimeAndResponse();
      return Response.status(200).entity("response should be 200").build();
    }
  }
}
