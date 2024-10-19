package dasturlash.uz.service;

import dasturlash.uz.dtos.articleTypeDTOs.ArticleTypeRequestDTO;
import dasturlash.uz.dtos.articleTypeDTOs.ArticleTypeResponseDTO;
import dasturlash.uz.entity.ArticleType;
import dasturlash.uz.exceptions.DataExistsException;
import dasturlash.uz.exceptions.DataNotFoundException;
import dasturlash.uz.repository.ArticleTypeRepository;
import org.modelmapper.ModelMapper;

import lombok.RequiredArgsConstructor;
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
public class ArticleTypeService {

    private final ArticleTypeRepository articleTypeRepository;
    private final ModelMapper modelMapper;


    public ArticleTypeResponseDTO create(ArticleTypeRequestDTO creationDTO) {

        // check if the article type exists
        existsByAnyName(creationDTO.getNameUz(), creationDTO.getNameRu(), creationDTO.getNameEn());

        ArticleType newArticleType = new ArticleType();
        newArticleType.setOrderNumber(creationDTO.getOrderNumber());
        newArticleType.setNameUz(creationDTO.getNameUz());
        newArticleType.setNameEn(creationDTO.getNameEn());
        newArticleType.setNameRu(creationDTO.getNameRu());
        newArticleType.setVisible(true);
        newArticleType.setCreatedDate(LocalDateTime.now());

        articleTypeRepository.save(newArticleType);

        return modelMapper.map(newArticleType, ArticleTypeResponseDTO.class);
    }

    public PageImpl<ArticleTypeResponseDTO> getAll(Integer page, Integer size) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<ArticleType> articleTypePage = articleTypeRepository.findAll(pageRequest);

        if (articleTypePage.isEmpty()) {
            throw new DataNotFoundException("No article types found");
        }

        // Convert to DTOs
        List<ArticleTypeResponseDTO> responseDTOS = articleTypePage.getContent().stream()
                .map(articleType -> modelMapper.map(articleType, ArticleTypeResponseDTO.class))
                .collect(Collectors.toList());

        // Create a new Page with the DTOs
        return new PageImpl<>(responseDTOS, pageRequest, articleTypePage.getTotalElements());
    }

    public ArticleTypeResponseDTO updateById(Long id, ArticleTypeRequestDTO requestDTO) {

        // check if it exists
        isExist(id);

        // fetch data
        ArticleType existingArticleType = articleTypeRepository.findById(id).get();


        // check if any fields exist


        // mapping
        modelMapper.map(requestDTO, existingArticleType);

        // saving into database
       return modelMapper.map(articleTypeRepository.save(existingArticleType), ArticleTypeResponseDTO.class);



    }

    public void existsByAnyName(String nameUz, String nameRu, String nameEn) {
        boolean isExist = articleTypeRepository.existsByNameUzOrNameRuOrNameEn(nameUz, nameRu, nameEn);
        if (isExist) {
            throw new DataExistsException("Article Type with name: " + nameUz + " exists");
        }
    }

    public void isExist(Long id) {
        boolean isExist = articleTypeRepository.existsById(id);
        if (!isExist) {
            throw new DataNotFoundException("Article type with id: " + id + " not found");
        }
    }


}
