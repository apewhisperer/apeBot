package actions;

import java.util.ArrayList;
import java.util.Random;

public class DiceRoller {

    public static String roll(String message) {

        String rest = message;
        int sum = 0;

        while (rest.length() > 1 && containsOperator(rest)) {
            int evaluateResult = evaluate(rest.substring(lastOperatorIndex(rest)));
            if (evaluateResult == -1) {
                return "dice value must be greater than 1!";
            }
            sum += evaluateResult;
            rest = rest.substring(0, lastOperatorIndex(rest));
        }
        ArrayList<Integer> arrayList = convertMainDice(rest.substring(rest.indexOf("!") + 1));
        if (arrayList == null) {
            return "dice value must be greater than 1!";
        }
        String main = "";
        for (int i = 0; i < arrayList.size() - 1; i++) {
            main = main.concat(String.valueOf(arrayList.get(i)));
            if (i < arrayList.size() - 2) {
                main = main.concat(", ");
            }
        }
        return "Result: `[" + main + "]` Sum: " + (sum + arrayList.get(arrayList.size() - 1));
    }

    private static int evaluate(String rest) {
        String preDice = rest;
        if (preDice.contains("d")) {
            preDice = convertRestDice(preDice);
            if (preDice == null) {
                return -1;
            }
        }
        if (preDice.charAt(0) == '+') {
            return Integer.parseInt(preDice.substring(1));
        } else if (preDice.charAt(1) == '-') {
            return Integer.parseInt(preDice.substring(1)) * -1;
        } else {
            return Integer.parseInt(preDice);
        }
    }

    public static ArrayList<Integer> convertMainDice(String diceString) {
        ArrayList<Integer> resultArray = new ArrayList<>();
        int[] array = getArray(diceString);
        int number = array[0];
        int dice = array[1];
        Random random = new Random();
        int sum = 0;
        if (dice > 1) {
            if (number == 0) {
                int roll = random.nextInt(dice) + 1;
                sum += roll;
                resultArray.add(roll);
            } else {
                for (int i = 0; i < number; i++) {
                    int roll = random.nextInt(dice) + 1;
                    sum += roll;
                    resultArray.add(roll);
                }
            }
        } else {
            return null;
        }
        resultArray.add(sum);
        return resultArray;
    }

    public static String convertRestDice(String preDice) {
        char operator = preDice.charAt(0);
        String diceString = preDice.substring(1);
        int[] array = getArray(diceString);
        int number = array[0];
        int dice = array[1];
        int sum = 0;
        Random random = new Random();
        if (dice > 1) {
            if (number == 0) {
                int roll = random.nextInt(dice) + 1;
                sum += roll;
            } else {
                for (int i = 0; i < number; i++) {
                    int roll = random.nextInt(dice) + 1;
                    sum += roll;
                }
            }
        } else {
            return null;
        }
        return operator + String.valueOf(sum);
    }

    private static int[] getArray(String diceString) {
        int[] array = new int[2];
        ArrayList<Character> diceNumberArray = new ArrayList<>();
        ArrayList<Character> diceTypeArray = new ArrayList<>();
        char[] diceNumberChar = diceString.substring(0, (diceString.indexOf("d"))).toCharArray();
        char[] diceTypeChar = diceString.substring(diceString.indexOf("d") + 1).toCharArray();
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
        String diceNumberString = getString(diceNumberArray);
        String diceTypeString = getString(diceTypeArray);
        if (!diceNumberString.equals("")) {
            array[0] += Integer.parseInt(diceNumberString);
        }
        if (!diceTypeString.equals("")) {
            array[1] += Integer.parseInt(diceTypeString);
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

    private static int lastOperatorIndex(String message) {
        return Math.max(message.lastIndexOf("+"), message.lastIndexOf("-"));
    }

    private static boolean containsOperator(String message) {
        return message.contains("+") || message.contains("-");
    }
}
