package org.springproject.inventorymangaement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springproject.inventorymangaement.dtos.ReorderRuleDto;
import org.springproject.inventorymangaement.dtos.StatusSender;
import org.springproject.inventorymangaement.services.ReorderRuleService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reorder-rules")
public class ReorderRuleController {

    @Autowired
    private ReorderRuleService reorderRuleService;

    // Get all reorder rules
    @GetMapping
    public ResponseEntity<List<ReorderRuleDto>> getAllReorderRules() {
        List<ReorderRuleDto> rules = reorderRuleService.getReorderRules();
        return ResponseEntity.ok(rules);
    }

    // Get a specific reorder rule by ID
    @GetMapping("/{id}")
    public ResponseEntity<ReorderRuleDto> getReorderRuleById(@PathVariable UUID id) {
        ReorderRuleDto ruleDto = reorderRuleService.getReorderRuleById(id);
        if (ruleDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ruleDto);
    }

    // Add a single reorder rule
    @PostMapping
    public ResponseEntity<StatusSender> createReorderRule(@RequestBody ReorderRuleDto reorderRuleDto) {
        StatusSender response = reorderRuleService.addReorderRule(reorderRuleDto);
        return ResponseEntity.ok(response);
    }

    // Bulk add reorder rules
    @PostMapping("/bulk")
    public ResponseEntity<StatusSender> createAllReorderRules(@RequestBody List<ReorderRuleDto> reorderRuleDtos) {
        StatusSender response = reorderRuleService.addReorderRules(reorderRuleDtos);
        return ResponseEntity.ok(response);
    }
}