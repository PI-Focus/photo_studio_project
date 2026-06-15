package pi.focus.server.service.models;

import pi.focus.server.api.models.INamedData;

/**
 * Универсальный DTO для представления именованных данных.
 * Позволяет связать любое содержимое с заголовком вкладки.
 * 
 * @param <T> тип данных, хранящихся во вкладке
 * @param tabName отображаемое имя вкладки
 * @param data объект данных вкладки
 */
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
