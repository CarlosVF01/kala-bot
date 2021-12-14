package xyz.mainframegames.kalabot.audioplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import lombok.extern.slf4j.Slf4j;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.mainframegames.kalabot.Api;
import xyz.mainframegames.kalabot.services.messages.MessagingService;
import xyz.mainframegames.kalabot.utils.enums.Commands;
import xyz.mainframegames.kalabot.utils.enums.Errors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static xyz.mainframegames.kalabot.utils.enums.FunctionsAndPredicates.authorIsNotBot;
import static xyz.mainframegames.kalabot.utils.enums.FunctionsAndPredicates.sendErrorMessage;

import static xyz.mainframegames.kalabot.utils.enums.Regex.HTTPS_URL_REGEX;

@Component
@Slf4j
public class AudioPlayerImpl implements AudioPlayerListener {
    private static final Pattern pattern = Pattern.compile(Commands.PLAY + " " + HTTPS_URL_REGEX);

    @Autowired
    MessagingService messagingService;

    @Autowired
    private Api api;

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {

        String messageContent = messageCreateEvent.getMessageContent();
        TextChannel textChannel = messageCreateEvent.getChannel();

        if (messageContent.startsWith(Commands.PLAY.toString())) {

            MessageAuthor messageAuthor = messageCreateEvent.getMessageAuthor();
            Matcher matcher = pattern.matcher(messageContent);

            if (isVoiceChannelAndMatcherTrue(matcher, messageAuthor)) {
                AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
                playerManager.registerSourceManager(new YoutubeAudioSourceManager());
                TrackScheduler audioEventListener = new TrackScheduler();
                AudioPlayer audioPlayer = playerManager.createPlayer();
                AudioHandler audioHandler = new AudioHandler(audioPlayer);

                audioPlayer.addListener(audioEventListener);

                // Create an audio source and add it to the audio connection's queue
                AudioSource source = new LavaplayerAudioSource(api.getDiscordApi(), audioPlayer);
                ServerVoiceChannel voiceChannel = messageAuthor.getConnectedVoiceChannel().get();

                voiceChannel.connect().thenAccept(audioConnection -> {
                    audioConnection.setAudioSource(source);

                    playerManager.loadItem(matcher.group(1), audioHandler);
                }).exceptionally(e -> {
                    log.error("Couldn't join channel {}", voiceChannel.getName());
                    // Failed to connect to voice channel (no permissions?)
                    e.printStackTrace();
                    return null;
                });
            }
        } else if (authorIsNotBot.test(messageCreateEvent)) {
            sendErrorMessage(messagingService, messageCreateEvent.getMessageAuthor(), textChannel, Errors.PLAY);
        }
    }


    private boolean isVoiceChannelAndMatcherTrue(Matcher matcher, MessageAuthor messageAuthor) {
        return matcher.matches() && messageAuthor.getConnectedVoiceChannel().isPresent();
    }
}
