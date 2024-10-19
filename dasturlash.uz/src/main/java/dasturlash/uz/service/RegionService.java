package dasturlash.uz.service;

import dasturlash.uz.dtos.CategoryCreationDTO;
import dasturlash.uz.dtos.RegionResponseDTO;
import dasturlash.uz.dtos.RegionCreationDTO;
import dasturlash.uz.dtos.RegionResponseDTO;
import dasturlash.uz.entity.Category;
import dasturlash.uz.entity.Region;
import dasturlash.uz.exceptions.DataExistsException;
import dasturlash.uz.exceptions.DataNotFoundException;
import dasturlash.uz.repository.RegionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegionService {
    
    private final RegionRepository regionRepository;
    private final ModelMapper modelMapper;
    

    public RegionResponseDTO create(@Valid RegionCreationDTO creationDTO) {

        // check if the region type exists
        existsByAnyName(creationDTO.getNameUz(), creationDTO.getNameRu(), creationDTO.getNameEn());

        Region newRegion = new Region();
        newRegion.setOrderNumber(creationDTO.getOrderNumber());
        newRegion.setNameUz(creationDTO.getNameUz());
        newRegion.setNameEn(creationDTO.getNameEn());
        newRegion.setNameRu(creationDTO.getNameRu());
        newRegion.setVisible(true);
        newRegion.setCreatedDate(LocalDateTime.now());

        regionRepository.save(newRegion);

        return modelMapper.map(newRegion, RegionResponseDTO.class);



    }


    public PageImpl<RegionResponseDTO> getAll(Integer page, Integer size) {

        Pageable pageRequest = PageRequest.of(page, size);

        Page<Region> articleTypePage = regionRepository.findAll(pageRequest);

        if (articleTypePage.isEmpty()) {
            throw new DataNotFoundException("No category found");
        }

        // Convert to DTOs
        List<RegionResponseDTO> responseDTOS = articleTypePage.getContent().stream()
                .map(articleType -> modelMapper.map(articleType, RegionResponseDTO.class))
                .collect(Collectors.toList());

        // Create a new Page with the DTOs
        return new PageImpl<>(responseDTOS, pageRequest, articleTypePage.getTotalElements());
    }


    public void createRegions(List<RegionCreationDTO> regionRequestDTOs) {
        for (RegionCreationDTO regionRequestDTO : regionRequestDTOs) {
            Region region = modelMapper.map(regionRequestDTO, Region.class);
            region.setCreatedDate(LocalDateTime.now());
            region.setVisible(true);
            regionRepository.save(region);
        }
    }

    public void existsByAnyName(String nameUz, String nameRu, String nameEn) {
        boolean isExist = regionRepository.existsByNameUzOrNameRuOrNameEn(nameUz, nameRu, nameEn);
        if (isExist) {
            throw new DataExistsException("Region with name: " + nameUz + " exists");
        }
    }
}
