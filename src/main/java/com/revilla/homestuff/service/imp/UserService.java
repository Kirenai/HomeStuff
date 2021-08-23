package com.revilla.homestuff.service.imp;

import java.util.List;
import java.util.stream.Collectors;
import com.revilla.homestuff.dto.UserDto;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.service.GeneralService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * NourishmentService
 * @author Kirenai
 */
@Slf4j
@Service
@Qualifier("user.service")
@RequiredArgsConstructor
public class UserService implements GeneralService<UserDto, Long> {

    private final UserRepository userRepository;
    private final ModelMapper modelmapper;

    @Override
    public List<UserDto> findAll(Pageable pageable) {
        log.info("Calling the findAll methond in UserService");
        return this.userRepository.findAll(pageable)
            .getContent()
            .stream()
            .map(u -> this.modelmapper.map(u, UserDto.class))
            .collect(Collectors.toList());
    }

    @Override
    public UserDto findOne(Long id) {
        log.info("Calling the findOne methond in UserService");
        User user = this.userRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("User don't found"));
        return this.modelmapper.map(user, UserDto.class);
    }

    @Override
    public UserDto create(UserDto data) {
        log.info("Calling the create methond in UserService");
        User userData = this.modelmapper.map(data, User.class);
        User userSaved = this.userRepository.save(userData);
        return this.modelmapper.map(userSaved, UserDto.class);
    }

    @Override
    public UserDto update(Long id, UserDto data) {
        log.info("Calling the update methond in UserService");
        return this.userRepository.findById(id)
            .map(u -> {
                u.setFirstName(data.getFirstName());
                u.setLastName(data.getLastName());
                u.setPassword(data.getPassword());
                u.setAge(data.getAge());
                return this.modelmapper.map(this.userRepository.save(u), UserDto.class);
            })
            .orElseThrow(() -> new IllegalStateException("User don't found"));
    }

    @Override
    public UserDto delete(Long id) {
        log.info("Calling the delete methond in UserService");
        return this.userRepository.findById(id)
            .map(u -> {
                this.userRepository.delete(u);
                return this.modelmapper.map(u, UserDto.class);
            })
            .orElseThrow(() -> new IllegalStateException("User don't found"));
    }
}
