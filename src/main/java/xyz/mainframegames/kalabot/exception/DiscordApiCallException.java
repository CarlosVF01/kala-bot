package xyz.mainframegames.kalabot.exception;

public class DiscordApiCallException extends RuntimeException {

  public DiscordApiCallException(String message) {
    super("ApiCallException: " + message);
  }
}
