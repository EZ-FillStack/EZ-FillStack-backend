package com.ezwell.backend.domain.admin;

import com.ezwell.backend.domain.user.User;
import com.ezwell.backend.domain.user.UserRepository;
import com.ezwell.backend.domain.admin.dto.UserRoleUpdateRequest;
import com.ezwell.backend.domain.user.dto.UserResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserRepository userRepository;

    /**
     * 전체 유저 조회 (DTO 변환)
     */
    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::from)
                .toList();
    }

    /**
     * 권한 변경
     */
    @PatchMapping("/{id}/role")
    public void changeRole(@PathVariable Long id,
                           @RequestBody UserRoleUpdateRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

        user.changeRole(request.role());
    }
}