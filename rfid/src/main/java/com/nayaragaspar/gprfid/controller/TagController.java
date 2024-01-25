package com.nayaragaspar.gprfid.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nayaragaspar.gprfid.service.TagService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("tag")
@Tag(name = "Tag")
public class TagController {
    private TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @Operation(summary = "Gravar tag")
    @PostMapping("/write-tag")
    public ResponseEntity<String> writeTag(@RequestParam String ip, @RequestParam String placa) {
        return ResponseEntity.ok(tagService.writeTag(ip, placa));
    }
}
