package org.example.Dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.entity.User;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserDao {

    @Select("select * from user where id =#{id}")
    public User getById(@Param("id") int id);
}
