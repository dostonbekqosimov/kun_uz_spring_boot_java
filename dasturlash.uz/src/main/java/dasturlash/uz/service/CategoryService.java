package dasturlash.uz.service;

import dasturlash.uz.dtos.CategoryResponseDTO;
import dasturlash.uz.dtos.CategoryCreationDTO;
import dasturlash.uz.dtos.CategoryResponseDTO;
import dasturlash.uz.entity.ArticleType;
import dasturlash.uz.entity.Category;
import dasturlash.uz.exceptions.DataExistsException;
import dasturlash.uz.exceptions.DataNotFoundException;
import dasturlash.uz.repository.ArticleTypeRepository;
import dasturlash.uz.repository.CategoryRepository;
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
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;


    public CategoryResponseDTO create(@Valid CategoryCreationDTO creationDTO) {

        // check if the category type exists
        existsByAnyName(creationDTO.getNameUz(), creationDTO.getNameRu(), creationDTO.getNameEn());

        Category newCategory = new Category();
        newCategory.setOrderNumber(creationDTO.getOrderNumber());
        newCategory.setNameUz(creationDTO.getNameUz());
        newCategory.setNameEn(creationDTO.getNameEn());
        newCategory.setNameRu(creationDTO.getNameRu());
        newCategory.setVisible(true);
        newCategory.setCreatedDate(LocalDateTime.now());

        categoryRepository.save(newCategory);

        return modelMapper.map(newCategory, CategoryResponseDTO.class);
        
    }

    public PageImpl<CategoryResponseDTO> getAll(Integer page, Integer size) {

        Pageable pageRequest = PageRequest.of(page, size);

        Page<Category> articleTypePage = categoryRepository.findAll(pageRequest);

        if (articleTypePage.isEmpty()) {
            throw new DataNotFoundException("No category found");
        }

        // Convert to DTOs
        List<CategoryResponseDTO> responseDTOS = articleTypePage.getContent().stream()
                .map(articleType -> modelMapper.map(articleType, CategoryResponseDTO.class))
                .collect(Collectors.toList());

        // Create a new Page with the DTOs
        return new PageImpl<>(responseDTOS, pageRequest, articleTypePage.getTotalElements());
    }

    public void existsByAnyName(String nameUz, String nameRu, String nameEn) {
        boolean isExist = categoryRepository.existsByNameUzOrNameRuOrNameEn(nameUz, nameRu, nameEn);
        if (isExist) {
            throw new DataExistsException("Category with name: " + nameUz + " exists");
        }
    }
}
