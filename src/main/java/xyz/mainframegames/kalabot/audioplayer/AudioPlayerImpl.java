package xyz.mainframegames.kalabot.audioplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
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
import xyz.mainframegames.kalabot.utils.enums.Regex;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static xyz.mainframegames.kalabot.utils.enums.Names.BOT_NAME;
import static xyz.mainframegames.kalabot.utils.enums.Regex.HTTPS_URL_REGEX;

@Component
@Slf4j
public class AudioPlayerImpl implements AudioPlayerListener{
    private static final Pattern pattern = Pattern.compile(Commands.PLAY + " " + HTTPS_URL_REGEX);

    @Autowired
    MessagingService messagingService;

    @Autowired
    private Api api;

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {

        String messageContent = messageCreateEvent.getMessageContent();

        if (messageContent.startsWith(Commands.PLAY.toString())) {

            MessageAuthor messageAuthor = messageCreateEvent.getMessageAuthor();
            TextChannel textChannel = messageCreateEvent.getChannel();
            Matcher matcher = pattern.matcher(messageContent);

            AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
            playerManager.registerSourceManager(new YoutubeAudioSourceManager());
            AudioPlayer player = playerManager.createPlayer();

            // Create an audio source and add it to the audio connection's queue
            AudioSource source = new LavaplayerAudioSource(api.getDiscordApi(), player);

            if (matcher.matches()){
                if(messageAuthor.getConnectedVoiceChannel().isPresent()){
                    ServerVoiceChannel voiceChannel = messageAuthor.getConnectedVoiceChannel().get();

                    voiceChannel.connect().thenAccept(audioConnection -> {
                        audioConnection.setAudioSource(source);

                        playerManager.loadItem(matcher.group(1), new AudioLoadResultHandler() {
                            @Override
                            public void trackLoaded(AudioTrack track) {
                                player.playTrack(track);
                            }

                            @Override
                            public void playlistLoaded(AudioPlaylist playlist) {
                                for (AudioTrack track : playlist.getTracks()) {
                                    player.playTrack(track);
                                }
                            }

                            @Override
                            public void noMatches() {
                                log.error("no match for {}", messageContent);
                                // Notify the user that we've got nothing
                            }

                            @Override
                            public void loadFailed(FriendlyException throwable) {
                                log.error("test error failed");
                                // Notify the user that everything exploded
                            }
                        });
                    }).exceptionally(e -> {
                        log.error("Couldn't join channel {}", voiceChannel.getName());
                        // Failed to connect to voice channel (no permissions?)
                        e.printStackTrace();
                        return null;
                    });
                }
            }else if(!messageCreateEvent.getMessageAuthor().getName().equals(BOT_NAME.toString())) {
                messagingService.sendMessageEmbed(
                        messageCreateEvent.getMessageAuthor(),
                        Errors.PLAY.getError(),
                        Errors.PLAY.getDescription(),
                        null,
                        null,
                        Color.BLACK,
                        textChannel);
            }
        }
    }
}
