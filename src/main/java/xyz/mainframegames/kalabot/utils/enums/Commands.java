package xyz.mainframegames.kalabot.utils.enums;

//Enum with all the commands that the bot has for organization purposes and cleaner code
public enum Commands {
    RATE("!rate"),
    PLAY("!play"),
    AVATAR("!avatar");

    private final String text;

    Commands(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
