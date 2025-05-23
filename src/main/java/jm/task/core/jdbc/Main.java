package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        UserDao userDao = new UserDaoJDBCImpl();
        UserService userService = new UserServiceImpl(userDao);

        System.out.println("Удаление таблицы...");
        userService.dropUsersTable();

        System.out.println("Создание таблицы...");
        userService.createUsersTable();

        System.out.println("Добавление пользователей...");
        userService.saveUser("Иван", "Иванов", (byte) 25);
        userService.saveUser("Семён", "Семёнов", (byte) 30);
        userService.saveUser("Алексей", "Смирнов", (byte) 28);

        System.out.println("Все пользователи:");
        userService.getAllUsers().forEach(System.out::println);

    }

}
