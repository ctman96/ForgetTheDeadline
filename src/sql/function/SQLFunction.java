package sql.function;

import java.sql.SQLException;

/**
 * @see java.util.function.Function
 * @param <T>
 * @param <R>
 */
public interface SQLFunction<T, R> {
    R apply(T t) throws SQLException;
}
