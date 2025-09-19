package com.webforj.webswing.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.webforj.App;
import com.webforj.annotation.AppProfile;
import com.webforj.annotation.Routify;

@SpringBootApplication
@Routify(packages = "com.webforj.webswing.demo.views")
@AppProfile(name = "webforj Webswing Integration Demo", shortName = "webforj Webswing Integration Demo")
public class Application extends App {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
