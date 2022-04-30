package xyz.mainframegames.kalabot.commands.avatar;

import static xyz.mainframegames.kalabot.utils.FunctionsAndPredicates.commandHasXAmountOfWords;

import lombok.extern.slf4j.Slf4j;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import xyz.mainframegames.kalabot.commands.AbstractCommand;
import xyz.mainframegames.kalabot.services.messages.MessagingService;
import xyz.mainframegames.kalabot.utils.BotError;
import xyz.mainframegames.kalabot.utils.Command;
import xyz.mainframegames.kalabot.utils.FunctionsAndPredicates;

@Slf4j
public class AvatarCommand extends AbstractCommand {

  private static final BotError BOT_ERROR = BotError.AVATAR;
  private static final Command COMMAND_TYPE = Command.AVATAR;
  private static final String SIZE = "?size=1024";

  public AvatarCommand(MessagingService messagingService) {
    super(COMMAND_TYPE.getCommandInput(), messagingService);
  }

  @Override
  protected void runCommand(
      MessageCreateEvent event, Server server, ServerTextChannel channel, User user) {
    if (checkNoCommandSyntaxError(event, BOT_ERROR, channel, COMMAND_TYPE, null)) {
      String userAvatarUrl;
      if (commandHasXAmountOfWords(event, 1)) {
        userAvatarUrl = user.getAvatar().getUrl().toString();
      } else {
        User userFuture = FunctionsAndPredicates.getUserFromMentionFuture(user, event);
        userAvatarUrl = userFuture.getAvatar().getUrl().toString();
      }
      messagingService.sendImage(userAvatarUrl + SIZE, channel);
    }
  }
}
