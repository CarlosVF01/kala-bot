package xyz.mainframegames.kalabot.commands.avatar;

import static xyz.mainframegames.kalabot.utils.FunctionsAndPredicates.sendErrorMessage;

import lombok.extern.slf4j.Slf4j;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Service;
import xyz.mainframegames.kalabot.commands.AbstractCommand;
import xyz.mainframegames.kalabot.services.messages.MessagingService;
import xyz.mainframegames.kalabot.utils.Commands;
import xyz.mainframegames.kalabot.utils.Errors;

@Slf4j
@Service
public class AvatarCommand extends AbstractCommand {

  private static final Errors error = Errors.AVATAR;
  private static final String COMMAND = Commands.AVATAR.toString();
  private static final String SIZE = "?size=1024";

  public AvatarCommand(MessagingService messagingService) {
    super(COMMAND, messagingService);
  }

  @Override
  protected void runCommand(
      MessageCreateEvent event, Server server, ServerTextChannel channel, User user) {
    if (event.getMessageContent().startsWith(COMMAND)) {
      String userAvatarUrl = user.getAvatar().getUrl().toString();
      messagingService.sendImage(userAvatarUrl + SIZE, channel);
    } else {
      sendErrorMessage(messagingService, event.getMessageAuthor(), channel, error);
    }
  }
}
