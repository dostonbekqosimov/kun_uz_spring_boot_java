package dasturlash.uz.controller;

import dasturlash.uz.dtos.categoryDTOS.CategoryCreationDTO;
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
    public ResponseEntity<?> create(@RequestBody @Valid CategoryCreationDTO creationDTO){

        return ResponseEntity.status(201).body(service.create(creationDTO));


    }

    // Get the list of article types
    @GetMapping({"", "/"})
    public ResponseEntity<PageImpl<CategoryResponseDTO>> getAll(@RequestParam("page") Integer page,
                                                                @RequestParam("size") Integer size) {

        return ResponseEntity.ok().body(service.getAll(page - 1, size));

    }




}
