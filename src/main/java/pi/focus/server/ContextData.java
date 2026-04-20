package pi.focus.server;

import java.util.List;

public interface ContextData {
    int getNumber();
    List<Integer> getNumbers();
    String getTextString();
    List<String> getTextStrings();
    String getPicture();
    List<String> getPictures();
}
