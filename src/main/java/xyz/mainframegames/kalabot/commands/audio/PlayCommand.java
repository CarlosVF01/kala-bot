package xyz.mainframegames.kalabot.commands.audio;

import static xyz.mainframegames.kalabot.utils.Regex.ANY_CHARACTER_REGEX;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.FunctionalResultHandler;
import java.util.regex.Pattern;
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

  private static final Pattern patternAny = Pattern.compile(
      Command.PLAY + " " + ANY_CHARACTER_REGEX);
  private static final Pattern patternUrl = Pattern.compile(
      Command.PLAY + " " + ANY_CHARACTER_REGEX);

  private static final String YOUTUBE_SEARCH = "ytsearch: ";
  private static final String THE_TRACK = "The track: ";
  private static final String ADDED = "has been added";
  private static final String QUEUED = "has been queued";
  private static final String NOT_FOUND = "The track introduced has not been found";

  private final AudioPlayerManager manager = PlayerManager.getAudioManager();

  public PlayCommand(MessagingService messagingService) {
    super(Command.PLAY.toString(), messagingService);
  }

  @Override
  protected void runCommand(MessageCreateEvent event, Server server, ServerTextChannel channel,
      User user) {
    if (checkNoCommandSyntaxError(event, BotError.PLAY, channel, Command.PLAY,
        getFinalPattern(event))) {
      event.getMessageAuthor().getConnectedVoiceChannel().ifPresentOrElse(voiceChannel -> {

        if (botHasPermissions(voiceChannel, event)) {
          ServerMusicManager musicManager = AudioManager.getServerManager(server.getId());
          String query = event.getMessageContent()
              .replace(event.getMessageContent().split(" ")[0] + " ", "");
          if (botIsNotConnectedAndAudioConnectionIsClosed(voiceChannel, event, server)) {
            joinChannelAndPlayTrack(voiceChannel, event, query, channel, musicManager);
          } else if (server.getAudioConnection().isPresent()) {
            playTrackWhenAlreadyConnectedToChannel(server, voiceChannel, event, query,
                channel, musicManager);
          }
        } else {
          event.getChannel().sendMessage("Not enough permissions.");
        }
      }, () -> event.getChannel().sendMessage("You are not connected to any voice channel."));
    }
  }

  private void play(String query, ServerTextChannel channel, ServerMusicManager m) {
    manager.loadItemOrdered(m, isUrl(query) ? query : YOUTUBE_SEARCH + query,
        new FunctionalResultHandler(audioTrack -> {
          channel.sendMessage(
              THE_TRACK + audioTrack.getInfo().title + ADDED);
          m.scheduler.queue(audioTrack);
        }, audioPlaylist -> {
          if (audioPlaylist.isSearchResult()) {
            m.scheduler.queue(audioPlaylist.getTracks().get(0));
            channel.sendMessage(
                THE_TRACK + audioPlaylist.getTracks().get(0).getInfo().title
                    + ADDED);
          } else {
            audioPlaylist.getTracks().forEach(audioTrack -> {
              m.scheduler.queue(audioTrack);
              channel.sendMessage(
                  THE_TRACK + audioTrack.getInfo().title + QUEUED);
            });
          }
        }, () -> channel.sendMessage(NOT_FOUND), e -> channel.sendMessage(
            "An error has occurred and the track couldn't be played "
                + e.getMessage())));
  }

  private void playTrackWhenAlreadyConnectedToChannel(Server server,
      ServerVoiceChannel voiceChannel, MessageCreateEvent event, String query,
      ServerTextChannel channel, ServerMusicManager musicManager) {
    server.getAudioConnection().ifPresent(audioConnection -> {
      if (audioConnection.getChannel().getId() == voiceChannel.getId()) {
        AudioSource audio = new LavaplayerAudioSource(event.getApi(), musicManager.player);
        audioConnection.setAudioSource(audio);
        play(query, channel, musicManager);
      } else {
        event.getChannel().sendMessage("You are not in the same voice channel as the bot");
      }
    });
  }

  private void joinChannelAndPlayTrack(ServerVoiceChannel voiceChannel, MessageCreateEvent event,
      String query, ServerTextChannel channel, ServerMusicManager musicManager) {
    voiceChannel.connect().thenAccept(audioConnection -> {
      AudioSource audio = new LavaplayerAudioSource(event.getApi(), musicManager.player);
      audioConnection.setAudioSource(audio);
      play(query, channel, musicManager);
    });
  }

  private boolean botIsNotConnectedAndAudioConnectionIsClosed(ServerVoiceChannel voiceChannel,
      MessageCreateEvent event, Server server) {
    return !voiceChannel.isConnected(event.getApi().getYourself()) && server.getAudioConnection()
        .isEmpty();
  }

  private Pattern getFinalPattern(MessageCreateEvent event) {
    return patternUrl.matcher(event.getMessageContent()).matches() ? patternUrl : patternAny;
  }

  private boolean botHasPermissions(ServerVoiceChannel voiceChannel, MessageCreateEvent event) {
    return voiceChannel.canYouConnect() && voiceChannel.canYouSee() && voiceChannel.hasPermission(
        event.getApi().getYourself(), PermissionType.SPEAK);
  }

  private boolean isUrl(String argument) {
    return argument.startsWith("https://") || argument.startsWith("http://");
  }
}
