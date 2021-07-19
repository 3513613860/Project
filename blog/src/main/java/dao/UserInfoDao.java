package dao;

import models.UserInfo;
import utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserInfoDao {

    //查找是否有此用户（登录）
    public  boolean find(UserInfo userInfo) throws SQLException {
        boolean ret = false;
        if(userInfo.getUsername() != null && userInfo.getPassword() != null
        && !userInfo.getPassword().equals("") && !userInfo.getUsername().equals("")){
            Connection connection = DBUtils.getConnect();
            String sql = "select * from userinfo where  username = ? and password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,userInfo.getUsername());
            statement.setString(2,userInfo.getPassword());

            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                ret = true;
            }

            DBUtils.close(connection,statement,resultSet);
        }
        return ret;
    }

    //添加用户（注册）
    public boolean add(UserInfo userInfo) throws SQLException {
        boolean ret = false;
        if(userInfo.getUsername() != null && userInfo.getPassword() != null){
            //正常的参数
            Connection connection = DBUtils.getConnect();
            String sql = "insert into userinfo(username,password) values(?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,userInfo.getUsername());
            preparedStatement.setString(2,userInfo.getPassword());

            ret = preparedStatement.executeUpdate() >= 1 ? true:false;

            //关闭连接
            DBUtils.close(connection,preparedStatement,null);
        }
        return ret;
    }

    //获取userinfo对象
    public UserInfo getUserInfo(UserInfo userInfo) throws SQLException {
        UserInfo user = new UserInfo();

        Connection connection = DBUtils.getConnect();
        String sql = "select * from userinfo where username = ? and password = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,userInfo.getUsername());
        statement.setString(2,userInfo.getPassword());
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            user.setId(resultSet.getInt("id"));
            user.setUsername(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));
        }

        return user;
    }
}
