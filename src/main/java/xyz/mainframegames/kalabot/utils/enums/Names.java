package xyz.mainframegames.kalabot.utils.enums;

public enum Names {
    BOT_NAME("Kala Bot");

    private final String text;

    Names(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
