package com.vegalabz.servlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vegalabz.robot.WaveCreator;

@SuppressWarnings("serial")
@Singleton
public class CreateWaveletServlet extends HttpServlet {
  Logger LOG = Logger.getLogger(CreateWaveletServlet.class.getName());

  private final WaveCreator waveCreator;

  @Inject
  public CreateWaveletServlet(WaveCreator waveCreator) {
    this.waveCreator = waveCreator;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String username = req.getParameter("username");
    String message = req.getParameter("message");
    String waveRef = null;
    try {
      waveRef = waveCreator.createWaveWithUserAndText(username, message);
    } catch (Exception e) {
      LOG.log(Level.SEVERE, "Failed to create wave for user " + username
          + " and message:" + message, e);
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      resp.getWriter().print("Failed! " + e.getMessage());
      resp.getWriter().flush();
      return;
    }
    resp.setStatus(HttpServletResponse.SC_OK);
    resp.getWriter().print("Success! " + waveRef);
    resp.getWriter().flush();
  }
}
