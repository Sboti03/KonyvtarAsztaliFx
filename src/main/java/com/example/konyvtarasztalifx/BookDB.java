package com.example.konyvtarasztalifx;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDB {
    Connection conn;

    public static String DB_DRIVER = "mysql";
    public static String DB_HOST = "localhost";
    public static String DB_PORT = "3306";
    public static String DB_DBNAME = "java";
    public static String DB_USER = "root";
    public static String DB_PASS = "";

    public BookDB() throws SQLException {
        String url = String.format("jdbc:%s://%s:%s/%s", DB_DRIVER, DB_HOST, DB_PORT, DB_DBNAME);
        conn = DriverManager.getConnection(url, DB_USER, DB_PASS);
    }

    public List<Konyv> readBooks() throws SQLException {
        List<Konyv> books = new ArrayList<>();

        String sql = "SELECT * FROM books";
        Statement statement = conn.createStatement();
        ResultSet result = statement.executeQuery(sql);
        while (result.next()) {
            int id = result.getInt("id");
            String title = result.getString("title");
            String author = result.getString("author");
            int publish_year = result.getInt("publish_year");
            int page_count = result.getInt("page_count");

            Konyv konyv = new Konyv(id, title, author, publish_year, page_count);
            books.add(konyv);
        }
        return books;
    }

    public boolean deleteBook(int id) throws SQLException {
        String sql = "DELETE FROM books WHERE id = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, id);
        return statement.executeUpdate() > 0;
    }

}
