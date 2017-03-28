package sql.function;

import java.sql.SQLException;

/**
 * @see java.util.function.Consumer
 * @param <T>
 */
@FunctionalInterface
public interface SQLConsumer<T> {
    void accept(T t) throws SQLException;
}