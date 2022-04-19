package xyz.mainframegames.kalabot.audioplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;

public class PlayerManager {

  private static final AudioPlayerManager audioManager = new DefaultAudioPlayerManager();

  /**
   * This is only here since we want to initialize the thing from the start, from the YouTube
   * Source, etc.
   */
  public static void init() {
    audioManager.registerSourceManager(new YoutubeAudioSourceManager(true));
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
