package com.revilla.homestuff.service.imp;

import java.util.List;
import java.util.stream.Collectors;
import com.revilla.homestuff.dto.NourishmentDto;
import com.revilla.homestuff.entity.Nourishment;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.repository.NourishmentRepository;
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
@RequiredArgsConstructor
@Service
@Qualifier("nourishment.service")
public class NourishmentService implements GeneralService<NourishmentDto, Long> {

    private final NourishmentRepository nourishmentRepository;
    private final ModelMapper modelmapper;

	@Override
	public List<NourishmentDto> findAll(Pageable pageable) {
        log.info("Calling the findAll methond in NourishmentService");
        return this.nourishmentRepository.findAll(pageable)
            .getContent()
            .stream()
            .map(n -> this.modelmapper.map(n, NourishmentDto.class))
            .collect(Collectors.toList());
	}

	@Override
	public NourishmentDto findOne(Long id) {
        log.info("Calling the findOne methond in NourishmentService");
        Nourishment nourishment = this.nourishmentRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("Nourishment don't found"));
        return this.modelmapper.map(nourishment, NourishmentDto.class);
	}

	@Override
	public NourishmentDto create(NourishmentDto data) {
        log.info("Calling the create methond in NourishmentService");
        Nourishment userData = this.modelmapper.map(data, Nourishment.class);
        Nourishment userSaved = this.nourishmentRepository.save(userData);
        return this.modelmapper.map(userSaved, NourishmentDto.class);
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
                return this.modelmapper.map(this.nourishmentRepository.save(n), NourishmentDto.class);
            })
            .orElseThrow(() -> new IllegalStateException("User don't found"));
	}

	@Override
	public NourishmentDto delete(Long id) {
        log.info("Calling the delete methond in NourishmentService");
        return this.nourishmentRepository.findById(id)
            .map(u -> {
                this.nourishmentRepository.delete(u);
                return this.modelmapper.map(u, NourishmentDto.class);
            })
            .orElseThrow(() -> new IllegalStateException("User don't found"));
	}

}
