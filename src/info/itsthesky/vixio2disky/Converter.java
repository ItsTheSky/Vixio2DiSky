package info.itsthesky.vixio2disky;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Converter {

    public static final Pattern txtInput = Pattern.compile("(\".+?(?=\")\")");

    public static String convert(final String current) {
        if (current.isEmpty() || current.split("").length == 0)
            throw new IllegalArgumentException("Convert code cannot be empty!");
        final String[] lines = current.split("\n").clone();

        Pattern titlePattern = Pattern.compile("set (the )?title(s)? of ([^\\s]+) to (a )?title( with)?( the)? text "+txtInput+"(( and (the)?|, )((url|link) "+txtInput+"|no (url|link)))?");
        Pattern seenPattern = Pattern.compile(" seen by "+txtInput+"");
        Pattern authorPattern = Pattern.compile("(an)? author named %string% with (((the)? url "+txtInput+"|no url)(( and (the)?|, )?))? (icon "+txtInput+"|no icon)?");

        /* Main line loop */
        List<String> toReturn = new ArrayList<>();
        for (String line : lines) {
            Pattern tab = Pattern.compile("(^(\\s+|\\t)+)");
            Matcher indentMatcher = tab.matcher(line);
            String indentation = "";
            if (indentMatcher.find()) {
                indentation = indentMatcher.group(2);
            }
            //line = tab.matcher(line).find() ? tab.matcher(line).replaceAll("") : line;
            Matcher m;
            boolean added = false;

            // Title
            m = titlePattern.matcher(line);
            if (m.find()) {
                String url = m.group(13);
                String var = m.group(3);
                String title = m.group(7);
                toReturn.add(m.replaceAll("set title of " + var + " to " + title + ""));
                added = true;
                if (url != null)
                    toReturn.add(indentation + "set title url of " + var + " to " + url + "");
            }
            m = seenPattern.matcher(line);
            if (m.find()) {
                added = true;
                toReturn.add(m.replaceAll(""));
            }

            if (!added)
                toReturn.add(line);
        }

        return String.join("\n", toReturn.toArray(new String[0]));
    }

}
