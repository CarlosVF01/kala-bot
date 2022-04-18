package xyz.mainframegames.kalabot.commands.rate;

import static xyz.mainframegames.kalabot.utils.FunctionsAndPredicates.sendErrorMessage;

import java.awt.Color;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;
import xyz.mainframegames.kalabot.commands.AbstractCommand;
import xyz.mainframegames.kalabot.services.messages.MessagingService;
import xyz.mainframegames.kalabot.utils.Commands;
import xyz.mainframegames.kalabot.utils.Errors;
import xyz.mainframegames.kalabot.utils.Regex;

@Slf4j
@Component
public class RateCommand extends AbstractCommand {

  private static final Pattern pattern = Pattern.compile(Commands.RATE + " " + Regex.MENTION_REGEX);
  private static final int MAX_RATING_NUMBER = 10;
  private static final String MESSAGE_TITLE = "Rate calculator";
  private static final Random random = new Random();

  public RateCommand(MessagingService messagingService) {
    super(Commands.RATE.toString(), messagingService);
  }

  @Override
  protected void runCommand(
      MessageCreateEvent event, Server server, ServerTextChannel channel, User user) {
    if (event.getMessageContent().startsWith(Commands.RATE.toString())) {

      Matcher matcher = pattern.matcher(event.getMessageContent());
      if (matcher.matches()) {
        int rating = random.nextInt(MAX_RATING_NUMBER);

        messagingService.sendMessageEmbedWithReactionListenerDelete(
            event.getMessageAuthor(),
            MESSAGE_TITLE,
            matcher.group(1) + " is a " + rating + "/" + MAX_RATING_NUMBER,
            null,
            user.getAvatar(),
            Color.BLACK,
            event);
      }
    } else {
      sendErrorMessage(messagingService, event.getMessageAuthor(), channel, Errors.RATE);
    }
  }
}
