package dasturlash.uz.service;

import dasturlash.uz.dtos.articleTypeDTOs.ArticleTypeRequestDTO;
import dasturlash.uz.dtos.articleTypeDTOs.ArticleTypeResponseDTO;
import dasturlash.uz.entity.ArticleType;
import dasturlash.uz.enums.LanguageEnum;
import dasturlash.uz.exceptions.DataExistsException;
import dasturlash.uz.exceptions.DataNotFoundException;
import dasturlash.uz.repository.customInterfaces.CustomMapperInterface;
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

        // check if the article type order number exist
        boolean orderNumberExists = articleTypeRepository.existsByOrderNumber(creationDTO.getOrderNumber());
        if (!orderNumberExists) {
            throw new DataExistsException("Article Type with order number: " + creationDTO.getOrderNumber() + " exists");
        }


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
        Page<ArticleType> articleTypePage = articleTypeRepository.findAllByVisibleTrue(pageRequest);

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
        // fetch data
        ArticleType existingArticleType = getById(id);

        // check if the article type order number exist
        boolean orderNumberExists = articleTypeRepository.existsByOrderNumber(requestDTO.getOrderNumber());
        if (!orderNumberExists) {
            throw new DataExistsException("Article Type with order number: " + requestDTO.getOrderNumber() + " exists");
        }

        // mapping
        modelMapper.map(requestDTO, existingArticleType);

        // saving into database
        return modelMapper.map(articleTypeRepository.save(existingArticleType), ArticleTypeResponseDTO.class);

    }

    public Boolean deleteById(Long id) {

        Integer result = articleTypeRepository.changeVisible(id);

        return result > 0;

    }

    public ArticleTypeResponseDTO getArticleTypeById(Long id) {

        ArticleType articleType = getById(id);

        if (!articleType.getVisible()) {
            throw new DataNotFoundException("No data found with id: " + id);
        }
        return modelMapper.map(articleType, ArticleTypeResponseDTO.class);
    }
    // old thing you should check this at home
//    public List<ArticleTypeResponseDTO> getByLang(LanguageEnum lang) {
//
////        List<ArticleType> articleTypeList = articleTypeRepository.findAll();
////        List<ArticleTypeResponseDTO> resultList = new ArrayList<>();
////
////        for (ArticleType articleType : articleTypeList) {
////            ArticleTypeResponseDTO dto = new ArticleTypeResponseDTO();
////            dto.setId(articleType.getId());
////            dto.setOrderNumber(articleType.getOrderNumber());
////
////            // Set the name based on the provided language
////            String name = switch (lang) {
////                case uz -> articleType.getNameUz();
////                case ru -> articleType.getNameRu();
////                case en -> articleType.getNameEn();
////                default -> throw new IllegalArgumentException("Unsupported language: " + lang);
////            };
////
////            // Only add to result list if the name is not null or empty
////            if (name != null && !name.isEmpty()) {
////                dto.setName(name);
////                resultList.add(dto);
////            }
////        }
////
////        return resultList;
//
//        return articleTypeRepository.findAllVisibleByLanguageOrdered(lang)
//                .stream()
//                .map(articleType -> modelMapper.map(articleType, ArticleTypeResponseDTO.class)).toList();
//
//    }

    public List<CustomMapperInterface> getVisibleArticleTypesByLanguageOrdered(LanguageEnum lang) {
        List<CustomMapperInterface> result = articleTypeRepository.findAllVisibleByLanguageOrdered(lang.name());
        if (result.isEmpty()) {
            throw new DataNotFoundException("No data found");
        }
        return result;
    }

    public void existsByAnyName(String nameUz, String nameRu, String nameEn) {
        boolean isExist = articleTypeRepository.existsByNameUzOrNameRuOrNameEn(nameUz, nameRu, nameEn);
        if (isExist) {
            throw new DataExistsException("Article Type with name: " + nameUz + " exists");
        }
    }


    public ArticleType getById(Long id) {
        return articleTypeRepository.findByIdAndVisibleTrue(id)
                .orElseThrow(() -> new DataNotFoundException("Article type with id: " + id + " not found"));
    }


}
