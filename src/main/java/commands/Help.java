package commands;

public class Help {

    public static String tipCode() {
        return "!tip";
    }

    public static String tipInfo() {
        return "- shows tooltip of a spell (example: **!tip animal friendship)**";
    }

    public static String rollCode() {
        return "!d";
    }

    public static String rollInfo() {
        return "- rolls a number of dice and adds bonuses (example: **!2d20+5**)";
    }

    public static String playCode() {
        return "!play";
    }

    public static String playInfo() {
        return "- loads track from link (example: **!play youtube.com**)";
    }

    public static String pauseCode() {
        return "!pause";
    }

    public static String pauseInfo() {
        return "- pauses current track (example: **!pause**)";
    }

    public static String volumeCode() {
        return "!volume";
    }

    public static String volumeInfo() {
        return "- changes volume (example: **!volume 10**)";
    }

    public static String surgeCode() {
        return "!surge";
    }

    public static String surgeInfo() {
        return "- generates a random magical effect from the table (example: **!surge**)";
    }
}
