package xyz.mainframegames.kalabot.utils;

/**
 * Enum that contains the different error messages that can be sent to the user
 */
public enum BotError {
  AVATAR("Avatar command error", "The syntax of the `!avatar` command is: `!avatar [@person]`"),
  RATE("Rate command error", "The syntax of the `!rate` command is: `!rate [@person]`"),
  PLAY("Play command error",
      "The syntax of the `!play` command is: `!play [Youtube Link or Video Name]`"),
  PLAYING_MUSIC("Audio Player Error", "The bot isn't currently playing music"),
  NOT_IN_VOICE_CHANNEL("Audio Player Error", "The bot isn't in a voice channel");

  private final String error;
  private final String description;

  BotError(final String error, final String description) {
    this.error = error;
    this.description = description;
  }

  public String getError() {
    return error;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public String toString() {
    return error + " " + description;
  }
}
