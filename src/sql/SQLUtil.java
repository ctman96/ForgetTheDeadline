package sql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
public class SQLUtil {

    /**
     * Executes statements from a file
     * @param connection database connection
     * @param file file from which query strings are read
     */
    public static void executeFile(Connection connection, File file) throws IOException, SQLException {
        String fileName = file.getName();
        int statementCount = 0;

        System.out.printf("Executing file %s.\n", fileName);
        try (FileReader fReader = new FileReader(file);
             BufferedReader reader = new BufferedReader(fReader)) {
            StringBuilder buf = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                buf.append(line);
            }

            String[] statementStrings = buf.toString().split(";");


            for (String statementString : statementStrings) {
                String trimString = statementString.trim();
                if (trimString.isEmpty()) {
                    continue; // Skip empty lines
                }

                Statement statement = connection.createStatement();
                statement.execute(trimString);
                statementCount++;

            }

            System.out.printf("%d statements executed from file %s.\n", statementCount, fileName);
        } catch (SQLException e) {
            System.out.printf("%d statements executed from file %s before exception.\n", statementCount, fileName);
            throw e;
        }
    }

    public static PreparedStatement getAllFromTableQuery(Connection connection, String tableName) throws SQLException {
        return connection.prepareStatement(String.format("SELECT * FROM %s", tableName));
    }
}
