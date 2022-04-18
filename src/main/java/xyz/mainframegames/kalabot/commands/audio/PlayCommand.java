package xyz.mainframegames.kalabot.commands.audio;

import static xyz.mainframegames.kalabot.utils.Regex.HTTPS_URL_REGEX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.mainframegames.kalabot.Api;
import xyz.mainframegames.kalabot.audioplayer.AudioPlayerListener;
import xyz.mainframegames.kalabot.services.messages.MessagingService;
import xyz.mainframegames.kalabot.utils.Commands;

@Service
@Slf4j
public class PlayCommand implements AudioPlayerListener {

  private static final Pattern pattern = Pattern.compile(Commands.PLAY + " " + HTTPS_URL_REGEX);

  @Autowired MessagingService messagingService;

  @Autowired Api api;

  @Override
  public void onMessageCreate(MessageCreateEvent messageCreateEvent) {

    String messageContent = messageCreateEvent.getMessageContent();
    TextChannel textChannel = messageCreateEvent.getChannel();

    if (messageContent.startsWith(Commands.PLAY.toString())) {

      MessageAuthor messageAuthor = messageCreateEvent.getMessageAuthor();
      Matcher matcher = pattern.matcher(messageContent);
    }
  }

  private boolean isVoiceChannelAndMatcherTrue(Matcher matcher, MessageAuthor messageAuthor) {
    return matcher.matches() && messageAuthor.getConnectedVoiceChannel().isPresent();
  }
}
