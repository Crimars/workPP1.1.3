package jm.task.core.jdbc.service;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {

    Connection conn = new Util().getConnection();
    public void createUsersTable() {
        String createTable = "CREATE TABLE IF NOT EXISTS User (" +
                "    id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                "    name VARCHAR(255)," +
                "    lastName VARCHAR(255)," +
                "    age TINYINT" +
                ");";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createTable);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при создании таблицы", e);
        }
    }

    public void dropUsersTable() {
        String dropSQL = "DROP TABLE IF EXISTS User;";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(dropSQL);
            System.out.println("Таблица 'User' удалена (или не существовала).");
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении таблицы", e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String insertSQL = "INSERT INTO User (name, lastName, age) VALUES (?, ?, ?);";

        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, name);
            pstmt.setString(2, lastName);
            pstmt.setByte(3, age);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Добавлено записей: " + rowsAffected);

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении пользователя", e);
        }
    }

    public void removeUserById(long id) {
        String deleteSQL = "DELETE FROM User WHERE id = ?;";

        try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setLong(1, id);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Пользователь с ID " + id + " удален.");
            } else {
                System.out.println("Пользователь с ID " + id + " не найден.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении пользователя по ID", e);
        }
    }

    public List<User> getAllUsers() {
        String selectSQL = "SELECT id, name, lastName, age FROM User;";
        List<User> users = new ArrayList<>();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {

            boolean hasResults = false;

            while (rs.next()) {
                hasResults = true;
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String lastName = rs.getString("lastName");
                byte age = rs.getByte("age");

                users.add(new User(id, name, lastName, age));
                System.out.println("ID: " + id + ", Имя: " + name + ", Фамилия: " + lastName + ", Возраст: " + age);
            }

            if (!hasResults) {
                System.out.println("Таблица 'User' пуста.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении всех пользователей", e);
        }

        return users;
    }

    public void cleanUsersTable() {
        String deleteSQL = "DELETE FROM User;";

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(deleteSQL);
            System.out.println("Все данные из таблицы 'User' удалены.");
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при очистке таблицы", e);
        }
    }
}