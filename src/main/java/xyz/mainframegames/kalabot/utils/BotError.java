package xyz.mainframegames.kalabot.utils;

/**
 * Enum that contains the different error messages that can be sent to the user
 */
public enum BotError {
  AVATAR("Avatar command error", "The syntax of the `!avatar` command is: `!avatar [@person]`"),
  RATE("Rate command error", "The syntax of the `!rate` command is: `!rate [@person]`"),
  PLAY("Play command error",
      "The syntax of the `!play` command is: `!play [Youtube Link or Video Name]`"),
  BOT_PERMISSION_ERROR("", "Not enough permissions"),
  NOT_PLAYING_MUSIC("", "I'm not currently playing music"),
  BOT_NOT_IN_A_VOICE_CHANNEL("", "I'm not in a voice channel"),
  USER_NOT_IN_A_VOICE_CHANNEL("", "You're not in a voice channel"),
  NOT_IN_SAME_VOICE_CHANNEL("", "You are not in the same voice channel as the bot"),
  AUDIO_TRACK_EXCEPTION("", "An error has occurred and the track couldn't be played");

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
