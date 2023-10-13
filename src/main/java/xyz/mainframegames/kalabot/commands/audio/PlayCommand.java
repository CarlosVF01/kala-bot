package xyz.mainframegames.kalabot.commands.audio;

import static xyz.mainframegames.kalabot.utils.BotError.AUDIO_TRACK_EXCEPTION;
import static xyz.mainframegames.kalabot.utils.BotError.BOT_PERMISSION_ERROR;
import static xyz.mainframegames.kalabot.utils.BotError.NOT_IN_SAME_VOICE_CHANNEL;
import static xyz.mainframegames.kalabot.utils.BotError.USER_NOT_IN_A_VOICE_CHANNEL;
import static xyz.mainframegames.kalabot.utils.FunctionsAndPredicates.FIRST;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.FunctionalResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import xyz.mainframegames.kalabot.audioplayer.AudioManager;
import xyz.mainframegames.kalabot.audioplayer.LavaplayerAudioSource;
import xyz.mainframegames.kalabot.audioplayer.PlayerManager;
import xyz.mainframegames.kalabot.audioplayer.ServerMusicManager;
import xyz.mainframegames.kalabot.commands.AbstractCommand;
import xyz.mainframegames.kalabot.services.messages.MessagingService;
import xyz.mainframegames.kalabot.utils.BotError;
import xyz.mainframegames.kalabot.utils.Command;

@Slf4j
public class PlayCommand extends AbstractCommand {

  private static final String YOUTUBE_SEARCH = "ytsearch: ";
  private static final String THE_TRACK = "The track: ";
  private static final String ADDED = " has been added";
  private static final String QUEUED = " has been queued";
  private static final String NOT_FOUND = "The track introduced has not been found";

  private final AudioPlayerManager manager = PlayerManager.getAudioManager();

  public PlayCommand(MessagingService messagingService) {
    super(Command.PLAY.getCommandInput(), messagingService);
  }

  @Override
  protected void runCommand(
      MessageCreateEvent event, Server server, ServerTextChannel channel, User user) {
    if (checkNoCommandSyntaxError(event, BotError.PLAY, channel, Command.PLAY, null)) {
      event
          .getMessageAuthor()
          .getConnectedVoiceChannel()
          .ifPresentOrElse(
              voiceChannel -> {
                if (botHasPermissions(voiceChannel, event)) {
                  ServerMusicManager musicManager = AudioManager.getServerManager(server);
                  String query = event
                          .getMessageContent()
                          .replace(event.getMessageContent().split(" ")[FIRST] + " ", "");
                  if (botIsNotConnectedAndAudioConnectionIsClosed(voiceChannel, event, server)) {
                    joinChannelAndPlayTrack(voiceChannel, event, query, channel, musicManager);
                  } else if (botIsAlreadyConnectedToChannel(server)) {
                    playTrackWhenAlreadyConnectedToChannel(
                        server, voiceChannel, event, query, channel, musicManager);
                  }
                } else {
                  channel.sendMessage(BOT_PERMISSION_ERROR.getDescription());
                }
              },
              () -> channel.sendMessage(USER_NOT_IN_A_VOICE_CHANNEL.getDescription()));
    }
  }

  private void play(String query, ServerTextChannel channel, ServerMusicManager musicManagger) {
    manager.loadItemOrdered(
        musicManagger,
        stringIsUrl(query) ? query : YOUTUBE_SEARCH + query,
        new FunctionalResultHandler(
            audioTrack -> {
              channel.sendMessage(THE_TRACK + audioTrack.getInfo().title + ADDED);
              musicManagger.getScheduler().queue(audioTrack);
            },
            audioPlaylist -> {
              if (audioPlaylist.isSearchResult()) {
                musicManagger.getScheduler().queue(audioPlaylist.getTracks().get(FIRST));
                channel.sendMessage(
                    THE_TRACK + audioPlaylist.getTracks().get(FIRST).getInfo().title + ADDED);
              } else {
                audioPlaylist
                    .getTracks()
                    .forEach(
                        audioTrack -> {
                          musicManagger.getScheduler().queue(audioTrack);
                          channel.sendMessage(THE_TRACK + audioTrack.getInfo().title + QUEUED);
                        });
              }
            },
            () -> channel.sendMessage(NOT_FOUND),
            e -> channel.sendMessage(AUDIO_TRACK_EXCEPTION.getDescription() + e.getMessage())));
  }

  private void playTrackWhenAlreadyConnectedToChannel(
      Server server,
      ServerVoiceChannel voiceChannel,
      MessageCreateEvent event,
      String query,
      ServerTextChannel channel,
      ServerMusicManager musicManager) {
    server
        .getAudioConnection()
        .ifPresent(
            audioConnection -> {
              if (audioConnection.getChannel().getId() == voiceChannel.getId()) {
                AudioSource audio = new LavaplayerAudioSource(event.getApi(), musicManager.getPlayer());
                audioConnection.setAudioSource(audio);
                play(query, channel, musicManager);
              } else {
                channel.sendMessage(NOT_IN_SAME_VOICE_CHANNEL.getDescription());
              }
            });
  }

  private void joinChannelAndPlayTrack(
      ServerVoiceChannel voiceChannel,
      MessageCreateEvent event,
      String query,
      ServerTextChannel channel,
      ServerMusicManager musicManager) {
    voiceChannel
        .connect()
        .thenAccept(
            audioConnection -> {
              AudioSource audio = new LavaplayerAudioSource(event.getApi(), musicManager.getPlayer());
              audioConnection.setAudioSource(audio);
              play(query, channel, musicManager);
            });
  }

  private boolean botIsAlreadyConnectedToChannel(Server server) {
    return server.getAudioConnection().isPresent();
  }

  private boolean botIsNotConnectedAndAudioConnectionIsClosed(
      ServerVoiceChannel voiceChannel, MessageCreateEvent event, Server server) {
    return !voiceChannel.isConnected(event.getApi().getYourself())
        && server.getAudioConnection().isEmpty();
  }

  private boolean botHasPermissions(ServerVoiceChannel voiceChannel, MessageCreateEvent event) {
    return voiceChannel.canYouConnect()
        && voiceChannel.canYouSee()
        && voiceChannel.hasPermission(event.getApi().getYourself(), PermissionType.SPEAK);
  }

  private boolean stringIsUrl(String argument) {
    return argument.startsWith("https://") || argument.startsWith("http://");
  }
}
