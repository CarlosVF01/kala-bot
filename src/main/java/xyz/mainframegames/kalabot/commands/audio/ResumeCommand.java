package xyz.mainframegames.kalabot.commands.audio;

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
              AudioPlayer player = AudioManager.get(server.getId()).player;

              if (player.getPlayingTrack() == null || !player.isPaused()) {
                event.getChannel().sendMessage("There's no track paused");

              } else {
                player.setPaused(false);
                event.getChannel().sendMessage("Track resumed");
              }
            },
            () -> event.getChannel().sendMessage(BotError.PLAYING_MUSIC.getDescription()));
  }
}
