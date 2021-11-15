package xyz.mainframegames.kalabot.utils.enums;


public enum Errors {
    AVATAR("Avatar command error", "The syntax of the `!avatar` command is: `!avatar [@person]`"),
    RATE("Rate command error", "The syntax of the `!rate` command is: `!rate [@person]`"),
    PLAY("Play command error", "The syntax of the `!play` command is: `!play [Youtube Link]`");

    private final String error;
    private final String description;

    Errors(final String error, final String description) {
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
