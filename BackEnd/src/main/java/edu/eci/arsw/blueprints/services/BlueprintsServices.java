/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;

/**
 *
 * @author hcadavid
 */
@Service
public class BlueprintsServices {

    @Autowired
    BlueprintsPersistence bpp;

    @Autowired
    Filter filter;

    public void addNewBlueprint(Blueprint bp) {
        try {
            bpp.saveBlueprint(bp);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public List<Blueprint> getAllBlueprints() {
        return bpp.getAllBluePrints();
    }

    /**
     *
     * @param author blueprint's author
     * @param name ueprint's name
     * @return the blueprint of the given name created by the given author
     * @throws BlueprintNotFoundException if there is no such blueprint
     */
    public Blueprint getBlueprint(String author, String name) throws BlueprintNotFoundException {
        return bpp.getBlueprint(author, name);

    }

    /**
     *
     * @param author blueprint's author
     * @return all the blueprints of the given author
     * @throws BlueprintNotFoundException if the given author doesn't exist
     */
    public List<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        return bpp.getBlueprintsByAuthor(author);
    }

    public Blueprint getBlueprintsByName(String name) throws BlueprintNotFoundException {

        try {
            return bpp.getBlueprintByName(name);
        } catch (Exception e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public Blueprint updateBlueprint(String author, String name, Blueprint blueprint)
            throws BlueprintNotFoundException {
        try {
            return bpp.updateBlueprint(author, name, blueprint);
        } catch (Exception e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public Blueprint filterBlueprint(Blueprint blueprint){
        return filter.filterPlain(blueprint);
    }



}
