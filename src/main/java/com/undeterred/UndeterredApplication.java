package com.undeterred;

import java.util.HashMap;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

@SpringBootApplication
public class UndeterredApplication {

  public static void main(String[] args) {
    Spark.webSocket("/websocket", UndeterredWebSocket.class);
    Spark.get(
        "/",
        (request, response) -> {
          final HashMap<String, Object> hashMap = new HashMap<>();
          return new ThymeleafTemplateEngine().render(new ModelAndView(hashMap, "outputView"));
        });
    Spark.init();
  }
}
