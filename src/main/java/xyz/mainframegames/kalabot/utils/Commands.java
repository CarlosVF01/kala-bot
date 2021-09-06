package xyz.mainframegames.kalabot.utils;

//Enum with all the commands that the bot has for organization purposes and cleaner code
public enum Commands {
    RATE("!rate"),
    TEST("!test"),
    AVATAR("!avatar")
    ;

    private final String text;

    Commands(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
