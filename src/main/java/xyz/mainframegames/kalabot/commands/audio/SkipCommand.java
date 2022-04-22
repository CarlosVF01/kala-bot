package xyz.mainframegames.kalabot.commands.audio;

import static xyz.mainframegames.kalabot.utils.FunctionsAndPredicates.audioPlayerNotPlayingTrackOrNotPaused;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import xyz.mainframegames.kalabot.audioplayer.AudioManager;
import xyz.mainframegames.kalabot.audioplayer.TrackScheduler;
import xyz.mainframegames.kalabot.commands.AbstractCommand;
import xyz.mainframegames.kalabot.services.messages.MessagingService;
import xyz.mainframegames.kalabot.utils.BotError;
import xyz.mainframegames.kalabot.utils.Command;

public class SkipCommand extends AbstractCommand {

  private static final String NOT_PLAYING = "There's no track playing";
  private static final String SKIPPED = "Track skipped";

  public SkipCommand(MessagingService messagingService) {
    super(Command.SKIP.toString(), messagingService);
  }

  @Override
  protected void runCommand(
      MessageCreateEvent event, Server server, ServerTextChannel channel, User user) {
    server
        .getAudioConnection()
        .ifPresentOrElse(
            connection -> {
              TrackScheduler scheduler = AudioManager.getServerManager(server.getId()).scheduler;
              AudioPlayer player = AudioManager.getServerManager(server.getId()).player;

              if (audioPlayerNotPlayingTrackOrNotPaused(player)) {
                event.getChannel().sendMessage(NOT_PLAYING);

              } else {
                scheduler.nextTrack();
                event.getChannel().sendMessage(SKIPPED);
              }
            },
            () -> event.getChannel().sendMessage(BotError.NOT_PLAYING_MUSIC.getDescription()));
  }
}
