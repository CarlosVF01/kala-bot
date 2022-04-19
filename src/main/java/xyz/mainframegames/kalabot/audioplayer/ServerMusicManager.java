package xyz.mainframegames.kalabot.audioplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class ServerMusicManager {

  /**
   * Audio player for the server.
   */
  public final AudioPlayer player;
  /**
   * Track scheduler for the player.
   */
  public final TrackScheduler scheduler;

  /**
   * Creates a player and a track scheduler.
   *
   * @param manager Audio player manager
   */
  public ServerMusicManager(AudioPlayerManager manager) {
    player = manager.createPlayer();
    scheduler = new TrackScheduler(player);
    player.addListener(scheduler);
  }
}
