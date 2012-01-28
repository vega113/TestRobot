package com.vegalabz.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.vegalabz.robot.TestRobot;
import com.vegalabz.robot.WaveCreator;
import com.vegalabz.servlet.CreateWaveletServlet;

public class GuiceServletConfig extends GuiceServletContextListener {

  @Override
  protected Injector getInjector() {
    ServletModule servletModule = new ServletModule() {
      @Override
      protected void configureServlets() {
        serveRegex("\\/_wave/.*").with(TestRobot.class);
        serve("/createwave").with(CreateWaveletServlet.class);
      }
    };

    AbstractModule businessModule = new AbstractModule() {
      @Override
      protected void configure() {
        bind(WaveCreator.class).to(TestRobot.class);
      }
    };
    return Guice.createInjector(servletModule, businessModule);
  }
}
