package xyz.mainframegames.kalabot.utils;

/**
 * Enum with all the commands to have them grouped in one place
 */
public enum Command {
  RATE("!rate", "Rates someone in a scale of 10"),
  PLAY("!play", "Plays a youtube video"),
  PAUSE("!pause", "Pauses the audio player"),
  SKIP("!skip", "Skips the current audio track"),
  RESUME("!resume", "Resumes the current audio track"),
  LEAVE("!leave", "Makes the bot leave the audio channel"),
  AVATAR("!avatar", "Shows your avatar"),
  HELP("!help", "Shows the list of available commands");


  private final String commandInput;
  private final String commandDescription;

  Command(final String commandInput, String commandDescription) {
    this.commandInput = commandInput;
    this.commandDescription = commandDescription;
  }

  public String getCommandInput() {
    return commandInput;
  }

  public String getCommandDescription() {
    return commandDescription;
  }

  @Override
  public String toString() {
    return commandInput;
  }
}
