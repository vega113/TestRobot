package com.vegalabz.robot;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.repackaged.com.google.common.collect.Sets;
import com.google.inject.Singleton;
import com.google.wave.api.AbstractRobot;
import com.google.wave.api.Wavelet;
import com.google.wave.api.event.WaveletSelfAddedEvent;
import com.vegalabz.guice.GuiceServletConfig;


@SuppressWarnings("serial")
@Singleton
public class TestRobot extends AbstractRobot {
	private static final Logger LOG = Logger.getLogger(TestRobot.class.getName());
	public static final String PREVIEW_RPC_URL = "http://gmodules.com/api/rpc";
	public static final String PREVIEW_DOMAIN = "googlewave.com";

	@Override
	protected String getRobotName() {
		return "Test Wave robot";
	}

	@Override
	protected String getRobotProfilePageUrl() {
		return null;
	}
	
	public TestRobot() {
		String consumerKey = "";
		String consumerSecret = "";
		setupOAuth(consumerKey, consumerSecret, PREVIEW_RPC_URL);
		setAllowUnsignedRequests(true);
	}

	@Override
	public void onWaveletSelfAdded(WaveletSelfAddedEvent event) {
		String dateStr = (new Date()).toString();
		@SuppressWarnings("deprecation")
		// NOTE Replace with your Google Wave ID.
		Wavelet wavelet = newWave(PREVIEW_DOMAIN, Sets.newSortedArraySet("vega113@googlewave.com"));
		wavelet.getRootBlip().append("The time is: " + dateStr);
		wavelet.setTitle("Test: " + dateStr);
		try {
			submit(wavelet, PREVIEW_RPC_URL);
		} catch (IOException e) {
			LOG.log(Level.SEVERE, "", e);
		}
	}
	
	

}
