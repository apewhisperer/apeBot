package functions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.ByteBuffer;
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
        ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(tooltip);
        return StandardCharsets.UTF_8.decode(byteBuffer).toString();
    }
}
