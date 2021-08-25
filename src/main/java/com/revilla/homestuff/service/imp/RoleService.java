package com.revilla.homestuff.service.imp;

import java.util.List;
import java.util.stream.Collectors;
import com.revilla.homestuff.dto.RoleDto;
import com.revilla.homestuff.entity.Role;
import com.revilla.homestuff.repository.RoleRepository;
import com.revilla.homestuff.service.GeneralService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * RoleService
 * @author Kirenai
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Qualifier("role.service")
public class RoleService implements GeneralService<RoleDto, Long>{

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

	@Override
	public List<RoleDto> findAll(Pageable pageable) {
        log.info("Calling the findAll methond in RoleService");
        return this.roleRepository.findAll(pageable)
            .getContent()
            .stream()
            .map(u -> this.modelMapper.map(u, RoleDto.class))
            .collect(Collectors.toList());
	}

	@Override
	public RoleDto findOne(Long id) {
        log.info("Calling the findOne methond in RoleService");
        Role role = this.roleRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("User don't found"));
        return this.modelMapper.map(role, RoleDto.class);
	}

	@Override
	public RoleDto create(RoleDto data) {
        log.info("Calling the create methond in RoleService");
        Role role = this.modelMapper.map(data, Role.class);
        Role roleSaved = this.roleRepository.save(role);
        return this.modelMapper.map(roleSaved, RoleDto.class);
	}

	@Override
	public RoleDto update(Long id, RoleDto data) {
        log.info("Calling the update methond in RoleService");
        return this.roleRepository.findById(id)
            .map(u -> {
                u.setName(data.getName());
                return this.modelMapper.map(this.roleRepository.save(u), RoleDto.class);
            })
            .orElseThrow(() -> new IllegalStateException("User don't found"));
	}

	@Override
	public RoleDto delete(Long id) {
        log.info("Calling the delete methond in RoleService");
        return this.roleRepository.findById(id)
            .map(u -> {
                this.roleRepository.delete(u);
                return this.modelMapper.map(u, RoleDto.class);
            })
            .orElseThrow(() -> new IllegalStateException("User don't found"));
	}

}
