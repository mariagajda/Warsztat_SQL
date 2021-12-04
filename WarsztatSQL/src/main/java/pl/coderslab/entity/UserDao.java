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
            PreparedStatement preparedStatement = conn.prepareStatement(QUERY_READ);
            preparedStatement.setString(1, Integer.toString(userId));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt(1));
                user.setEmail(resultSet.getString(2));
                user.setUserName(resultSet.getString(3));
                user.setPassword(resultSet.getString(4));
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
            PreparedStatement preparedStatement = conn.prepareStatement(QUERY_UPDATE);
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getUserName());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setInt(4, user.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int userId) {
        try (Connection conn = DbUtil.connectWorkshop2()) {
            PreparedStatement preparedStatement = conn.prepareStatement(QUERY_DELETE);
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User[] findAll() {
        try (Connection conn = DbUtil.connectWorkshop2()) {
            PreparedStatement preparedStatement = conn.prepareStatement(QUERY_FINDALL);
            ResultSet resultSet = preparedStatement.executeQuery();
            User[] users = new User[0];
            while (resultSet.next()) {
                User newUser = new User();
                newUser.setId(resultSet.getInt(1));
                newUser.setEmail(resultSet.getString(2));
                newUser.setUserName(resultSet.getString(3));
                newUser.setPassword(resultSet.getString(4));
                users = addToArray(newUser, users);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private User[] addToArray(User u, User[] users) {
        User[] tmpUsers = Arrays.copyOf(users, users.length + 1);
        tmpUsers[users.length] = u;
        return tmpUsers;
    }

}
