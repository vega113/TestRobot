package com.vegalabz.robot;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.waveprotocol.wave.model.id.InvalidIdException;
import org.waveprotocol.wave.model.id.LegacyIdSerialiser;
import org.waveprotocol.wave.model.id.ModernIdSerialiser;
import org.waveprotocol.wave.model.id.WaveId;
import org.waveprotocol.wave.model.waveref.WaveRef;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Singleton;
import com.google.wave.api.AbstractRobot;
import com.google.wave.api.Blip;
import com.google.wave.api.JsonRpcConstant.ParamsProperty;
import com.google.wave.api.JsonRpcResponse;
import com.google.wave.api.Wavelet;

@SuppressWarnings("serial")
@Singleton
public class TestRobot extends AbstractRobot implements WaveCreator {
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

  public String createWaveWithUserAndText(String user, String text) {
    Wavelet wavelet = newWave(WAVE_DOMAIN, ImmutableSet.of(user + "@" + WAVE_DOMAIN));
    Blip rootBlip = wavelet.getRootBlip();
    rootBlip.append(text);
    String dateStr = (new Date()).toString();
    wavelet.setTitle("Test: " + dateStr);
    WaveRef waveRef = null;
    try {
      List<JsonRpcResponse> responses = submit(wavelet, RPC_URL);
      for (JsonRpcResponse response : responses) {
        if (response != null) {
          Map<ParamsProperty, Object> data = response.getData();
          if (data.containsKey(ParamsProperty.WAVELET_ID)) {
            String waveletId =  String.valueOf(data.get(ParamsProperty.WAVELET_ID));
            String waveId =  String.valueOf(data.get(ParamsProperty.WAVE_ID));
            String blipId =  String.valueOf(data.get(ParamsProperty.BLIP_ID));
            try {
              waveRef = WaveRef.of(LegacyIdSerialiser.INSTANCE.deserialiseWaveId(waveId),
                  LegacyIdSerialiser.INSTANCE.deserialiseWaveletId(waveletId), blipId);
            } catch (InvalidIdException e) {
              LOG.log(Level.SEVERE, String.format(
                  "The response contains invalid ids: waveId: %s, waveletId: %s, blipId: %s",
                  waveId, waveletId, blipId), e);
              throw new RuntimeException(e);
            }
          }
        }
      }
    } catch (IOException e) {
      LOG.log(Level.SEVERE, "", e);
    }
    return waveRef.toString();
  }
}
