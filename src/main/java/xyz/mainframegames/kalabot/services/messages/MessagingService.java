package xyz.mainframegames.kalabot.services.messages;

import org.javacord.api.entity.Icon;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;

import java.awt.*;

public interface MessagingService {
    void sendMessageEmbedWithReactionListenerDelete(MessageAuthor author, String title, String description, String footer, Icon thumbnail, Color color, MessageCreateEvent event);

    void sendMessageEmbed(MessageAuthor author, String title, String description, String footer, String thumbnail, Color color, TextChannel channel);

    void sendImage(Icon image, TextChannel channel);

    void sendImage(String image, TextChannel channel);

    long formatUserId(String word);
}
