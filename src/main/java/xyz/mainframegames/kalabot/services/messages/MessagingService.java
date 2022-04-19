package xyz.mainframegames.kalabot.services.messages;

import java.awt.Color;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import xyz.mainframegames.kalabot.utils.Emoji;

public interface MessagingService {

  void sendMessageEmbedWithReactionListenerDelete(
      MessageAuthor author,
      String title,
      String description,
      String footer,
      Icon thumbnail,
      Color color,
      Emoji emoji,
      MessageCreateEvent event);

  void sendMessageEmbed(
      MessageAuthor author,
      String title,
      String description,
      String footer,
      String thumbnail,
      Color color,
      TextChannel channel);

  void sendMessageEmbedCustom(MessageBuilder messageBuilder, TextChannel textChannel);

  void sendImage(String image, TextChannel channel);

  /**
   * Formats the user IDs that the bot gets from a mention. All of them have 18 numbers that
   * represent the ID But when doing a mention the ID given can look like this based on if their
   * nickname is changed or not: Example 1: <@!123456789123456789> Example 2: <@123456789123456789>
   * So to use the User ID first it needs to be formatted to one that only has numbers.
   *
   * @param fullId the complete user id with extra characters
   * @return the user id with only numbers
   */
  long formatUserId(String fullId);
}
