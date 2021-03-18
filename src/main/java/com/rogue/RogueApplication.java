package com.rogue;

import io.dropwizard.Application;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RogueApplication extends Application<BasicRogueConfiguration> {

  public static void main(String[] args) throws Exception {
    /*SpringApplication.run(DemoApplication.class, args);*/
    new RogueApplication().run("server", "dropwizard-config.yml");
  }

  @Override
  public void run(BasicRogueConfiguration basicRogueConfiguration, Environment environment)
      throws Exception {
    environment.jersey().register(new RogueController());
  }

  @Override
  public void initialize(Bootstrap<BasicRogueConfiguration> bootstrap) {
    bootstrap.setConfigurationSourceProvider(new ResourceConfigurationSourceProvider());
    super.initialize(bootstrap);
  }
}
