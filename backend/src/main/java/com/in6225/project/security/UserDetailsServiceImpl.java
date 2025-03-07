package com.in6225.project.security;

import com.in6225.project.entity.User;
import com.in6225.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String employeeId) throws UsernameNotFoundException {
        // 通过用户名查找用户（假设用户名是 employeeId）
        User user = userRepository.findByEmployeeId(employeeId);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + employeeId);
        }

        // 获取用户角色
        List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));

        // 返回 UserDetails 对象
        return new org.springframework.security.core.userdetails.User(user.getEmployeeId(), user.getPassword(), authorities);
    }}
