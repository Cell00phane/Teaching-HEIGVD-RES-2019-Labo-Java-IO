package ch.heigvd.res.labio.impl.filters;

import ch.heigvd.res.labio.impl.Utils;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

/**
 * This class transforms the streams of character sent to the decorated writer.
 * When filter encounters a line separator, it sends it to the decorated writer.
 * It then sends the line number and a tab character, before resuming the write
 * process.
 *
 * Hello\n\World -> 1\tHello\n2\tWorld
 *
 * @author Olivier Liechti
 */
public class FileNumberingFilterWriter extends FilterWriter {

  private static final Logger LOG = Logger.getLogger(FileNumberingFilterWriter.class.getName());

  public FileNumberingFilterWriter(Writer out) {
    super(out);
  }

  private int lineCount = 1;
  private boolean startFile = true;

  private boolean foundLineSeparator;

  @Override
  public void write(String str, int off, int len) throws IOException {
    String filteredString = applyFilterToInput(str.substring(off, off + len));

    super.write(filteredString, 0, filteredString.length());
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    String filteredString = applyFilterToInput(new String(cbuf).substring(off, off + len));
    super.write(filteredString.toCharArray(), 0, filteredString.length());
  }

  @Override
  public void write(int c) throws IOException {
    if(c == '\r') {
      foundLineSeparator = true;
    } else if(foundLineSeparator) {
      if(c == '\n') {
        // Windows
        write("\r\n", 0, 2);
      } else {
        // Just Mac
        write("\r" + (char) c, 0, 2);
      }
      foundLineSeparator = false;
    } else {
      String filteredString = applyFilterToInput(Character.toString((char) c));
      super.write(filteredString, 0, filteredString.length());
    }
  }

  private String applyFilterToInput(String str) {
    String[] newLineSeparation = Utils.getNextLine(str);
    StringBuilder sb = new StringBuilder();

    if(startFile) {
      sb.append(lineCount).append('\t');
      startFile = false;
    }

    while(!newLineSeparation[0].isEmpty()) {
      sb.append(newLineSeparation[0]).append(++lineCount).append('\t');
      newLineSeparation = Utils.getNextLine(newLineSeparation[1]);
    }

    // append the rest if trailing string without line separator at the end is found
    sb.append(newLineSeparation[1]);

    return sb.toString();
  }

}
