package edu.ucsb.cs156.example.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.ucsb.cs156.example.services.UCSBCurriculumService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/curriculum")
public class UCSBCurriculumController {
    @Autowired
    UCSBCurriculumService ucsbCurriculumService;

    @GetMapping(value = "/curriculum", produces = "application/json")
    public ResponseEntity<String> curriculum(@RequestParam String qtr, @RequestParam String dept,
            @RequestParam String level) throws JsonProcessingException {

        String body = ucsbCurriculumService.getJSON(dept, qtr, level);
        log.info("body = {}", body);

        return ResponseEntity.ok().body(body);
    }
}