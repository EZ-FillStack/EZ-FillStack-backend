package com.ezwell.backend.domain.admin;

import com.ezwell.backend.domain.user.User;
import com.ezwell.backend.domain.user.UserRepository;
import com.ezwell.backend.domain.user.Role;
import com.ezwell.backend.domain.admin.dto.RoleChangeRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 관리자 전용 유저 관리 API
 *
 * - ADMIN 권한만 접근 가능
 * - 유저 목록 조회
 * - 권한 변경 기능 제공
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
// @PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserRepository userRepository;

    /**
     * 전체 유저 조회
     *
     * - 토이 프로젝트 기준으로 단순 조회
     * - 비밀번호는 엔티티에 있으므로 실제 운영 시에는 DTO 분리 필요
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * 유저 권한 변경
     *
     * 예:
     * PATCH /admin/users/3/role
     * body: { "role": "ADMIN" }
     */
    @PatchMapping("/{id}/role")
    public void changeRole(@PathVariable Long id,
                           @RequestBody RoleChangeRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

        // enum 변환 (잘못된 값 들어오면 IllegalArgumentException 발생)
        Role newRole = Role.valueOf(request.role());

        user.changeRole(newRole);
    }
}