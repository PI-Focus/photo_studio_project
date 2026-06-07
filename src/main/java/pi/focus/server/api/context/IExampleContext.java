package pi.focus.server.api.context;

import java.util.List;

// context for example page
public interface IExampleContext {
    int getNumber();
    List<Integer> getNumbers();
    String getTextString();
    List<String> getTextStrings();
    String getPicture();
    List<String> getPictures();
}
