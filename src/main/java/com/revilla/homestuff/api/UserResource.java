package com.revilla.homestuff.api;

import com.revilla.homestuff.dto.UserDto;
import com.revilla.homestuff.service.GeneralService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserResource {

    @Qualifier("user.service")
    private final GeneralService<UserDto, Long> userService;

    @GetMapping
    public List<UserDto> getUsers(Pageable pageable) {
        return this.userService.findAll(pageable);
    }

    @PostMapping
    public UserDto saveUser(@RequestBody UserDto data) {
        return this.userService.create(data);
    }

}
