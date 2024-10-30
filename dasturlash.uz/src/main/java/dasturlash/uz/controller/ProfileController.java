package dasturlash.uz.controller;

import dasturlash.uz.dtos.JwtDTO;
import dasturlash.uz.dtos.articleTypeDTOs.ArticleTypeResponseDTO;
import dasturlash.uz.dtos.profileDTOs.*;
import dasturlash.uz.enums.Role;
import dasturlash.uz.exceptions.ForbiddenException;
import dasturlash.uz.service.ProfileService;
import dasturlash.uz.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping(value = "/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping
    public ResponseEntity<?> addProfile(@RequestBody @Valid ProfileCreationDTO requestDTO,
                                        @RequestHeader("Authorization") String token) {


//        JwtDTO parsedToken = JwtUtil.decode(token);
//        if (!parsedToken.getRole().equals(Role.ADMIN.toString())) {
//            throw new ForbiddenException("You don't have fucking rights to do this bitch!");
//        }
        return ResponseEntity.status(201).body(profileService.createProfile(requestDTO));
    }


    // Update Profile (ADMIN)
    @PutMapping("/{id}/admin")
    public ResponseEntity<ProfileResponseDTO> updateProfileByAdmin(
            @PathVariable Long id,
            @RequestBody @Valid ProfileUpdateDTO requestDTO,
            @RequestHeader("Authorization") String token) {

        JwtDTO parsedToken = JwtUtil.decode(token);
        if (!parsedToken.getRole().equals(Role.ADMIN.toString())) {
            throw new ForbiddenException("You don't have fucking rights to do this bitch!");
        }

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
    public ResponseEntity<PageImpl<ProfileResponseDTO>> getAll(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                               @RequestParam(value = "size", defaultValue = "5") Integer size) {

        return ResponseEntity.ok().body(profileService.getAll(page - 1, size));

    }

    // Update Own Profile (USER)
    @PutMapping("/{id}")
    public ResponseEntity<Boolean> updateOwnProfile(
            @PathVariable Long id,
            @RequestBody @Valid ProfileUpdateOwnDTO requestDTO,
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(profileService.updateProfileDetail(requestDTO, token));
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
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(profileService.deleteById(id));
    }

}
