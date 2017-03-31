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
        int failureCount = 0;
        String failures="";

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
                try {
                    statement.execute(trimString);
                    statementCount++;
                }catch (SQLException e){
                    failures += "\n On line:"+trimString+e.getMessage()+"\n";
                    failureCount++;
                }
            }

            System.out.printf("%d statements executed, %d exceptions from file %s.\n", statementCount,failureCount, fileName);
        } catch (SQLException e) {
            System.out.printf("%d statements executed from file %s before exception.\n", statementCount, fileName);
            throw e;
        }
        if(!failures.equals("")){
            failures = "Encountered "+failureCount+" exception(s) during execution. \n"+failures;
            throw new SQLException(failures);
        }
    }

    public static PreparedStatement getAllFromTableQuery(Connection connection, String tableName, String mainColumn) throws SQLException {
        return connection.prepareStatement(String.format("SELECT * FROM %s ORDER BY %S", tableName,mainColumn));
    }
}
