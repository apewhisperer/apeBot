import java.util.ArrayList;
import java.util.Random;

public class DiceRoller {

    public static String rollDice(String message) {

        String details = "";
        Log.registerEvent("!d", message);

        int[] array = getArray(message);

        int number = array[0];
        int dice = array[1];
        int bonus = array[2];

        if (number > 500) {
            return "dice number must not exceed 500";
        }
        if (dice < 2 || dice > 100) {
            return "dice value must be within 2-100 range";
        }
        if (bonus < -100 || bonus > 100) {
            return "modifier must be within (-100)-100 range";
        }

        String result;
        Random random = new Random();
        int sum = 0;
        if (number == 0) {
            int roll = random.nextInt(dice) + 1;
            sum += roll + bonus;
            details = details.concat(String.valueOf(roll + bonus));
        } else {
            for (int i = 0; i < number; i++) {
                int roll = random.nextInt(dice) + 1;
                sum += roll + bonus;
                details = details.concat(String.valueOf(roll + bonus));
                if (i < number - 1) {
                    details = details.concat(", ");
                }
            }
        }
        result = "Roll: `[" + details + "]` Result: " + sum;
        return result;
    }

    private static int[] getArray(String message) {

        String noWhitespaceMessage = message.trim().replaceAll(" ", "");

        boolean isNegative = false;
        int[] array = new int[3];

        ArrayList<Character> diceNumberArray = new ArrayList<>();
        ArrayList<Character> diceTypeArray = new ArrayList<>();
        ArrayList<Character> bonusArray = new ArrayList<>();

        char[] diceNumberChar = noWhitespaceMessage.substring((noWhitespaceMessage.indexOf("!") + 1), (noWhitespaceMessage.indexOf("d"))).toCharArray();
        char[] diceTypeChar;
        char[] bonusChar = new char[0];

        if (noWhitespaceMessage.contains("+")) {
            diceTypeChar = noWhitespaceMessage.substring((noWhitespaceMessage.indexOf("d") + 1), noWhitespaceMessage.indexOf("+")).toCharArray();
            bonusChar = noWhitespaceMessage.substring(noWhitespaceMessage.indexOf("+") + 1).toCharArray();
        } else if (noWhitespaceMessage.contains("-")) {
            isNegative = true;
            diceTypeChar = noWhitespaceMessage.substring((noWhitespaceMessage.indexOf("d") + 1), noWhitespaceMessage.indexOf("-")).toCharArray();
            bonusChar = noWhitespaceMessage.substring(noWhitespaceMessage.indexOf("-") + 1).toCharArray();
        } else {
            diceTypeChar = noWhitespaceMessage.substring(noWhitespaceMessage.indexOf("d") + 1).toCharArray();
        }

        for (char c : diceNumberChar) {
            if (Character.isDigit(c)) {
                diceNumberArray.add(c);
            } else {
                break;
            }
        }
        for (char c : diceTypeChar) {
            if (Character.isDigit(c)) {
                diceTypeArray.add(c);
            } else {
                break;
            }
        }
        for (char c : bonusChar) {
            if (Character.isDigit(c)) {
                bonusArray.add(c);
            } else {
                break;
            }
        }
        String diceNumberString = getString(diceNumberArray);
        String diceTypeString = getString(diceTypeArray);
        String bonusString = getString(bonusArray);
        if (!diceNumberString.equals("")) {
            array[0] += Integer.parseInt(diceNumberString);
        }
        if (!diceTypeString.equals("")) {
            array[1] += Integer.parseInt(diceTypeString);
        }
        if (!bonusString.equals("")) {
            array[2] += Integer.parseInt(bonusString);
        }
        if (isNegative) {
            array[2] *= -1;
        }
        return array;
    }

    private static String getString(ArrayList<Character> list) {
        StringBuilder builder = new StringBuilder(list.size());
        for (Character ch : list) {
            builder.append(ch);
        }
        return builder.toString();
    }
}
