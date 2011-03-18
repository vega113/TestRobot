package com.vegalabz.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.vegalabz.register.RegisterRobotServlet;
import com.vegalabz.robot.TestRobot;

public class GuiceServletConfig extends GuiceServletContextListener {

  @Override
  protected Injector getInjector() {
    ServletModule servletModule = new ServletModule() {
      @Override
      protected void configureServlets() {
        serveRegex("\\/_wave/.*").with(TestRobot.class);
        serve("/_wave/verify_token").with(RegisterRobotServlet.class);
      }
    };

    AbstractModule businessModule = new AbstractModule() {
      @Override
      protected void configure() {
      }

    };

    return Guice.createInjector(servletModule, businessModule);
  }
}
