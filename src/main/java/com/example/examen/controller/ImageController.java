package com.example.examen.controller;

import com.example.examen.service.IPersonnelService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageController {

    private final IPersonnelService personnelService;

    public ImageController(IPersonnelService personnelService) {
        this.personnelService = personnelService;
    }


    @GetMapping("/getimage/{id}")
    public ResponseEntity<ByteArrayResource> getImageById (@PathVariable Long id) {

        byte[] personnelImage = personnelService.findPersonnelById(id).get().getPicture();

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"main-image.jpg\"")
                .body(new ByteArrayResource(personnelImage));


    }
}
