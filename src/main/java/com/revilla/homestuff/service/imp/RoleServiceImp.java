package com.revilla.homestuff.service.imp;

import com.revilla.homestuff.dto.RoleDto;
import com.revilla.homestuff.dto.response.ApiResponseDto;
import com.revilla.homestuff.entity.Role;
import com.revilla.homestuff.exception.entity.EntityNoSuchElementException;
import com.revilla.homestuff.mapper.GenericMapper;
import com.revilla.homestuff.mapper.role.RoleMapper;
import com.revilla.homestuff.repository.RoleRepository;
import com.revilla.homestuff.service.RoleService;
import com.revilla.homestuff.util.ConstraintViolation;
import com.revilla.homestuff.util.GeneralUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * RoleServiceImp
 *
 * @author Kirenai
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RoleServiceImp extends GeneralServiceImp<RoleDto, Long, Role> implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public JpaRepository<Role, Long> getRepo() {
        return this.roleRepository;
    }

    @Override
    public GenericMapper<RoleDto, Role> getMapper() {
        return this.roleMapper;
    }

    @Override
    public RoleDto create(RoleDto data) {
        log.info("Invoking RoleServiceImp.create method");
        ConstraintViolation.validateDuplicate(data.getName().name(),
                this.roleRepository, Role.class);
        Role role = this.roleMapper.mapIn(data);
        Role roleSaved = this.roleRepository.save(role);
        return this.roleMapper.mapOut(roleSaved);
    }

    @Override
    public ApiResponseDto update(Long id, RoleDto data) {
        log.info("Invoking RoleServiceImp.update method");
        return this.roleRepository.findById(id)
                .map(role -> {
                    role.setName(data.getName().name());
                    return GeneralUtil.responseMessageAction(Role.class,
                            "updated successfully");
                })
                .orElseThrow(() -> new EntityNoSuchElementException(
                        GeneralUtil.simpleNameClass(Role.class)
                                + " not found with id: " + id)
                );
    }

}
