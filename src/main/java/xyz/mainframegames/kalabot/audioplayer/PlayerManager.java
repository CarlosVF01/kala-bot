package xyz.mainframegames.kalabot.audioplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;

public class PlayerManager {

  private static final AudioPlayerManager audioManager = new DefaultAudioPlayerManager();

  private PlayerManager() {}

  /** Youtube source manager initialization */
  public static void init() {
    audioManager.registerSourceManager(new YoutubeAudioSourceManager(true, "", ""));
  }

  /**
   * Retrieves the AudioPlayerManager.
   *
   * @return AudioPlayerManager.
   */
  public static AudioPlayerManager getAudioManager() {
    return audioManager;
  }
}
