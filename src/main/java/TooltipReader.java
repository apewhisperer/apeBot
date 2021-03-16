import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class TooltipReader {

    public static String getTooltip(String spellName) {

        spellName = spellName.replaceAll(" ", "-");

        BufferedReader br = null;
        try {
            URL url = new URL("http://dnd5e.wikidot.com/spell:" + spellName);
            br = new BufferedReader(new InputStreamReader(url.openStream()));
            String foundWord = "page-content";
            String currentLineLevel1 = "";
            String currentLineLevel2 = "";
            String currentLineLevel3 = "";
            String tooltip = "";

            while ((currentLineLevel1 = br.readLine()) != null) {
                if (currentLineLevel1.contains(foundWord)) {
                    while ((currentLineLevel2 = br.readLine()) != null) {
                        if (currentLineLevel2.contains("<div class=\"content-separator\"")) {
                            while ((currentLineLevel3 = br.readLine()) != null) {

                                tooltip = tooltip.concat(currentLineLevel3);
                                if (currentLineLevel3.contains("<div class=\"content-separator\"")) {

                                    tooltip = tooltip.replaceAll("<p>", "\n")
                                            .replaceAll("</p>", "\n")
                                            .replaceAll("<ul><li>", "\n• ")
                                            .replaceAll("</li></ul>", "\n")
                                            .replaceAll("<br>", "\n")
                                            .replaceAll("<br />", "\n")
                                            .replaceAll("<em>", "")
                                            .replaceAll("</em>", "")
                                            .replaceAll("<strong>", "**")
                                            .replaceAll("</strong>", "**");
                                    tooltip = tooltip.substring(0, tooltip.indexOf("Spell Lists"));
                                    tooltip = tooltip.substring(0, tooltip.lastIndexOf("**"));
                                    return trimTo2000Chars(tooltip);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return "yikes!";
    }

    private static String trimTo2000Chars(String tooltip) {
        return tooltip.substring(0, 1995) + "[...]";
    }
}
