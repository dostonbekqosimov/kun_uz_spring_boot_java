package dasturlash.uz.controller;

import dasturlash.uz.dtos.profileDTOs.ProfileCreationDTO;
import dasturlash.uz.dtos.profileDTOs.ProfileResponseDTO;
import dasturlash.uz.dtos.profileDTOs.ProfileUpdateDTO;
import dasturlash.uz.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping
    public ResponseEntity<ProfileResponseDTO> addProfile(@RequestBody @Valid ProfileCreationDTO requestDTO) {
        return ResponseEntity.status(201).body(profileService.createProfile(requestDTO));
    }


    // Update Profile (ADMIN)
    @PutMapping("/{id}/admin")
    public ResponseEntity<ProfileResponseDTO> updateProfileByAdmin(
            @PathVariable Long id,
            @RequestBody @Valid ProfileUpdateDTO requestDTO) {

        ProfileResponseDTO updatedProfile = profileService.updateProfileByAdmin(id, requestDTO);
        return ResponseEntity.ok(updatedProfile);
    }

    // Get By ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getProfileById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(profileService.getProfileById(id));
    }

    // Update Own Profile (USER)
    @PutMapping("/{id}")
    public ResponseEntity<ProfileResponseDTO> updateOwnProfile(
            @PathVariable Long id,
            @RequestBody @Valid ProfileUpdateDTO requestDTO) {

        ProfileResponseDTO updatedProfile = profileService.updateProfile(id, requestDTO);
        return ResponseEntity.ok(updatedProfile);
    }

}
