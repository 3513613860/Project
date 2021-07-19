package dao;

import models.ArticleInfo;
import models.vo.ArticleInfoVO;
import utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/*
    增删改查
 */
public class ArticleInfoDao {
    public List<ArticleInfo> getArtList(int id) throws SQLException {
        List<ArticleInfo> list = new ArrayList<>();
        Connection connection = DBUtils.getConnect();
        String sql = "select * from articleinfo where uid = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1,id);
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()){
            ArticleInfo articleInfo = new ArticleInfo();
            articleInfo.setId(resultSet.getInt("id"));
            articleInfo.setTitle(resultSet.getString("title"));
            articleInfo.setContent(resultSet.getString("content"));
            articleInfo.setCreatetime(resultSet.getDate("createtime"));
            articleInfo.setUpdatetime(resultSet.getDate("updatetime"));
            articleInfo.setRcount(resultSet.getInt("rcount"));

            list.add(articleInfo);
        }

        return list;
    }

    public boolean del(int id) throws SQLException {
        boolean ret = false;
        Connection connection = DBUtils.getConnect();
        String sql = "delete from articleinfo where id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1,id);
        ret = statement.executeUpdate() >= 1 ? true : false;
        DBUtils.close(connection,statement,null);

        return ret;
    }

    public int add(String title, String content,int uid) throws SQLException {
        int ret = 0;
        Connection connection = DBUtils.getConnect();
        String sql = "insert into articleinfo(title,content,uid) values(?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,title);
        statement.setString(2,content);
        statement.setInt(3,uid);
        ret = statement.executeUpdate();

        DBUtils.close(connection,statement,null);
        return ret;
    }

    public int upart(String title, String content, int id) throws SQLException {
        int ret = -1;
        Connection connection = DBUtils.getConnect();
        String sql = "update  articleinfo set title = ?,content = ? where id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,title);
        statement.setString(2,content);
        statement.setInt(3,id);
        ret = statement.executeUpdate();

        DBUtils.close(connection,statement,null);
        return ret;
    }

    //文章查询
    public ArticleInfoVO getArtById(int id) throws SQLException {
        ArticleInfoVO articleInfoVO = new ArticleInfoVO();
        Connection connection = DBUtils.getConnect();
        String sql = "select a.*,b.username from articleinfo a left join userinfo b on a.uid = b.id where a.id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1,id);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()){
            articleInfoVO.setTitle(resultSet.getString("title"));
            articleInfoVO.setContent(resultSet.getString("content"));
            articleInfoVO.setCreatetime(resultSet.getDate("createtime"));
            articleInfoVO.setRcount(resultSet.getInt("rcount"));
            articleInfoVO.setUsername(resultSet.getString("username"));

        }
        return articleInfoVO;
    }

    public List<ArticleInfoVO> getList() throws SQLException {
        List<ArticleInfoVO> list = new ArrayList<>();
        Connection connection = DBUtils.getConnect();
        String sql = "select a.*,u.username from articleinfo a left join userinfo u on a.uid = u.id";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()){
            ArticleInfoVO articleInfo = new ArticleInfoVO();
            articleInfo.setId(resultSet.getInt("id"));
            articleInfo.setContent(resultSet.getString("content"));
            articleInfo.setTitle(resultSet.getString("title"));
            articleInfo.setRcount(resultSet.getInt("rcount"));
            articleInfo.setCreatetime(resultSet.getDate("createtime"));
            articleInfo.setUpdatetime(resultSet.getDate("updatetime"));
            articleInfo.setState(resultSet.getInt("state"));
            articleInfo.setUid(resultSet.getInt("uid"));
            articleInfo.setUsername(resultSet.getString("username"));
            list.add(articleInfo);
        }
        DBUtils.close(connection,statement,resultSet);
        return list;
    }

    //根据分页查询文章列表
    public List<ArticleInfoVO> getListByPage(int curpage, int psize) throws SQLException {
        List<ArticleInfoVO> list = new ArrayList<>();
        Connection connection = DBUtils.getConnect();
        String sql = "select a.*,u.username from articleinfo a left join userinfo u on a.uid = u.id limit ?,?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1,(curpage-1)*psize);
        statement.setInt(2,psize);
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()){
            ArticleInfoVO articleInfo = new ArticleInfoVO();
            articleInfo.setId(resultSet.getInt("id"));
            articleInfo.setContent(resultSet.getString("content"));
            articleInfo.setTitle(resultSet.getString("title"));
            articleInfo.setRcount(resultSet.getInt("rcount"));
            articleInfo.setCreatetime(resultSet.getDate("createtime"));
            articleInfo.setUpdatetime(resultSet.getDate("updatetime"));
            articleInfo.setState(resultSet.getInt("state"));
            articleInfo.setUid(resultSet.getInt("uid"));
            articleInfo.setUsername(resultSet.getString("username"));
            list.add(articleInfo);
        }
        DBUtils.close(connection,statement,resultSet);
        return list;
    }

    //更新文章阅读量
    public int upcount(int id) throws SQLException {
        int ret = -1;
        Connection connection = DBUtils.getConnect();
        String sql = "update articleinfo set rcount = rcount+1 where id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1,id);
        ret = statement.executeUpdate();
        DBUtils.close(connection,statement,null);

        return ret;
    }
}
