package com.server.lms.user.service;

import com.server.lms._shared.exception.DuplicateFieldException;
import com.server.lms._shared.exception.EntityNotFoundException;
import com.server.lms.user.dto.request.UserRequest;
import com.server.lms.user.dto.response.UserResponse;
import com.server.lms.user.repository.UserRepository;
import com.server.lms.user.entity.User;
import com.server.lms.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public User create(UserRequest dto) {
        var user = userRepository.findByEmail(dto.getEmail());
        if (user.isPresent())
            throw new DuplicateFieldException("User with email " + dto.getEmail() + " already exists!");

        return userRepository.save(userMapper.toEntity(dto));
    }

    @Override
    public UserResponse getCurrentUser() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = findEntityByEmail(userEmail);
        return userMapper.toDTO(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public void updateLastLogin(String userId, LocalDateTime lastLogin) {
        userRepository.updateLastLogin(userId, lastLogin);
    }


    //=========== HELPERS ===========//
    public User findEntityByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Email Or Password Not Found"
                ));
    }
}
