package dasturlash.uz.controller;

import dasturlash.uz.dtos.AttachDTO;
import dasturlash.uz.entity.Attach;
import dasturlash.uz.service.AttachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/attach")
public class AttachController {

    @Autowired
    private AttachService attachService;

    @PostMapping("/upload")
    public ResponseEntity<AttachDTO> upload(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok().body(attachService.upload(file));

    }

    @GetMapping("/open/{fileName}")  // Changed to match parameter name
    public ResponseEntity<Resource> open(@PathVariable("fileName") String fileName) {  // Added explicit name binding
        return attachService.open(fileName);
    }


}
