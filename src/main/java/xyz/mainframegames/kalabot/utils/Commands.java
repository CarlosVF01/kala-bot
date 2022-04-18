package xyz.mainframegames.kalabot.utils;

/** Enum with all the commands to have them grouped in one place */
public enum Commands {
  RATE("rate"),
  PLAY("play"),
  STOP("stop"),
  AVATAR("avatar");

  private final String text;

  Commands(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
