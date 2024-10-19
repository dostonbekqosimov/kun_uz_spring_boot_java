package dasturlash.uz.controller;

import dasturlash.uz.dtos.ArticleTypeCreationDTO;
import dasturlash.uz.dtos.ArticleTypeResponseDTO;
import dasturlash.uz.service.ArticleTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/article-type")
@RequiredArgsConstructor
public class ArticleTypeController {

    private final ArticleTypeService articleTypeService;

    // Add new Article Type
    @PostMapping({"", "/"})
    public ResponseEntity<?> createArticleType(@RequestBody @Valid ArticleTypeCreationDTO creationDTO) {

        return ResponseEntity.status(201).body(articleTypeService.create(creationDTO));

    }

    // Get the list of article types
    @GetMapping({"", "/"})
    public ResponseEntity<PageImpl<ArticleTypeResponseDTO>> getAll(@RequestParam("page") Integer page,
                                                                   @RequestParam("size") Integer size) {

        return ResponseEntity.ok().body(articleTypeService.getAll(page - 1, size));

    }
}
