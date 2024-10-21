package dasturlash.uz.service;

import dasturlash.uz.dtos.regionDTOs.RegionResponseDTO;
import dasturlash.uz.dtos.regionDTOs.RegionRequestDTO;
import dasturlash.uz.entity.Region;
import dasturlash.uz.enums.LanguageEnum;
import dasturlash.uz.exceptions.DataExistsException;
import dasturlash.uz.exceptions.DataNotFoundException;
import dasturlash.uz.repository.customInterfaces.CustomMapperInterface;
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
    

    public RegionResponseDTO create(@Valid RegionRequestDTO creationDTO) {

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


    public void createRegions(List<RegionRequestDTO> regionRequestDTOs) {
        for (RegionRequestDTO regionRequestDTO : regionRequestDTOs) {
            Region region = modelMapper.map(regionRequestDTO, Region.class);
            region.setCreatedDate(LocalDateTime.now());
            region.setVisible(true);
            regionRepository.save(region);
        }
    }

    public RegionResponseDTO getRegionById(Long id) {
        
        Region region = getById(id);

      
        return modelMapper.map(region, RegionResponseDTO.class);
    }

    public RegionResponseDTO updateById(Long id, RegionRequestDTO requestDTO) {

        // check if it exists
        // fetch data
        Region existingRegion = getById(id);

        // check if the article type order number exist
        boolean orderNumberExists = regionRepository.existsByOrderNumber(requestDTO.getOrderNumber());
        if (!orderNumberExists) {
            throw new DataExistsException("Region with order number: " + requestDTO.getOrderNumber() + " exists");
        }

        // mapping
        modelMapper.map(requestDTO, existingRegion);

        // saving into database
        return modelMapper.map(regionRepository.save(existingRegion), RegionResponseDTO.class);
    }

    public Boolean deleteById(Long id) {

        Integer result = regionRepository.changeVisible(id);

        return result > 0;

    }

    public List<CustomMapperInterface> getVisibleRegionsByLanguageOrdered(LanguageEnum lang) {

        List<CustomMapperInterface> result = regionRepository.findAllVisibleByLanguageOrdered(lang.name());
        if (result.isEmpty()) {
            throw new DataNotFoundException("No data found");
        }
        return result;
    }

    public void existsByAnyName(String nameUz, String nameRu, String nameEn) {
        boolean isExist = regionRepository.existsByNameUzOrNameRuOrNameEn(nameUz, nameRu, nameEn);
        if (isExist) {
            throw new DataExistsException("Region with name: " + nameUz + " exists");
        }
    }

    public Region getById(Long id) {
        return regionRepository.findByIdAndVisibleTrue(id)
                .orElseThrow(() -> new DataNotFoundException("Region with id: " + id + " not found"));
    }
}
