package commands;

public class Help {

    static String tipCode() {
        return "!tip";
    }

    static String tipInfo() {
        return "- shows tooltip of a spell (example: **!tip animal friendship)**";
    }

    static String rollCode() {
        return "!d";
    }

    static String rollInfo() {
        return "- rolls a number of dice and adds bonuses (example: **!2d20+5**)";
    }

    static String playCode() {
        return "!play";
    }

    static String playInfo() {
        return "- loads track from link (example: **!play youtube.com**)";
    }

    static String pauseCode() {
        return "!pause";
    }

    static String pauseInfo() {
        return "- pauses current track (example: **!pause**)";
    }

    static String volumeCode() {
        return "!volume";
    }

    static String volumeInfo() {
        return "- changes volume (example: **!volume 10**)";
    }

    static String surgeCode() {
        return "!surge";
    }

    static String surgeInfo() {
        return "- generates a random magical effect from the table (example: **!surge**)";
    }
}
