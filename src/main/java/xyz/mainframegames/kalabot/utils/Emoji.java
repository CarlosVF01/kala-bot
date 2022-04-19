package xyz.mainframegames.kalabot.utils;

public enum Emoji {
  THUMBS_DOWN("👎");

  private final String emoji;

  Emoji(String emoji) {
    this.emoji = emoji;
  }

  @Override
  public String toString() {
    return emoji;
  }
}
