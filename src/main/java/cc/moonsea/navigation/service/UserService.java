package cc.moonsea.navigation.service;

import cc.moonsea.navigation.config.AppConfig;
import cc.moonsea.navigation.dto.ChangePasswordRequest;
import cc.moonsea.navigation.dto.RegisterRequest;
import cc.moonsea.navigation.entity.User;
import cc.moonsea.navigation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppConfig appConfig;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("USER")
                .build();
    }

    @Transactional
    public User registerUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("邮箱已被注册");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("两次密码输入不一致");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());

        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public boolean changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 验证当前密码是否正确
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("当前密码不正确");
        }

        // 验证新密码和确认密码是否一致
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new RuntimeException("新密码和确认密码不一致");
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return true;
    }

    public User getDefaultUser() {
        String defaultUsername = appConfig.getDefaultUser().getUsername();
        return userRepository.findByUsername(defaultUsername)
                .orElseThrow(() -> new RuntimeException("默认用户不存在"));
    }

    public User getDefaultUser(Authentication authentication) {
        User user;
        if (authentication != null && authentication.isAuthenticated() &&
                !"anonymousUser".equals(authentication.getPrincipal())) {
            String username = authentication.getName();
            user = userRepository.findByUsername(username).orElse(null);
            if (user == null) {
                user = getDefaultUser();
            }
        } else {
            user = getDefaultUser();
        }

        return user;
    }
}
