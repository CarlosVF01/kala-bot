package xyz.mainframegames.kalabot.audioplayer;

import java.util.HashMap;
import java.util.Map;

public class AudioManager {

  private static final Map<Long, ServerMusicManager> managers = new HashMap<>();

  /**
   * Retrieves the server music manager dedicated for the server.
   *
   * @param server the server's identification number.
   * @return a ServerMusicManager.
   */
  public static ServerMusicManager get(long server) {

    if (!managers.containsKey(server)) {
      managers.put(server, new ServerMusicManager(PlayerManager.getAudioManager()));
    }

    return managers.get(server);
  }
}
