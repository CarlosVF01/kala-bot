package xyz.mainframegames.kalabot.services.messages;

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

@Service
@Slf4j
public class MessagingServiceImpl implements MessagingService {

  static final int EXCLAMATION_INDEX = 2;
  static final int NICKNAME_NOT_CHANGED_INDEX_START = 2;
  static final int NICKNAME_CHANGED_INDEX_START = 3;

  @Override
  public void sendMessageEmbedWithReactionListenerDelete(
      MessageAuthor author,
      String title,
      String description,
      String footer,
      Icon thumbnail,
      Color color,
      MessageCreateEvent event) {
    event
        .getChannel()
        .addReactionAddListener(
            reactionAddEvent -> {
              Optional<Message> message = reactionAddEvent.getMessage();
              if (message.isPresent()) {
                if (reactionAddEvent.getEmoji().equalsEmoji("👎")
                    && message.get().getAuthor().getName().equals(BOT_NAME.toString())) {
                  event.getChannel().sendMessage("Sorry you didn't like it \uD83D\uDE14");
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
                .setAuthor(author)
                .setTitle(title)
                .setDescription(description)
                .setFooter(footer)
                .setThumbnail(thumbnail)
                .setColor(color))
        .send(event.getChannel());
  }

  @Override
  public void sendMessageEmbed(
      MessageAuthor author,
      String title,
      String description,
      String footer,
      String thumbnail,
      Color color,
      TextChannel channel) {
    new MessageBuilder()
        .setEmbed(
            new EmbedBuilder()
                .setAuthor(author)
                .setTitle(title)
                .setDescription(description)
                .setFooter(footer)
                .setThumbnail(thumbnail)
                .setColor(color))
        .send(channel);
  }

  @Override
  public void sendImage(String image, TextChannel channel) {
    new MessageBuilder().setEmbed(new EmbedBuilder().setImage(image)).send(channel);
  }

  @Override
  public long formatUserId(String fullId) {

    return Long.parseLong(
        fullId.substring(
            Character.isDigit(fullId.charAt(EXCLAMATION_INDEX))
                ? NICKNAME_NOT_CHANGED_INDEX_START
                : NICKNAME_CHANGED_INDEX_START,
            fullId.length() - 1));
  }
}
