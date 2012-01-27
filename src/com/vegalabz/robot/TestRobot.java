package com.vegalabz.robot;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Singleton;
import com.google.wave.api.AbstractRobot;
import com.google.wave.api.Blip;
import com.google.wave.api.Wavelet;


@SuppressWarnings("serial")
@Singleton
public class TestRobot extends AbstractRobot implements WaveCreator{
	private static final Logger LOG = Logger.getLogger(TestRobot.class.getName());
	
	public static final String RPC_URL = "http://waveinabox.net:80/robot/rpc";
	public static final String WAVE_DOMAIN = "waveinabox.net";
	
	// Acquire at http://waveinabox.net/robot/register/create (or your own WIAB server).
	public static final String CONSUMER_TOKEN = "waveaids@waveinabox.net";
	public static final String CONSUMER_TOKEN_SECRET = "Vs4GJMH6z3ffi5pNJmJTpsxzesjwrNTkFUv_JHolshXsE_B9";

	@Override
	protected String getRobotName() {
		return "Waveaids";
	}

	@Override
	protected String getRobotProfilePageUrl() {
		return null;
	}
	
	public TestRobot() {
		setupOAuth(CONSUMER_TOKEN, CONSUMER_TOKEN_SECRET, RPC_URL);
		setAllowUnsignedRequests(true);
	}
	
	public void createWaveWithUserAndText(String user, String text) {
		Wavelet wavelet = newWave(WAVE_DOMAIN, ImmutableSet.of(user + "@" + WAVE_DOMAIN));
		Blip rootBlip = wavelet.getRootBlip();
		rootBlip.append(text);
		String dateStr = (new Date()).toString();
		wavelet.setTitle("Test: " + dateStr);
		try {
			submit(wavelet, RPC_URL);
		} catch (IOException e) {
			LOG.log(Level.SEVERE, "", e);
		}
	}
}
