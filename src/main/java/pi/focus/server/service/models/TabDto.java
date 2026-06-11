package pi.focus.server.service.models;

import pi.focus.server.api.models.ITab;

public record TabDto<T>(String tabName, T data) implements ITab<T> {

    @Override
    public String getTabName() {
        return tabName;
    }

    @Override
    public T getData() {
        return data;
    }
}
