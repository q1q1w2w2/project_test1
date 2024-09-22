package com.example.demo1.service;

import com.example.demo1.domain.User;
import com.example.demo1.dto.JoinDto;
import com.example.demo1.dto.UpdateDto;
import com.example.demo1.exception.user.UserAlreadyExistException;
import com.example.demo1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User join(JoinDto dto) {

        if (userRepository.findByLoginId(dto.getLoginId()).isPresent()) {
            throw new UserAlreadyExistException("이미 존재하는 아이디입니다.");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .birth(dto.getBirth())
                .tel(dto.getTel())
                .address(dto.getAddress())
                .detail(dto.getDetail())
                .loginId(dto.getLoginId())
                .password(passwordEncoder.encode(dto.getPassword()))
                .authority("ROLE_USER")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }

    @Transactional
    public User joinAdmin(JoinDto dto) {

        if (userRepository.findByLoginId(dto.getLoginId()).isPresent()) {
            throw new UserAlreadyExistException("이미 존재하는 아이디입니다.");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .birth(dto.getBirth())
                .tel(dto.getTel())
                .address(dto.getAddress())
                .detail(dto.getDetail())
                .loginId(dto.getLoginId())
                .password(passwordEncoder.encode(dto.getPassword()))
                .authority("ROLE_ADMIN")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public void update(Long id, UpdateDto dto) {
        User findUser = userRepository.findById(id);

        User user = User.builder()
                .password(passwordEncoder.encode(dto.getPassword()))
                .tel(dto.getTel())
                .address(dto.getAddress())
                .detail(dto.getDetail())
                .updatedAt(LocalDateTime.now())
                .build();

        findUser.updateUser(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id);
    }
}