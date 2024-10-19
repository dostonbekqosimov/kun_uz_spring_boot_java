package dasturlash.uz.controller;

import dasturlash.uz.dtos.categoryDTOS.CategoryRequestDTO;
import dasturlash.uz.dtos.categoryDTOS.CategoryResponseDTO;
import dasturlash.uz.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/category")
public class CategoryController {

    private final CategoryService service;

    // Add new Category
    @PostMapping({"", "/"})
    public ResponseEntity<?> create(@RequestBody @Valid CategoryRequestDTO creationDTO){

        return ResponseEntity.status(201).body(service.create(creationDTO));


    }

    // Get the list of article types
    @GetMapping({"", "/"})
    public ResponseEntity<PageImpl<CategoryResponseDTO>> getAll(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                @RequestParam(value = "size", defaultValue = "5") Integer size) {

        return ResponseEntity.ok().body(service.getAll(page - 1, size));

    }




}
