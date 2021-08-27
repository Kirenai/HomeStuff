package com.revilla.homestuff.service.imp;

import javax.transaction.Transactional;
import com.revilla.homestuff.dto.NourishmentDto;
import com.revilla.homestuff.entity.Category;
import com.revilla.homestuff.entity.Nourishment;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.repository.CategoryRepository;
import com.revilla.homestuff.repository.NourishmentRepository;
import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.service.NourishmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * NourishmentService
 * @author Kirenai
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Qualifier("nourishment.service")
public class NourishmentServiceImp extends GeneralServiceImp<NourishmentDto, Long, Nourishment>
    implements NourishmentService {

    private final NourishmentRepository nourishmentRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
	public JpaRepository<Nourishment, Long> getRepo() {
		return this.nourishmentRepository;
	}

    @Transactional
    @Override
    public NourishmentDto create(
            Long userId,
            Long categoryId,
            NourishmentDto data) {
        log.info("Calling the create methond in " + getClass());
        Nourishment nourishment = this.modelMapper.map(data, this.getThirdGenericClass());
        User user = this.userRepository.findById(userId)
            .orElseThrow(() -> new IllegalStateException("User don't found"));
        Category category = this.categoryRepository.findById(categoryId)
            .orElseThrow(() -> new IllegalStateException("Category don't found"));
        nourishment.setUser(user);
        nourishment.setCategory(category);
        Nourishment nourishmentSaved = this.nourishmentRepository.save(nourishment);
        return this.modelMapper.map(nourishmentSaved, this.getFirstGenericClass());
    }

	@Override
	public NourishmentDto update(Long id, NourishmentDto data) {
        log.info("Calling the update methond in NourishmentService");
        return this.nourishmentRepository.findById(id)
            .map(n -> {
                n.setName(data.getName());
                n.setImagePath(data.getImagePath());
                n.setDescription(data.getDescription());
                n.setIsAvailable(data.getIsAvailable());
                return this.modelMapper.map(this.nourishmentRepository.save(n), NourishmentDto.class);
            })
            .orElseThrow(() -> new IllegalStateException("User don't found"));
	}


}
