package com.revilla.homestuff.service.imp;

import javax.transaction.Transactional;
import com.revilla.homestuff.dto.UserDto;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.service.UserService;
import com.revilla.homestuff.util.GeneralUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * NourishmentService
 * @author Kirenai
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Qualifier("user.service")
public class UserServiceImp extends GeneralServiceImp<UserDto, Long, User> implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
	public JpaRepository<User, Long> getRepo() {
		return this.userRepository;
	}

    @Transactional
    @Override
    public UserDto create(UserDto data) {
        log.info("Calling the create method in " + this.getClass());
        GeneralUtil.validateDuplicateConstraintViolation(data.getUsername(),
                this.userRepository, User.class);
        User user = this.modelMapper.map(data, this.getThirdGenericClass());
        User userSaved = this.userRepository.save(user);
        return this.modelMapper.map(userSaved, this.getFirstGenericClass());
    }

	@Override
	public UserDto update(Long id, UserDto data) {
		return null;
	}

}
