package pl.coderslab.entity;

public class User {
    private static int id;
    private static String userName;
    private static String email;
    private static String password;


    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        UserDao userDao = new UserDao();
        return userDao.hashPassword(password);
    }

    public void setPassword(String password) {
        UserDao userDao = new UserDao();
        this.password = userDao.hashPassword(password);
    }
}
