package dasturlash.uz.controller;

import dasturlash.uz.dtos.profile.*;
import dasturlash.uz.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addProfile(@RequestBody @Valid ProfileCreationDTO requestDTO) {
        return ResponseEntity.status(201).body(profileService.createProfile(requestDTO));
    }


    // Update Profile (ADMIN)
    @PutMapping("/{id}/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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

    // Get the list of profiles
    @GetMapping({"", "/"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PageImpl<ProfileResponseDTO>> getAll(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                               @RequestParam(value = "size", defaultValue = "5") Integer size) {

        return ResponseEntity.ok().body(profileService.getAll(page - 1, size));

    }

    // Update Own Profile (USER)
    @PutMapping("/{id}")
    public ResponseEntity<Boolean> updateOwnProfile(
            @RequestBody @Valid ProfileUpdateOwnDTO requestDTO) {

        return ResponseEntity.ok(profileService.updateProfileDetail(requestDTO));
    }

    // Update Profile Picture (USER)
    @PutMapping("/{id}/photo")
    public ResponseEntity<ProfileResponseDTO> updatePhoto(
            @PathVariable Long id,
            @RequestBody @Valid ProfilePhotoUpdateDTO requestDTO) {

        ProfileResponseDTO updatedProfile = profileService.updateProfilePhoto(id, requestDTO);
        return ResponseEntity.ok(updatedProfile);
    }

    // Delete by ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(profileService.deleteById(id));
    }

}
