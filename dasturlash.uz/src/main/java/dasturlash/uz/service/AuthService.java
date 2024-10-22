package dasturlash.uz.service;


import dasturlash.uz.dtos.profileDTOs.MessageDTO;
import dasturlash.uz.dtos.profileDTOs.RegistrationDTO;
import dasturlash.uz.entity.Profile;
import dasturlash.uz.enums.Role;
import dasturlash.uz.enums.Status;
import dasturlash.uz.exceptions.DataExistsException;
import dasturlash.uz.repository.ProfileRepository;
import dasturlash.uz.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private EmailSendingService emailSendingService;

    public String registration(RegistrationDTO dto) {
        // check email exists
//        existsByEmailOrPhone(dto.getEmail(), dto.getPhone());

        Profile entity = new Profile();
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPassword(MD5Util.getMd5(dto.getPassword()));
        entity.setSurname(dto.getSurname());
        entity.setStatus(Status.IN_REGISTRATION);
        entity.setRole(Role.USER);
        entity.setVisible(Boolean.TRUE);
        entity.setCreatedAt(LocalDateTime.now());
        profileRepository.save(entity);

        // email sending

        StringBuilder sb = new StringBuilder();
        sb.append("<h1 style=\"text-align: center\"> Complete Registration</h1>");
        sb.append("<br>");
        sb.append("<p>Click the link below to complete registration</p>\n");
        sb.append("<p><a style=\"padding: 5px; background-color: indianred; color: white\"  href=\"http://localhost:8081/auth/registration/confirm/")
                .append(entity.getId()).append("\" target=\"_blank\">Click Here</a></p>\n");


        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setToAccount(dto.getEmail());
        messageDTO.setSubject("Complete registration");
        messageDTO.setText(sb.toString());
        emailSendingService.sendMimeMessage(messageDTO);

        return "Email was sent";
    }

    public String registrationConfirm(Long id) {
        Optional<Profile> optional = profileRepository.findByIdAndVisibleTrue(id);
        if (optional.isEmpty()) {
            return "Not Completed";
        }
        Profile entity = optional.get();
        if (!entity.getStatus().equals(Status.IN_REGISTRATION)) {
            return "Not Completed";
        }
        entity.setStatus(Status.ACTIVE);
        profileRepository.save(entity);
        return "Completed";
    }


//    private void existsByEmailOrPhone(String email, String phone) {
//
//        if (email != null && !email.trim().isEmpty()) {
//            boolean isEmailExist = profileRepository.existsByEmail(email);
//            if (isEmailExist) {
//                throw new DataExistsException("Profile with email: " + email + " already exists");
//            }
//        }
//
//        if (phone != null && !phone.trim().isEmpty()) {
//            boolean isPhoneExist = profileRepository.existsByPhone(phone);
//            if (isPhoneExist) {
//                throw new DataExistsException("Profile with phone: " + phone + " already exists");
//            }
//        }
//    }


}
