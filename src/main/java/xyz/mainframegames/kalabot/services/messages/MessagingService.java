package xyz.mainframegames.kalabot.services.messages;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import xyz.mainframegames.kalabot.data.EmbedMessageData;
import xyz.mainframegames.kalabot.utils.Emoji;

public interface MessagingService {

  /**
   * Sends an embed message that deletes itself if a user reacts with the introduced emoji
   *
   * @param messageData {@link EmbedMessageData} with the information required
   * @param emoji       emoji to use in reaction
   * @param event       message event from the user
   */
  void sendMessageEmbedWithReactionListenerDelete(
      EmbedMessageData messageData,
      Emoji emoji,
      MessageCreateEvent event);

  /**
   * Sends an embed message
   *
   * @param messageData {@link EmbedMessageData} with the information required
   * @param channel     text channel where message will be sent
   */
  void sendMessageEmbed(
      EmbedMessageData messageData,
      TextChannel channel);

  /**
   * Send an embed message fully customized in case it's needed
   *
   * @param messageBuilder embed message builder
   * @param textChannel    channel where message will be sent
   */
  void sendMessageEmbedCustom(MessageBuilder messageBuilder, TextChannel textChannel);

  /**
   * Send an image to the text channel
   *
   * @param imageUrl url to the image
   * @param channel  channel where message will be sent
   */
  void sendImage(String imageUrl, TextChannel channel);
}
