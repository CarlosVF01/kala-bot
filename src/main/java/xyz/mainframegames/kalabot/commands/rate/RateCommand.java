package xyz.mainframegames.kalabot.commands.rate;

import static xyz.mainframegames.kalabot.utils.Emoji.THUMBS_DOWN;
import static xyz.mainframegames.kalabot.utils.FunctionsAndPredicates.embedMessageDataBuilder;
import static xyz.mainframegames.kalabot.utils.Names.RATE_COMMAND_TITLE;

import java.awt.Color;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import xyz.mainframegames.kalabot.commands.AbstractCommand;
import xyz.mainframegames.kalabot.data.EmbedMessageData;
import xyz.mainframegames.kalabot.services.messages.MessagingService;
import xyz.mainframegames.kalabot.utils.BotError;
import xyz.mainframegames.kalabot.utils.Command;
import xyz.mainframegames.kalabot.utils.FunctionsAndPredicates;
import xyz.mainframegames.kalabot.utils.Regex;

@Slf4j
public class RateCommand extends AbstractCommand {

  private static final Pattern pattern = Pattern.compile(Command.RATE + " " + Regex.MENTION_REGEX);
  private static final int MAX_RATING_NUMBER = 10;
  private static final Random random = new Random();
  private static final String NOT_LIKING_RATING = "If you didn't like this rating \nyou have 30 seconds to ";

  public RateCommand(MessagingService messagingService) {
    super(Command.RATE.toString(), messagingService);
  }

  @Override
  protected void runCommand(
      MessageCreateEvent event, Server server, ServerTextChannel channel, User user) {

    if (checkNoCommandSyntaxError(event, BotError.RATE, channel, Command.RATE, pattern)) {
      Matcher matcher = pattern.matcher(event.getMessageContent());
      if (matcher.matches()) {
        int rating = random.nextInt(MAX_RATING_NUMBER);
        User userFuture = FunctionsAndPredicates.getUseFromMentionFuture(user, event);

        String userMentioned = matcher.group(1);

        MessageAuthor messageAuthor = event.getMessageAuthor();
        String footer = NOT_LIKING_RATING + THUMBS_DOWN;
        Color color = Color.BLACK;
        String title = RATE_COMMAND_TITLE.toString();
        String description = userMentioned + " is a " + rating + "/" + MAX_RATING_NUMBER;
        Icon thumbnail = userFuture.getAvatar();

        EmbedMessageData messageData = embedMessageDataBuilder(messageAuthor, title, description, footer, thumbnail, color);

        messagingService.sendMessageEmbedWithReactionListenerDelete(
            messageData,
            THUMBS_DOWN,
            event);
      }
    }
  }
}
