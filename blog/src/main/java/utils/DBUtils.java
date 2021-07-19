package utils;


import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.*;

/*
    通用数据库操作方法
    1.对外提供connection对象
    2.提供统一的关闭方法
 */
public class DBUtils {
    private static MysqlDataSource dataSource = null;

    //对外提供统一的connection连接对象
    public static Connection getConnect() throws SQLException {
        if (dataSource == null){
            //首次调用，先初始化
            dataSource = new MysqlDataSource();
            //1.设置连接的服务器地址
            dataSource.setURL("jdbc:mysql://127.0.0.1:3306/blog?characterEncoding=utf-8&useSSL=false");
            //2.设置用户名
            dataSource.setUser("root");
            //3.设置密码
            dataSource.setPassword("12345678");
        }
        return dataSource.getConnection();
    }

    public static void close(Connection connection, PreparedStatement statement, ResultSet resultSet) throws SQLException {
        if(resultSet != null){
            resultSet.close();
        }

        if(statement != null){
            statement.close();
        }

        if(connection != null){
            connection.close();
        }
    }
}
