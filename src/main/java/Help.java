public class Help {

    public static String tipCode() {
        return "!tip [spell name]";
    }

    public static String tipHelp() {
        return "- shows tooltip of a spell (example: !tip animal friendship)";
    }

    public static String rollCode() {
        return "!d[value][operation]";
    }

    public static String rollHelp() {
        return "- rolls a [value] sided dice and performs a desired operation(example: !d20+5)";
    }
}
