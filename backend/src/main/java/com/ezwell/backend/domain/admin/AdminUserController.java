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

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::from)
                .toList();
    }

    @PatchMapping("/{id}/role")
    public void changeRole(@PathVariable Long id,
                           @RequestBody UserRoleUpdateRequest request) {
        adminService.changeUserRole(id, request.role());
    }
}