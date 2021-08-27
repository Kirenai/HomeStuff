package com.revilla.homestuff.api;

import java.util.List;
import com.revilla.homestuff.dto.UserDto;
import com.revilla.homestuff.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserResource {

    @Qualifier("user.service")
    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers(Pageable pageable) {
        return this.userService.findAll(pageable);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long userId) {
        UserDto response = this.userService.findOne(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto data) {
        UserDto response = this.userService.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId,
            @RequestBody UserDto userDto) {
        UserDto response = this.userService.update(userId, userDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable Long userId) {
        UserDto response = this.userService.delete(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
