package dasturlash.uz.controller;

import dasturlash.uz.dtos.regionDTOs.RegionRequestDTO;
import dasturlash.uz.dtos.regionDTOs.RegionResponseDTO;
import dasturlash.uz.enums.LanguageEnum;
import dasturlash.uz.repository.customInterfaces.CustomMapperInterface;
import dasturlash.uz.service.RegionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/region")
public class RegionController {

    private final RegionService regionService;

    // Add new Article Type
    @PostMapping({"", "/"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createArticleType(@RequestBody @Valid RegionRequestDTO creationDTO) {

        return ResponseEntity.status(201).body(regionService.create(creationDTO));

    }


    // Get the list of article types
    @GetMapping({"", "/"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PageImpl<RegionResponseDTO>> getAll(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                              @RequestParam(value = "size", defaultValue = "5") Integer size) {

        return ResponseEntity.ok().body(regionService.getAll(page - 1, size));

    }


    @PostMapping("/bulk")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createRegions(@RequestBody List<RegionRequestDTO> regionRequestDTOs) {
        regionService.createRegions(regionRequestDTOs);
        return ResponseEntity.ok("Regions created successfully");
    }

    // Get by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {

        return ResponseEntity.ok().body(regionService.getRegionById(id));
    }

    // Update by ID
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateById(@PathVariable("id") Long id,
                                        @RequestBody @Valid RegionRequestDTO requestDTO) {

        return ResponseEntity.ok().body(regionService.updateById(id, requestDTO));
    }

    // Delete by ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(regionService.deleteById(id));
    }

    // Get by lang
    @GetMapping("/lang")
    public ResponseEntity<List<CustomMapperInterface>> getVisibleRegionsByLang(
            @RequestHeader(name = "Accept-Language", defaultValue = "uz") LanguageEnum lang) {
        List<CustomMapperInterface> articleTypes =
                regionService.getVisibleRegionsByLanguageOrdered(lang);
        return ResponseEntity.ok(articleTypes);
    }
}
