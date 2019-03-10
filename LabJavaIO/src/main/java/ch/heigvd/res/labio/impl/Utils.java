package ch.heigvd.res.labio.impl;

import java.util.logging.Logger;

/**
 *
 * @author Olivier Liechti
 */
public class Utils {

  private static final Logger LOG = Logger.getLogger(Utils.class.getName());

  /**
   * This method looks for the next new line separators (\r, \n, \r\n) to extract
   * the next line in the string passed in arguments. 
   * 
   * @param lines a string that may contain 0, 1 or more lines
   * @return an array with 2 elements; the first element is the next line with
   * the line separator, the second element is the remaining text. If the argument does not
   * contain any line separator, then the first element is an empty string.
   */
  public static String[] getNextLine(String lines) {
    //throw new UnsupportedOperationException("The student has not implemented this method yet.");
    /*String[] result = new String[]{"", lines};
    if(lines.contains("\r")) {
      if(lines.contains("\n")) {
        //Windows
        result = lines.split("\r\n", 2);
        result[0] += "\r\n";
        return result;
      } else {
        // Mac
        result = lines.split("\r", 2);
        result[0] += "\r";
        return result;
      }
    } else if(lines.contains("\n")){
      // Unix
      result = lines.split("\n", 2);
      result[0] += "\n";
      return result;
    }
    // No line separators
    return result;*/

    // With regex
    if(lines.contains("\r")) {
      if(lines.contains("\n")) {
        // Windows
        return lines.split("(?<=\r\n)", 2);
      } else {
        // Mac
        return lines.split("(?<=\r)", 2);
      }
    } else if (lines.contains("\n")) {
      // Unix
      return lines.split("(?<=\n)", 2);
    }
    return new String[] {"", lines};
  }

}
