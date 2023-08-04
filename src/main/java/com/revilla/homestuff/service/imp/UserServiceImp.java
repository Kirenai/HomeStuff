package com.revilla.homestuff.service.imp;

import com.revilla.homestuff.dto.UserDto;
import com.revilla.homestuff.dto.response.ApiResponseDto;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.exception.entity.EntityNoSuchElementException;
import com.revilla.homestuff.mapper.GenericMapper;
import com.revilla.homestuff.mapper.user.UserMapper;
import com.revilla.homestuff.repository.RoleRepository;
import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.security.AuthUserDetails;
import com.revilla.homestuff.service.UserService;
import com.revilla.homestuff.util.ConstraintViolation;
import com.revilla.homestuff.util.GeneralUtil;
import com.revilla.homestuff.util.RoleUtil;
import com.revilla.homestuff.util.enums.MessageAction;
import com.revilla.homestuff.util.enums.RoleName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * NourishmentService
 *
 * @author Kirenai
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImp extends GeneralServiceImp<UserDto, Long, User> implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public JpaRepository<User, Long> getRepo() {
        return this.userRepository;
    }

    @Override
    public GenericMapper<UserDto, User> getMapper() {
        return this.userMapper;
    }

    @Transactional
    @Override
    public UserDto create(UserDto data) {
        log.info("Invoking UserServiceImp.create method");
        ConstraintViolation.validateDuplicate(data.getUsername(), this.userRepository, User.class);
        // TODO: Add an input and output mapper in the appropriate class for all entities
        User user = this.userMapper.mapIn(data);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        user.setRoles(RoleUtil.getSetOfRolesOrThrow(data.getRoles(), this.roleRepository));
        User userSaved = this.userRepository.save(user);
        return this.userMapper.mapOut(userSaved)
                .setMessage(GeneralUtil.simpleNameClass(User.class)
                        + " created successfully by admin");
    }

    @Transactional
    @Override
    public ApiResponseDto update(Long id, UserDto data, AuthUserDetails userDetails) {
        log.info("Invoking UserServiceImp.update method");
        return this.userRepository.findById(id)
                .map(user -> {
                    GeneralUtil.validateAuthorizationPermissionOrThrow(user, userDetails,
                            MessageAction.UPDATE);
                    user.setUsername(data.getUsername());
                    user.setPassword(this.passwordEncoder.encode(data.getPassword()));
                    user.setFirstName(data.getFirstName());
                    user.setLastName(data.getLastName());
                    user.setAge(data.getAge());
                    if (Objects.nonNull(data.getRoles())
                            && userDetails.getAuthorities().contains(
                            new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.name()))) {
                        user.setRoles(RoleUtil.getSetOfRolesOrThrow(data.getRoles(), this.roleRepository));
                    }
                    return GeneralUtil.responseMessageAction(User.class, "updated successfully");
                })
                .orElseThrow(() -> new EntityNoSuchElementException(
                        GeneralUtil.simpleNameClass(User.class)
                                + " not found with id: " + id
                ));
    }

}
