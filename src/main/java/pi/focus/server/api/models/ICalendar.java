package pi.focus.server.api.models;

import java.util.List;

public interface ICalendar {
    Integer getROWS();
    Integer getCOLUMNS();
    List<List<Integer>> getCalendar();
}
