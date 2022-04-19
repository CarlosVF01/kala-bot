package xyz.mainframegames.kalabot.audioplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TrackScheduler extends AudioEventAdapter {

  private final AudioPlayer player;
  private final BlockingQueue<AudioTrack> queue;

  /**
   * @param player The audio player
   */
  public TrackScheduler(AudioPlayer player) {
    this.player = player;
    this.queue = new LinkedBlockingQueue<>();
  }

  /**
   * Add the next track to queue or play right away if nothing is in the queue.
   *
   * @param track The track to play or add to queue.
   */
  public void queue(AudioTrack track) {
    if (!player.startTrack(track, true)) {
      boolean offerResult = queue.offer(track);
      if (!offerResult) {
        log.info("queue: no space available in BlockingQueue");
      }
    }
  }

  /**
   * Start the next track, stopping the current one if it is playing.
   */
  public void nextTrack() {
    player.startTrack(queue.poll(), false);
  }

  @Override
  public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
    if (endReason.mayStartNext) {
      nextTrack();
    }
  }
}
