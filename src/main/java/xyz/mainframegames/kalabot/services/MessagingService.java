package xyz.mainframegames.kalabot.services;

import org.javacord.api.entity.Icon;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;

import java.awt.*;
import java.io.File;

public interface MessagingService {
    void sendMessage(MessageAuthor author, String title, String description, String footer, Icon thumbnail, Color color, TextChannel channel);
    void sendMessage(MessageAuthor author, String title, String description, String footer, String thumbnail, Color color, TextChannel channel);
    void sendImage(Icon image, TextChannel channel);
    void sendImage(String image, TextChannel channel);
    long formatUserId(String word);
}
