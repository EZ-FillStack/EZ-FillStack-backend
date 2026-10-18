package com.ezwell.backend.domain.admin;

import com.ezwell.backend.domain.admin.dto.UserRoleUpdateRequest;
import com.ezwell.backend.domain.user.UserRepository;
import com.ezwell.backend.domain.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserRepository userRepository;
    private final AdminService adminService;

    //전체 유저 조회
    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::from)
                .toList();
    }

    //유저 권한 변경
    @PatchMapping("/{id}/role")
    public void changeRole(@PathVariable Long id,
                           @RequestBody UserRoleUpdateRequest request) {
        adminService.changeUserRole(id, request.role());
    }

    // 회원 삭제
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
    }
}