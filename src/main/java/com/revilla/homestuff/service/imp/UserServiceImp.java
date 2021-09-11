package com.revilla.homestuff.service.imp;

import javax.transaction.Transactional;

import com.revilla.homestuff.dto.UserDto;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.exception.entity.EntityNoSuchElementException;
import com.revilla.homestuff.exception.unauthorize.UnauthorizedPermissionException;
import com.revilla.homestuff.repository.RoleRepository;
import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.security.AuthUserDetails;
import com.revilla.homestuff.service.UserService;
import com.revilla.homestuff.util.GeneralUtil;
import com.revilla.homestuff.util.RoleUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

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
    public UserDto create(UserDto data) {
        log.info("Calling the create method in "
                + GeneralUtil.simpleNameClass(this.getClass()));
        GeneralUtil.validateDuplicateConstraintViolation(data.getUsername(),
                this.userRepository, User.class);
        User user = super.getModelMapper().map(data, super.getThirdGenericClass());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // TODO: possible backdoor
        user.setRoles(RoleUtil.getSetOfRolesOrThrow(data.getRoles(), this.roleRepository));
        User userSaved = this.userRepository.save(user);
        return super.getModelMapper().map(userSaved, super.getFirstGenericClass());
    }

    @Override
    public UserDto update(Long id, UserDto data, AuthUserDetails userDetails) {
        return this.userRepository.findById(id)
                .map(user -> {
                    if (user.getUserId().equals(userDetails.getUserId())
                            || userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                        user.setUsername(data.getUsername());
                        user.setPassword(this.passwordEncoder.encode(data.getPassword()));
                        user.setFirstName(data.getFirstName());
                        user.setLastName(data.getLastName());
                        user.setAge(data.getAge());
                        return super.getModelMapper()
                                .map(this.userRepository.save(user),
                                        super.getFirstGenericClass());
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
