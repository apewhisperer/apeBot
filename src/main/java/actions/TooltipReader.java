package actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class TooltipReader {

    public static String getTooltip(String spellName) {
        spellName = spellName.replaceAll(" ", "-");
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL("http://dnd5e.wikidot.com/spell:" + spellName);
            bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            String foundWord = "page-content";
            String currentLineLevel1;
            String currentLineLevel2;
            String currentLineLevel3;
            String tooltip = "";
            while ((currentLineLevel1 = bufferedReader.readLine()) != null) {
                if (currentLineLevel1.contains(foundWord)) {
                    while ((currentLineLevel2 = bufferedReader.readLine()) != null) {
                        if (currentLineLevel2.contains("<div class=\"content-separator\"")) {
                            while ((currentLineLevel3 = bufferedReader.readLine()) != null) {
                                tooltip = tooltip.concat(currentLineLevel3);
                                if (currentLineLevel3.contains("<div class=\"content-separator\""))
                                    return filterInput(tooltip);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null)
                    bufferedReader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return "no such spell!";
    }

    private static String filterInput(String tooltip) {
        byte[] bytes = tooltip.getBytes(StandardCharsets.UTF_8);
        String converted = new String(bytes);
        converted = converted.substring(0, converted.indexOf("<p><strong><em>Spell Lists.</em></strong>"));
        converted = converted.replaceAll("<p>", "\n")
                .replaceAll("</p>", "\n")
                .replaceAll("<ul><li>", "\n• ")
                .replaceAll("</li></ul>", "\n")
                .replaceAll("<br>", "\n")
                .replaceAll("<br />", "\n")
                .replaceAll("<em>", "")
                .replaceAll("</em>", "")
                .replaceAll("<strong>", "**")
                .replaceAll("</strong>", "**")
        ;
        return converted;
    }
}
