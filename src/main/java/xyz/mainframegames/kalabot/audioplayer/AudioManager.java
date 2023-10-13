package xyz.mainframegames.kalabot.audioplayer;

import org.javacord.api.entity.server.Server;

import java.util.HashMap;
import java.util.Map;

public class AudioManager {

  private static final Map<Long, ServerMusicManager> managers = new HashMap<>();

  private AudioManager() {}

  /**
   * Retrieves the server music manager dedicated for the server.
   *
   * @param server the server's identification number.
   * @return a {@link ServerMusicManager}.
   */
  public static ServerMusicManager getServerManager(Server server) {

    long id = server.getId();
    if (!managers.containsKey(id)) {
      managers.put(id, new ServerMusicManager(PlayerManager.getAudioManager()));
    }

    return managers.get(id);
  }
}
