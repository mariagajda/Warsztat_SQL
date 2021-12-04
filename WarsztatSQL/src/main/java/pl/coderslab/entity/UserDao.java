package pl.coderslab.entity;

import org.mindrot.jbcrypt.BCrypt;

import javax.swing.plaf.IconUIResource;
import java.sql.*;
import java.util.Arrays;

public class UserDao {
    private static final String CREATE_USER_QUERY = "INSERT INTO users(username, email, password) VALUES (?, ?, ?);";
    private static final String QUERY_READ = "select * from users where id = ?;";
    private static final String QUERY_UPDATE = "update users set email = ?, username = ?, password = ? where id = ?;";
    private static final String QUERY_DELETE = "delete from users where id = ?;";
    private static final String QUERY_FINDALL = "select * from users";


    public static void main(String[] args) {

        UserDao userDao = new UserDao();
        /*User user1 = new User();
        user1.setUserName("Anna");
        user1.setEmail("anna@gmail.com");
        user1.setPassword("StrongPassword1");
        userDao.create(user1);

        UserDao userDao2 = new UserDao();
        User user2 = new User();
        user2.setUserName("Robert");
        user2.setEmail("robert@gmail.com");
        user2.setPassword("StrongPassword2");
        userDao2.create(user2);

        UserDao userDao3 = new UserDao();
        User user3 = new User();
        user3.setUserName("Tom");
        user3.setEmail("tom@gmail.com");
        user3.setPassword("StrongPassword3");
        userDao3.create(user3);

        UserDao userDao4 = new UserDao();
        User user4 = new User();
        user4.setUserName("Lisa");
        user4.setEmail("lisa@gmail.com");
        user4.setPassword("StrongPassword4");
        userDao4.create(user4);

        UserDao userDao5 = new UserDao();
        User user5 = new User();
        user5.setUserName("John");
        user5.setEmail("john@gmail.com");
        user5.setPassword("StrongPassword5");
        userDao5.create(user5);


        User user = new User();
        user.setId(userDao.read(4).getId());
        user.setUserName("Andrew");
        user.setEmail("andrew@gmail.com");
        user.setPassword("NewStrongPassword7");
        userDao.update(user);


        userDao.delete(6);


        System.out.println(userDao.read(9));*/

        User read = userDao.read(1);
        System.out.println(userDao.toString(read));

        // User readNotExist = userDao.read(2);

        //User userToUpdate = userDao.read(1);
        //userToUpdate.setUserName("Minnie");
        //userToUpdate.setEmail("minnie@coderslab.pl");
        //userToUpdate.setPassword("StrongPasswordUpdated");
        //userDao.update(userToUpdate);

        User[] all = userDao.findAll();
        for (User user : all) {
            System.out.println(userDao.toString(user));
        }
    }

    public User create(User user) {
        try (Connection conn = DbUtil.connectWorkshop2()) {
            PreparedStatement statement = conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public User read(int userId) {
        try (Connection conn = DbUtil.connectWorkshop2()) {
            PreparedStatement preparedStatement1 = conn.prepareStatement(QUERY_READ);
            preparedStatement1.setString(1, Integer.toString(userId));
            ResultSet resultSet1 = preparedStatement1.executeQuery();
            if (resultSet1.next()) {
                int user_Id = resultSet1.getInt(1);
                String user_Email = resultSet1.getString(2);
                String user_Name = resultSet1.getString(3);
                String user_Password = resultSet1.getString(4);
                User user = new User();
                user.setId(userId);
                user.setEmail(user_Email);
                user.setUserName(user_Name);
                user.setPassword(user_Password);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public void update(User user) {
        try (Connection conn = DbUtil.connectWorkshop2()) {
            PreparedStatement preparedStatement2 = conn.prepareStatement(QUERY_UPDATE);
            preparedStatement2.setString(1, user.getEmail());
            preparedStatement2.setString(2, user.getUserName());
            preparedStatement2.setString(3, user.getPassword());
            preparedStatement2.setInt(4, user.getId());
            preparedStatement2.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int userId) {
        try (Connection conn = DbUtil.connectWorkshop2()) {
            PreparedStatement preparedStatement3 = conn.prepareStatement(QUERY_DELETE);
            preparedStatement3.setInt(1, userId);
            preparedStatement3.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User[] findAll() {
        try (Connection conn = DbUtil.connectWorkshop2()) {
            PreparedStatement preparedStatement4 = conn.prepareStatement(QUERY_FINDALL);
            ResultSet resultSet4 = preparedStatement4.executeQuery();
            User[] tmpUsers = new User[0];
            while (resultSet4.next()) {
                int user_Id = resultSet4.getInt(1);
                String user_Email = resultSet4.getString(2);
                String user_Name = resultSet4.getString(3);
                String user_Password = resultSet4.getString(4);
                User newUser = new User();
                newUser.setId(user_Id);
                newUser.setEmail(user_Email);
                newUser.setUserName(user_Name);
                newUser.setPassword(user_Password);
                tmpUsers = addToArray(newUser, tmpUsers);
            }
            return tmpUsers;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private User[] addToArray(User u, User[] users) {
        User[] tmpUsers = Arrays.copyOf(users, users.length + 1); // Tworzymy kopię tablicy powiększoną o 1.
        tmpUsers[users.length] = u; // Dodajemy obiekt na ostatniej pozycji.
        return tmpUsers; // Zwracamy nową tablicę.
    }

    public String toString (User user) {
        return "id: " + user.getId() + " username: " + user.getUserName() + " email: " + user.getEmail();
    }

}
