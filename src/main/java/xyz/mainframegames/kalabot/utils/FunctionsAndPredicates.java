package xyz.mainframegames.kalabot.utils;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import java.awt.Color;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import xyz.mainframegames.kalabot.data.EmbedMessageData;
import xyz.mainframegames.kalabot.exception.DiscordApiCallException;
import xyz.mainframegames.kalabot.services.messages.MessagingService;

/**
 * Class that contains predicates and functions that are use globally in the application
 */
public final class FunctionsAndPredicates {

  public static final int FIRST = 0;

  private static final int EXCLAMATION_INDEX = 2;
  private static final int NICKNAME_NOT_CHANGED_INDEX_START = 2;
  private static final int NICKNAME_CHANGED_INDEX_START = 3;

  /**
   * Private constructor to avoid having this class initialized
   */
  private FunctionsAndPredicates() {
  }

  /**
   * Sends an error message based on the enum variable introduced {@link BotError}
   *
   * @param messagingService messaging service that will send the message
   * @param messageAuthor    author of the message
   * @param textChannel      text channel where message will be sent
   * @param botError         error title and description
   */
  public static void sendErrorMessage(
      MessagingService messagingService,
      MessageAuthor messageAuthor,
      TextChannel textChannel,
      BotError botError) {
    EmbedMessageData embedMessageData = embedMessageDataBuilder(messageAuthor, botError.getError(),
        botError.getDescription(), null, null, Color.BLACK);
    messagingService.sendMessageEmbed(
        embedMessageData,
        textChannel);
  }

  /**
   * Checks if the audio player is not playing a track or is not paused
   *
   * @param player audio player
   * @return boolean with the result
   */
  public static boolean audioPlayerNotPlayingTrackOrNotPaused(AudioPlayer player) {
    return player.getPlayingTrack() == null || !player.isPaused();
  }

  /**
   * Calls the discord api to get a user, waiting up to 3 seconds
   *
   * @param user  user that sent the message
   * @param event message event
   * @return the user obtained from the ID
   */
  public static User getUserFromMentionFuture(User user, MessageCreateEvent event) {
    User userFuture;
    try {
      userFuture = user.getApi()
          .getUserById(formatUserId(event.getMessageContent().split(" ")[1]))
          .get(3000L, TimeUnit.MILLISECONDS);
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      Thread.currentThread().interrupt();
      throw new DiscordApiCallException(e.getMessage());
    }
    return userFuture;
  }

  /**
   * Formats the user IDs that the bot gets from a mention. All of them have 18 numbers that
   * represent the ID. But when doing a mention the ID given can look like this based on if their
   * nickname is changed or not: Example 1: <@!123456789123456789> Example 2: <@123456789123456789>
   * So to use the User ID first it needs to be formatted to one that only has numbers.
   *
   * @param fullId the complete user id with extra characters
   * @return the user id with only numbers
   */
  private static long formatUserId(String fullId) {

    return Long.parseLong(
        fullId.substring(
            Character.isDigit(fullId.charAt(EXCLAMATION_INDEX))
                ? NICKNAME_NOT_CHANGED_INDEX_START
                : NICKNAME_CHANGED_INDEX_START,
            fullId.length() - 1));
  }

  /**
   * Creates a {@link EmbedMessageData} using a builder
   *
   * @return {@link EmbedMessageData}
   */
  public static EmbedMessageData embedMessageDataBuilder(
      MessageAuthor author,
      String title,
      String description,
      String footer,
      String thumbnail,
      Color color) {

    return EmbedMessageData.builder().author(author)
        .footer(footer).color(color)
        .title(title).description(description).thumbnail(thumbnail).build();
  }

  public static boolean commandHasXAmountOfWords(MessageCreateEvent event, int amountOfWords) {
    return event.getMessageContent().trim().split(" ").length == amountOfWords;
  }
}
