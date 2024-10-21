package dasturlash.uz.controller;

import dasturlash.uz.dtos.articleTypeDTOs.ArticleTypeRequestDTO;
import dasturlash.uz.dtos.articleTypeDTOs.ArticleTypeResponseDTO;
import dasturlash.uz.enums.LanguageEnum;
import dasturlash.uz.repository.CustomMapperInterface;
import dasturlash.uz.service.ArticleTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/article-type")
@RequiredArgsConstructor
public class ArticleTypeController {

    private final ArticleTypeService articleTypeService;

    // Add new Article Type
    @PostMapping({"", "/"})
    public ResponseEntity<?> createArticleType(@RequestBody @Valid ArticleTypeRequestDTO creationDTO) {

        return ResponseEntity.status(201).body(articleTypeService.create(creationDTO));

    }

    // Get the list of article types
    @GetMapping({"", "/"})
    public ResponseEntity<PageImpl<ArticleTypeResponseDTO>> getAll(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                   @RequestParam(value = "size", defaultValue = "5") Integer size) {

        return ResponseEntity.ok().body(articleTypeService.getAll(page - 1, size));

    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {

        return ResponseEntity.ok().body(articleTypeService.getArticleTypeById(id));
    }


    // Update by ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable("id") Long id,
                                        @RequestBody @Valid ArticleTypeRequestDTO requestDTO) {

        return ResponseEntity.ok().body(articleTypeService.updateById(id, requestDTO));
    }

    // Delete by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(articleTypeService.deleteById(id));
    }

    // Get by lang
    @GetMapping("/lang")
    public ResponseEntity<List<CustomMapperInterface>> getVisibleArticleTypes(
            @RequestHeader(name = "Accept-Language", defaultValue = "uz") LanguageEnum lang) {
        List<CustomMapperInterface> articleTypes =
                articleTypeService.getVisibleArticleTypesByLanguageOrdered(lang);
        return ResponseEntity.ok(articleTypes);
    }


}
