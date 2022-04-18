package xyz.mainframegames.kalabot.utils;

import java.awt.Color;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import xyz.mainframegames.kalabot.services.messages.MessagingService;

/** Class that contains predicates and functions that are use globally in the application */
public final class FunctionsAndPredicates {

  /** Private constructor to avoid having this class initialized */
  private FunctionsAndPredicates() {}

  public static void sendErrorMessage(
      MessagingService messagingService,
      MessageAuthor messageAuthor,
      TextChannel textChannel,
      Errors error) {
    messagingService.sendMessageEmbed(
        messageAuthor,
        error.getError(),
        error.getDescription(),
        null,
        null,
        Color.BLACK,
        textChannel);
  }
}
