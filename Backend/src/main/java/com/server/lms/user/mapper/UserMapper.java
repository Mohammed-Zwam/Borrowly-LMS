package com.server.lms.user.mapper;

import com.server.lms._shared.base.BaseMapper;
import com.server.lms.user.dto.request.UserRequest;
import com.server.lms.user.dto.response.UserResponse;
import com.server.lms.user.entity.User;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class UserMapper extends BaseMapper<UserRequest, UserResponse, User> {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Mapping(target = "password", qualifiedByName = "passwordEncoding")
    abstract public User toEntity(UserRequest dto);


    @Named("passwordEncoding")
    String passwordEncoding(String password) {
        if (password == null) return null;
        return passwordEncoder.encode(password);
    }

}
