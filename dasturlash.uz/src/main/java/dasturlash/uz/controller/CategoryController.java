package dasturlash.uz.controller;

import dasturlash.uz.dtos.categoryDTOS.CategoryRequestDTO;
import dasturlash.uz.dtos.categoryDTOS.CategoryResponseDTO;
import dasturlash.uz.enums.LanguageEnum;
import dasturlash.uz.repository.customInterfaces.CustomMapperInterface;
import dasturlash.uz.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/category")
public class CategoryController {

    private final CategoryService service;

    // Add new Category
    @PostMapping({"", "/"})
    public ResponseEntity<?> create(@RequestBody @Valid CategoryRequestDTO creationDTO) {

        return ResponseEntity.status(201).body(service.create(creationDTO));


    }

    // Get the list of article types
    @GetMapping({"", "/"})
    public ResponseEntity<PageImpl<CategoryResponseDTO>> getAll(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                @RequestParam(value = "size", defaultValue = "5") Integer size) {

        return ResponseEntity.ok().body(service.getAll(page - 1, size));

    }


    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {

        return ResponseEntity.ok().body(service.getCategoryById(id));
    }

    // Update by ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable("id") Long id,
                                        @RequestBody @Valid CategoryRequestDTO requestDTO) {

        return ResponseEntity.ok().body(service.updateById(id, requestDTO));
    }

    // Delete by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(service.deleteById(id));
    }

    // Get by lang
    @GetMapping("/lang")
    public ResponseEntity<List<CustomMapperInterface>> getVisibleArticleTypes(
            @RequestHeader(name = "Accept-Language", defaultValue = "uz") LanguageEnum lang) {
        List<CustomMapperInterface> articleTypes =
                service.getVisibleCategoriesByLanguageOrdered(lang);
        return ResponseEntity.ok(articleTypes);
    }


}
