package com.insurancecorp.insurecar.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to populate the database with sample data.
 * This class reads SQL statements from a text file and executes them using JDBC.
 */
public class DatabasePopulator {

    // HSQLDB connection parameters from context.xml
    private static final String JDBC_URL = "jdbc:hsqldb:hsql://localhost:1666";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASSWORD = "";
    
    // Hardcoded path to SQL file - change this to match your file location
    private static final String SQL_FILE_PATH = "e:/code/ai-course/insurecar/sample_data.sql";
    
    /**
     * Main method to run the database population.
     */
    public static void main(String[] args) {
        try {
            // Load JDBC driver
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            
            System.out.println("Reading SQL from file: " + SQL_FILE_PATH);
            
            // Read SQL statements from file
            List<String> sqlStatements = readSqlFile(SQL_FILE_PATH);
            
            System.out.println("Found " + sqlStatements.size() + " SQL statements");
            
            // Execute SQL statements
            executeSqlStatements(sqlStatements);
            
            System.out.println("Database populated successfully!");
            
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading SQL file: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
    
    /**
     * Reads SQL statements from a file.
     * Statements are expected to be separated by semicolons.
     * 
     * @param filePath Path to the SQL file
     * @return List of SQL statements
     * @throws IOException If an I/O error occurs
     */
    private static List<String> readSqlFile(String filePath) throws IOException {
        List<String> statements = new ArrayList<>();
        StringBuilder currentStatement = new StringBuilder();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip comments and empty lines
                line = line.trim();
                if (line.isEmpty() || line.startsWith("--") || line.startsWith("/*")) {
                    continue;
                }
                
                // Add the line to the current statement
                currentStatement.append(line).append(" ");
                
                // If the line ends with a semicolon, it's the end of a statement
                if (line.endsWith(";")) {
                    statements.add(currentStatement.toString());
                    currentStatement = new StringBuilder();
                }
            }
        }
        
        // Add the last statement if there is one
        if (currentStatement.length() > 0) {
            statements.add(currentStatement.toString());
        }
        
        return statements;
    }
    
    /**
     * Executes a list of SQL statements.
     * 
     * @param sqlStatements List of SQL statements to execute
     * @throws SQLException If a database error occurs
     */
    private static void executeSqlStatements(List<String> sqlStatements) throws SQLException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            connection.setAutoCommit(false);
            
            try (Statement statement = connection.createStatement()) {
                for (String sql : sqlStatements) {
                    System.out.println("Executing: " + sql);
                    statement.execute(sql);
                }
                
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }
}
