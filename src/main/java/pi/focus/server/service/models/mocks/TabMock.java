package pi.focus.server.service.models.mocks;

import pi.focus.server.api.models.ITab;

public class TabMock<T> implements ITab<T>{
    private final String tabName;
    private final T data;

    
    public TabMock(String tabName, T data) {
        this.tabName = tabName;
        this.data = data;
    }

    @Override
    public String getTabName() {
        return tabName;
    }
    @Override
    public T getData() {
        return data;
    }

    
}
