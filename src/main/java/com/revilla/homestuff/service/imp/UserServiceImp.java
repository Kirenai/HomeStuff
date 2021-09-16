package com.revilla.homestuff.service.imp;

import com.revilla.homestuff.dto.UserDto;
import com.revilla.homestuff.dto.request.RegisterRequestDto;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.exception.entity.EntityNoSuchElementException;
import com.revilla.homestuff.exception.unauthorize.UnauthorizedPermissionException;
import com.revilla.homestuff.repository.RoleRepository;
import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.security.AuthUserDetails;
import com.revilla.homestuff.service.UserService;
import com.revilla.homestuff.util.GeneralUtil;
import com.revilla.homestuff.util.RoleUtil;
import com.revilla.homestuff.util.enums.RoleName;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * NourishmentService
 *
 * @author Kirenai
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Qualifier("user.service")
public class UserServiceImp extends GeneralServiceImp<UserDto, Long, User> implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public JpaRepository<User, Long> getRepo() {
        return this.userRepository;
    }

    @Transactional
    @Override
    public UserDto register(RegisterRequestDto requestDto) {
        log.info("Calling the register method in "
                + GeneralUtil.simpleNameClass(this.getClass()));
        GeneralUtil.validateDuplicateConstraintViolation(requestDto.getUsername(),
                this.userRepository, User.class);
        User user = super.getModelMapper().map(requestDto, super.getThirdGenericClass());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(RoleUtil.getSetOfRolesOrThrow(null, this.roleRepository));
        User userRegistered = this.userRepository.save(user);
        return super.getModelMapper().map(userRegistered, super.getFirstGenericClass())
                .setMessage("Successfully registered "
                        + GeneralUtil.simpleNameClass(User.class));
    }

    @Transactional
    @Override
    public UserDto create(UserDto data) {
        log.info("Calling the create method in "
                + GeneralUtil.simpleNameClass(this.getClass()));
        GeneralUtil.validateDuplicateConstraintViolation(data.getUsername(),
                this.userRepository, User.class);
        User user = super.getModelMapper().map(data, super.getThirdGenericClass());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(RoleUtil.getSetOfRolesOrThrow(data.getRoles(), this.roleRepository));
        User userSaved = this.userRepository.save(user);
        return super.getModelMapper().map(userSaved, super.getFirstGenericClass())
                .setMessage(GeneralUtil.simpleNameClass(User.class)
                        + " created successfully by admin");
    }

    @Transactional
    @Override
    public UserDto update(Long id, UserDto data, AuthUserDetails userDetails) {
        return this.userRepository.findById(id)
                .map(user -> {
                    if (user.getUserId().equals(userDetails.getUserId())
                            || userDetails.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.name()))) {
                        user.setUsername(data.getUsername());
                        user.setPassword(this.passwordEncoder.encode(data.getPassword()));
                        user.setFirstName(data.getFirstName());
                        user.setLastName(data.getLastName());
                        user.setAge(data.getAge());
                        if (Objects.nonNull(data.getRoles())
                                && userDetails.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.name()))) {
                            user.setRoles(RoleUtil.getSetOfRolesOrThrow(data.getRoles(), this.roleRepository));
                        }
                        return super.getModelMapper()
                                .map(this.userRepository.save(user),
                                        super.getFirstGenericClass())
                                .setMessage(GeneralUtil.simpleNameClass(User.class)
                                        + " updated successfully");
                    }
                    throw new UnauthorizedPermissionException(
                            "You don't have the permission to update this profile");
                })
                .orElseThrow(() -> new EntityNoSuchElementException(
                        GeneralUtil.simpleNameClass(User.class)
                                + " don't found with id: " + id
                ));
    }

}
