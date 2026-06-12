package pi.focus.server.service.models;

import pi.focus.server.api.models.INamedData;

public record TabDto<T>(String tabName, T data) implements INamedData<T> {

    @Override
    public String getTabName() {
        return tabName;
    }

    @Override
    public T getData() {
        return data;
    }
}
