package com.revilla.homestuff.api;

import com.revilla.homestuff.dto.RoleDto;
import com.revilla.homestuff.security.AuthUserDetails;
import com.revilla.homestuff.security.CurrentUser;
import com.revilla.homestuff.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RoleResource
 * @author Kirenai
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/roles")
public class RoleResource {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<List<RoleDto>> getRoles(
        @PageableDefault(size = 2)
        @SortDefault.SortDefaults(value = {
                @SortDefault(sort = "roleId", direction = Sort.Direction.ASC)
        }) Pageable pageable
    ) {
        List<RoleDto> response = this.roleService.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<RoleDto> getRole(@PathVariable Long roleId,
                                           @CurrentUser AuthUserDetails userDetails) {
        RoleDto response = this.roleService.findOne(roleId, userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<RoleDto> createRole(@RequestBody RoleDto roleDto) {
        RoleDto response = this.roleService.create(roleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<RoleDto> updateRole(@PathVariable Long roleId,
                                              @RequestBody RoleDto roleDto) {
        RoleDto response = this.roleService.update(roleId, roleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
