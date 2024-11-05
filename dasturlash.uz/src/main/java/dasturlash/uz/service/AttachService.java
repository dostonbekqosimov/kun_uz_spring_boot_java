package dasturlash.uz.service;

import dasturlash.uz.dtos.AttachDTO;
import dasturlash.uz.entity.Attach;
import dasturlash.uz.exceptions.AppBadRequestException;
import dasturlash.uz.exceptions.DataNotFoundException;
import dasturlash.uz.repository.AttachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
public class AttachService {

    @Autowired
    private AttachRepository attachRepository;

    private final String folderName = "dasturlash.uz/attaches";


    public AttachDTO upload(MultipartFile file) {
        String pathFolder = getYmDString();
        String key = UUID.randomUUID().toString();
        String extension = getExtension(file.getOriginalFilename());

        File folder = new File(folderName + "/" + pathFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(folderName + "/" + pathFolder + "/" + key + "." + extension);
            Files.write(path, bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Attach entity = new Attach();
        entity.setId(key + "." + extension);
        entity.setPath(pathFolder);
        entity.setSize(file.getSize());
        entity.setOrigenName(file.getOriginalFilename());
        entity.setExtension(extension);
        entity.setVisible(true);
        attachRepository.save(entity);

        return toDTO(entity);
    }

    public ResponseEntity<Resource> open(String id) {
        Attach entity = getById(id);
        String path = folderName + "/" + entity.getPath() + "/" + entity.getId();

        Path filePath = Paths.get(path).normalize();
        Resource resource = null;
        try {
            resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) {
                throw new DataNotFoundException("File not found: " + entity.getId());
            }
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } catch (IOException e) {
            throw new AppBadRequestException("Could not read file: " + id);
        }

    }

    public ResponseEntity<Resource> download(String id) {
        try {
            Attach entity = getById(id);
            String path = folderName + "/" + entity.getPath() + "/" + entity.getId();

            Path filePath = Paths.get(getPath(entity)).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + entity.getOrigenName() + "\"").body(resource);
            } else {
                throw new DataNotFoundException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new DataNotFoundException("Could not read the file!");
        }
    }

    public boolean delete(String id) {
        Attach entity = getById(id);
        attachRepository.delete(entity);
        File file = new File(getPath(entity));
        boolean b = false;
        if (file.exists()) {
            b = file.delete();
        }
        return b;
    }

    public PageImpl<AttachDTO> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Attach> entityPages = attachRepository.findAll(pageable);
        return new PageImpl<>(entityPages.stream().map(this::toDTO).toList(), pageable, entityPages.getTotalElements());
    }

    private String getPath(Attach entity) {
        return folderName + "/" + entity.getPath() + "/" + entity.getId();
    }

    public AttachDTO getAttachDTO(String id) {
        return toDTO(getById(id));
    }

    public Attach getById(String id) {
        return attachRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("File not found with id: " + id));
    }

    private String getYmDString() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DATE);
        return year + "/" + month + "/" + day;
    }

    private String getExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastIndex + 1);
    }

    private AttachDTO toDTO(Attach entity) {
        AttachDTO attachDTO = new AttachDTO();
        attachDTO.setId(entity.getId());
        attachDTO.setOriginName(entity.getOrigenName());
        attachDTO.setSize(entity.getSize());
        attachDTO.setExtension(entity.getExtension());
        attachDTO.setCreatedData(entity.getCreatedDate());
        return attachDTO;
    }
}