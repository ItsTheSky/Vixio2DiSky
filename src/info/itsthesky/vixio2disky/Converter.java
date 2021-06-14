package info.itsthesky.vixio2disky;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Converter {

    public static final Pattern txtInput = Pattern.compile("(\".+?(?=\")\")");
    public static final Pattern varInput = Pattern.compile("([^\\s]+|(\".+?(?=\")\"))");

    // for (int i = 1; i <= m.groupCount(); i++)
    //                    System.out.println("Group N°" + i + ":" + m.group(i));

    public static String convert(final String current) {
        if (current.isEmpty() || current.split("").length == 0)
            throw new IllegalArgumentException("Convert code cannot be empty!");
        final String[] lines = current.split("\n").clone();

        Pattern titlePattern = Pattern.compile("set (the )?title(s)? of "+varInput+" to (a )?title( with)?( the)? text "+txtInput+"(( and (the)?|, )((url|link) "+txtInput+"|no (url|link)))?");
        Pattern seenPattern = Pattern.compile(" seen by "+txtInput+"");
        Pattern authorPattern = Pattern.compile("set (the )?author of "+varInput+" to (an )?author named "+txtInput+" with (((the)? url "+txtInput+"|no url)(( and (the)?|, )?))?(icon "+txtInput+"|no icon)?");
        Pattern footerPattern = Pattern.compile("set (the )?footer of "+varInput+" to (a)? footer with (the )?text "+txtInput+"(( and (the)?|, )(icon "+txtInput+"|no icon))?");
        Pattern embedPattern = Pattern.compile("(make|create) embed");
        Pattern builderPattern = Pattern.compile("append (line)? "+txtInput+" to "+varInput);
        Pattern reactionPattern = Pattern.compile("add (emoji|emote|reaction)(s)? "+varInput+"( (from|in) "+varInput+")? to "+varInput+" with "+varInput);
        Pattern lastEmbedPattern = Pattern.compile("(the )?last(ly)? (made|created )?embed(( )?builder)?");

        /* Main line loop */
        List<String> toReturn = new ArrayList<>();
        for (String line : lines) {
            Pattern tab = Pattern.compile("(^(\\s+|\\t)+)");
            Matcher indentMatcher = tab.matcher(line);
            String indentation = "";
            if (indentMatcher.find()) {
                indentation = indentMatcher.group(2);
            }
            Matcher m;
            boolean added = false;

            // Last embed
            m = lastEmbedPattern.matcher(line);
            if (m.find()) {
                added = true;
                toReturn.add(m.replaceAll("last embed"));
            }

            // Footer
            m = footerPattern.matcher(line);
            if (m.find()) {
                added = true;
                String var = m.group(2);
                String footer = m.group(6);
                String icon = m.group(11);
                toReturn.add(m.replaceAll("set footer of " + var + " to " + footer));
                if (icon != null)
                    toReturn.add(indentation + "set footer icon of " + var + " to " + icon);
            }

            // Reaction
            m = reactionPattern.matcher(line);
            if (m.find()) {
                added = true;
                String reactions = m.group(3);
                String guild = m.group(6);
                String msg = m.group(7);
                String bot = m.group(8);
                toReturn.add(indentation + "add reaction " + reactions + (guild == null ? "" : " from " + guild) + " to " + msg + " with bot " + bot);
            }

            // Builder
            m = builderPattern.matcher(line);
            if (m.find()) {
                added = true;
                String input = m.group(2);
                String var = m.group(3);
                toReturn.add(m.replaceAll("append " + input + " to builder " + var));
            }

            // Embed
            m = embedPattern.matcher(line);
            if (m.find()) {
                added = true;
                toReturn.add(m.replaceAll("make embed"));
            }

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
            // Author
            m = authorPattern.matcher(line);
            if (m.find()) {
                String var = m.group(2);
                String author = m.group(4);
                String url = m.group(8);
                String icon = m.group(13);
                added = true;
                toReturn.add(m.replaceAll("set author of " + var + " to " + author));
                if (url != null)
                    toReturn.add(indentation + "set author url of " + var + " to " + url);
                if (icon != null)
                    toReturn.add(indentation + "set author icon of " + var + " to " + icon);
            }

            // 'seen by' pattern
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
