package dasturlash.uz.controller;

import dasturlash.uz.dtos.regionDTOs.RegionRequestDTO;
import dasturlash.uz.dtos.regionDTOs.RegionResponseDTO;
import dasturlash.uz.service.RegionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/region")
public class RegionController {

    private final RegionService regionService;

    // Add new Article Type
    @PostMapping({"", "/"})
    public ResponseEntity<?> createArticleType(@RequestBody @Valid RegionRequestDTO creationDTO) {

        return ResponseEntity.status(201).body(regionService.create(creationDTO));

    }


    // Get the list of article types
    @GetMapping({"", "/"})
    public ResponseEntity<PageImpl<RegionResponseDTO>> getAll(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                              @RequestParam(value = "size", defaultValue = "5") Integer size) {

        return ResponseEntity.ok().body(regionService.getAll(page - 1, size));

    }


    @PostMapping("/bulk")
    public ResponseEntity<String> createRegions(@RequestBody List<RegionRequestDTO> regionRequestDTOs) {
        regionService.createRegions(regionRequestDTOs);
        return ResponseEntity.ok("Regions created successfully");
    }
}
