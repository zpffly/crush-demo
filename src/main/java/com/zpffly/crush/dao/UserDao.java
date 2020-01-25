package com.zpffly.crush.dao;

import com.zpffly.crush.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserDao {

    @Select("select * from crush_user where id = #{id}")
    User getUserById(@Param("id") long id);
}
