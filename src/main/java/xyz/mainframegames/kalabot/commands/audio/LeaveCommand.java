package xyz.mainframegames.kalabot.commands.audio;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import xyz.mainframegames.kalabot.audioplayer.AudioManager;
import xyz.mainframegames.kalabot.commands.AbstractCommand;
import xyz.mainframegames.kalabot.services.messages.MessagingService;
import xyz.mainframegames.kalabot.utils.BotError;
import xyz.mainframegames.kalabot.utils.Command;

public class LeaveCommand extends AbstractCommand {

  public LeaveCommand(MessagingService messagingService) {
    super(Command.LEAVE.toString(), messagingService);
  }

  @Override
  protected void runCommand(
      MessageCreateEvent event, Server server, ServerTextChannel channel, User user) {
    server
        .getConnectedVoiceChannel(event.getApi().getYourself())
        .ifPresentOrElse(
            voiceChannel ->
                server
                    .getAudioConnection()
                    .ifPresentOrElse(
                        connection -> {
                          AudioManager.getServerManager(server.getId()).player.stopTrack();
                          connection.close();
                        },
                        () -> event.getChannel()
                            .sendMessage(BotError.BOT_NOT_IN_A_VOICE_CHANNEL.getDescription())),
            () -> event.getChannel()
                .sendMessage(BotError.BOT_NOT_IN_A_VOICE_CHANNEL.getDescription()));
  }
}
