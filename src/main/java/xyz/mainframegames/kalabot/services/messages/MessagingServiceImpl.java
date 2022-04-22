package xyz.mainframegames.kalabot.services.messages;

import static xyz.mainframegames.kalabot.utils.Emoji.SAD_FACE;
import static xyz.mainframegames.kalabot.utils.Names.BOT_NAME;

import java.awt.Color;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Service;
import xyz.mainframegames.kalabot.data.EmbedMessageData;
import xyz.mainframegames.kalabot.utils.Emoji;

@Service
@Slf4j
public class MessagingServiceImpl implements MessagingService {

  private static final String SORRY_REACTION = "Sorry you didn't like it ";

  @Override
  public void sendMessageEmbedWithReactionListenerDelete(
      EmbedMessageData messageData,
      Emoji emoji,
      MessageCreateEvent event) {
    event
        .getChannel()
        .addReactionAddListener(
            reactionAddEvent -> {
              Optional<Message> message = reactionAddEvent.getMessage();
              if (message.isPresent()) {
                if (reactionAddEvent.getEmoji().equalsEmoji(emoji.toString())
                    && message.get().getAuthor().getName().equals(BOT_NAME.toString())) {
                  event.getChannel().sendMessage(SORRY_REACTION + SAD_FACE);
                  reactionAddEvent.deleteMessage();
                }
              } else {
                log.error("sendMessageEmbedWithReactionListenerDelete: Message not found");
              }
            })
        .removeAfter(30, TimeUnit.SECONDS);

    new MessageBuilder()
        .setEmbed(
            new EmbedBuilder()
                .setAuthor(messageData.getAuthor())
                .setTitle(messageData.getTitle())
                .setDescription(messageData.getDescription())
                .setFooter(messageData.getFooter())
                .setThumbnail(messageData.getThumbnail())
                .setColor(messageData.getColor()))
        .send(event.getChannel());
  }

  @Override
  public void sendMessageEmbed(
      EmbedMessageData messageData,
      TextChannel channel) {
    new MessageBuilder()
        .setEmbed(
            new EmbedBuilder()
                .setAuthor(messageData.getAuthor())
                .setTitle(messageData.getTitle())
                .setDescription(messageData.getDescription())
                .setFooter(messageData.getFooter())
                .setThumbnail(messageData.getThumbnail())
                .setColor(messageData.getColor()))
        .send(channel);
  }

  @Override
  public void sendMessageEmbedCustom(MessageBuilder messageBuilder, TextChannel textChannel) {
    messageBuilder.send(textChannel);
  }

  @Override
  public void sendImage(String imageUrl, TextChannel channel) {
    new MessageBuilder().setEmbed(new EmbedBuilder().setImage(imageUrl)).send(channel);
  }
}
