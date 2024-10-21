package dasturlash.uz.service;

import dasturlash.uz.dtos.profileDTOs.ProfileCreationDTO;
import dasturlash.uz.dtos.profileDTOs.ProfileResponseDTO;
import dasturlash.uz.dtos.profileDTOs.ProfileUpdateDTO;
import dasturlash.uz.entity.Category;
import dasturlash.uz.entity.Profile;
import dasturlash.uz.enums.Role;
import dasturlash.uz.enums.Status;
import dasturlash.uz.exceptions.DataExistsException;
import dasturlash.uz.exceptions.DataNotFoundException;
import dasturlash.uz.repository.ProfileRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ModelMapper modelMapper;


    public ProfileResponseDTO createProfile(ProfileCreationDTO requestDTO) {


        // check if profile name or phone exists
        existsByEmailOrPhone(requestDTO.getEmail(), requestDTO.getPhone());

        Profile newProfile = new Profile();
        modelMapper.map(requestDTO, newProfile);
        newProfile.setStatus(Status.ACTIVE);
        newProfile.setVisible(Boolean.TRUE);
        newProfile.setCreatedAt(LocalDateTime.now());

        if (requestDTO.getRole() == null) {
            newProfile.setRole(Role.USER);
        }

        // saving into database
        profileRepository.save(newProfile);

        // map
        return modelMapper.map(newProfile, ProfileResponseDTO.class);
    }

    public ProfileResponseDTO updateProfileByAdmin(Long id, ProfileUpdateDTO requestDTO) {

        // getting data from db and checking
        Profile existingProfile = getById(id);

        // check if profile name or phone exists
        existsByEmailOrPhone(requestDTO.getEmail(), requestDTO.getPhone());

        modelMapper.map(requestDTO, existingProfile);
        if (existingProfile.getEmail() == null) {
            existingProfile.setEmail(requestDTO.getEmail());
        }
        if (existingProfile.getPhone() == null) {
            existingProfile.setPhone(requestDTO.getPhone());
        }

        Profile updatedProfile = profileRepository.save(existingProfile);

        return convertToProfileResponseDTO(updatedProfile);


    }

    public ProfileResponseDTO updateProfile(Long id, ProfileUpdateDTO requestDTO) {
        return null;
    }

    private void existsByEmailOrPhone(String email, String phone) {

        if (email != null && !email.trim().isEmpty()) {
            boolean isEmailExist = profileRepository.existsByEmail(email);
            if (isEmailExist) {
                throw new DataExistsException("Profile with email: " + email + " already exists");
            }
        }

        if (phone != null && !phone.trim().isEmpty()) {
            boolean isPhoneExist = profileRepository.existsByPhone(phone);
            if (isPhoneExist) {
                throw new DataExistsException("Profile with phone: " + phone + " already exists");
            }
        }
    }

    public Profile getById(Long id) {
        return profileRepository.findByIdAndVisibleTrue(id)
                .orElseThrow(() -> new DataNotFoundException("Profile with id: " + id + " not found"));
    }

    private ProfileResponseDTO convertToProfileResponseDTO(Profile profile) {
        return modelMapper.map(profile, ProfileResponseDTO.class);
    }


}
