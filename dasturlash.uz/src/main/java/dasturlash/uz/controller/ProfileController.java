package dasturlash.uz.controller;

import dasturlash.uz.dtos.profileDTOs.ProfileCreationDTO;
import dasturlash.uz.dtos.profileDTOs.ProfileResponseDTO;
import dasturlash.uz.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping
    public ResponseEntity<ProfileResponseDTO> addProfile(@RequestBody @Valid ProfileCreationDTO requestDTO) {
        return ResponseEntity.ok().build();
    }
}
