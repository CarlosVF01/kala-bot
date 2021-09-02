package xyz.mainframegames.kalabot.services;

import org.javacord.api.entity.Icon;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;

public interface MessagingService {
    void sendMessage(MessageAuthor author, String title, String description, String footer, Icon thumbnail, int[] colors, TextChannel channel);
    void sendMessage(MessageAuthor author, String title, String description, String footer, String thumbnail, int[] colors, TextChannel channel);
    long formatUserId(String word);
}
