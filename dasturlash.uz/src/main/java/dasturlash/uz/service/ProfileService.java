package dasturlash.uz.service;

import dasturlash.uz.dtos.JwtDTO;
import dasturlash.uz.dtos.profileDTOs.*;
import dasturlash.uz.entity.Profile;
import dasturlash.uz.enums.Role;
import dasturlash.uz.enums.Status;
import dasturlash.uz.exceptions.DataExistsException;
import dasturlash.uz.exceptions.DataNotFoundException;
import dasturlash.uz.exceptions.ForbiddenException;
import dasturlash.uz.repository.ProfileRepository;
import dasturlash.uz.util.JwtUtil;
import dasturlash.uz.util.MD5Util;
import dasturlash.uz.util.SpringSecurityUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public ProfileResponseDTO createProfile(ProfileCreationDTO requestDTO) {


        // check if profile with name or phone exists
        existsByEmailOrPhone(requestDTO.getEmail(), requestDTO.getPhone());

        Profile newProfile = new Profile();
        modelMapper.map(requestDTO, newProfile);
        newProfile.setPassword(bCryptPasswordEncoder.encode(requestDTO.getPassword()));
        newProfile.setStatus(Status.ACTIVE);
        newProfile.setVisible(Boolean.TRUE);
        newProfile.setCreatedAt(LocalDateTime.now());


        // saving into database
        profileRepository.save(newProfile);

        // map
        return modelMapper.map(newProfile, ProfileResponseDTO.class);
    }

    public ProfileResponseDTO updateProfileByAdmin(Long id, ProfileUpdateDTO requestDTO) {

        // Getting and checking data
        Profile existingProfile = getById(id);

        // check if profile with name or phone exists
        existsByEmailOrPhone(requestDTO.getEmail(), requestDTO.getPhone());

        Profile updatedProfile = covertToProfile(requestDTO, existingProfile);

        // update profile
        profileRepository.save(updatedProfile);


        return convertToProfileResponseDTO(updatedProfile);


    }

    public boolean updateProfileDetail(ProfileUpdateOwnDTO requestDTO) {

        Profile profile = getById(SpringSecurityUtil.getUserId());
        profile.setName(requestDTO.getName());
        profile.setSurname(requestDTO.getSurname());
        profileRepository.save(profile);

        return true;
    }

    public ProfileResponseDTO getProfileById(Long id) {

        // getting data from db and checking
        Profile profile = getById(id);


        return convertToProfileResponseDTO(profile);
    }

    public PageImpl<ProfileResponseDTO> getAll(Integer page, Integer size) {

        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Profile> profiles = profileRepository.findAllByVisibleTrue(pageRequest);

        if (profiles.isEmpty()) {
            throw new DataNotFoundException("No profile found");
        }

        // Convert to DTOs
        List<ProfileResponseDTO> responseDTOS = profiles.getContent().stream()
                .map(profile -> modelMapper.map(profile, ProfileResponseDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<>(responseDTOS, pageRequest, profiles.getTotalElements());


    }

    public ProfileResponseDTO updateProfilePhoto(Long id, ProfilePhotoUpdateDTO requestDTO) {

        return null;
    }

    public Boolean deleteById(Long id) {

        Integer result = profileRepository.changeVisible(id);

        return result > 0;

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

    public Profile getByLogin(String login) {
        return profileRepository.findByEmailAndVisibleTrue(login).orElseThrow(() -> new DataNotFoundException("Not Found"));
    }

    private Profile covertToProfile(ProfileUpdateDTO updateDTO, Profile existingProfile) {


        existingProfile.setName(updateDTO.getName());
        existingProfile.setSurname(updateDTO.getSurname());
        // Only update email if provided, otherwise keep existing email
        if (updateDTO.getEmail() != null && !updateDTO.getEmail().trim().isEmpty()) {
            existingProfile.setEmail(updateDTO.getEmail());
        }

        // Only update phone if provided, otherwise keep existing phone
        if (updateDTO.getPhone() != null && !updateDTO.getPhone().trim().isEmpty()) {
            existingProfile.setPhone(updateDTO.getPhone());
        }

        // Update role if not provided
        if (updateDTO.getRole() != null) {
            existingProfile.setRole(updateDTO.getRole());

        }

        // Update status if provided
        if (updateDTO.getStatus() != null) {
            existingProfile.setStatus(updateDTO.getStatus());
        }

        return existingProfile;


    }


}
