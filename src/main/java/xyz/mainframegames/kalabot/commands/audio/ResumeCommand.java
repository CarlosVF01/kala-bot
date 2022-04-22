package xyz.mainframegames.kalabot.commands.audio;

import static xyz.mainframegames.kalabot.utils.FunctionsAndPredicates.audioPlayerNotPlayingTrackOrNotPaused;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import xyz.mainframegames.kalabot.audioplayer.AudioManager;
import xyz.mainframegames.kalabot.commands.AbstractCommand;
import xyz.mainframegames.kalabot.services.messages.MessagingService;
import xyz.mainframegames.kalabot.utils.BotError;
import xyz.mainframegames.kalabot.utils.Command;

public class ResumeCommand extends AbstractCommand {

  private static final String NOT_PAUSED = "There's no track paused";
  private static final String RESUMED = "Track resumed";

  public ResumeCommand(MessagingService messagingService) {
    super(Command.RESUME.toString(), messagingService);
  }

  @Override
  protected void runCommand(
      MessageCreateEvent event, Server server, ServerTextChannel channel, User user) {
    server
        .getAudioConnection()
        .ifPresentOrElse(
            connection -> {
              AudioPlayer player = AudioManager.getServerManager(server.getId()).player;

              if (audioPlayerNotPlayingTrackOrNotPaused(player)) {
                event.getChannel().sendMessage(NOT_PAUSED);

              } else {
                player.setPaused(false);
                event.getChannel().sendMessage(RESUMED);
              }
            },
            () -> event.getChannel().sendMessage(BotError.NOT_PLAYING_MUSIC.getDescription()));
  }

}
