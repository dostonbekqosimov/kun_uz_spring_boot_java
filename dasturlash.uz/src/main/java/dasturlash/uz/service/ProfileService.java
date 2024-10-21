package dasturlash.uz.service;

import dasturlash.uz.dtos.profileDTOs.ProfileCreationDTO;
import dasturlash.uz.dtos.profileDTOs.ProfileResponseDTO;
import dasturlash.uz.entity.Profile;
import dasturlash.uz.enums.Role;
import dasturlash.uz.enums.Status;
import dasturlash.uz.exceptions.DataExistsException;
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


    public ProfileResponseDTO createProfile(@Valid ProfileCreationDTO requestDTO) {


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

        // Convert the newly created Profile to ProfileResponseDTO

        //
        return modelMapper.map(newProfile, ProfileResponseDTO.class);
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
}
