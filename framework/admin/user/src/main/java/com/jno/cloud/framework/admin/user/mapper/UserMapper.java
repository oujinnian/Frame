package com.jno.cloud.framework.admin.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jno.cloud.framework.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
