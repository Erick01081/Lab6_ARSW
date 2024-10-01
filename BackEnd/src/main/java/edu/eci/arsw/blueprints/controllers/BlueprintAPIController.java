/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.controllers;

import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.services.BlueprintsServices;

/**
 *
 * @author hcadavid
 */
@RestController
@RequestMapping(value = "/blueprints")
@CrossOrigin(origins = "http://localhost:3000") // Cambia al origen permitido

public class BlueprintAPIController {

    @Autowired
    BlueprintsServices bps;

    @GetMapping("/{author}/{bpname}")
    public ResponseEntity<?> getBlueprint(@PathVariable("author") String author,
            @PathVariable("bpname") String bpname) {
        try {
            return new ResponseEntity<>(bps.getBlueprint(author, bpname), HttpStatus.OK);
        } catch (BlueprintNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/{author}")
    public ResponseEntity<?> getBlueprintsByAuthor(@PathVariable("author") String author) {
        try {
            System.out.println(author);
            return new ResponseEntity<>(bps.getBlueprintsByAuthor(author), HttpStatus.OK);
        } catch (BlueprintNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllBlueprints() {
        try {
            return new ResponseEntity<>(bps.getAllBlueprints(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("Error bla bla bla", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> addNewBlueprint(@RequestBody Blueprint bp) {
        try {
            System.out.println(bp);
            bps.addNewBlueprint(bp);
            return new ResponseEntity<>(bp, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>("Error bla bla bla", HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/{author}/{bpname}")
    public ResponseEntity<?> updateBlueprint(@PathVariable("author") String author,
            @PathVariable("bpname") String bpname, @RequestBody Blueprint bp) {
        try {

            bps.updateBlueprint(author, bpname, bp);
            return new ResponseEntity<>(bp, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>("Error bla bla bla", HttpStatus.FORBIDDEN);
        }
    }

}
