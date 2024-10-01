/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.persistence;

import java.util.List;

import edu.eci.arsw.blueprints.model.Blueprint;

/**
 *
 * @author hcadavid
 */
public interface BlueprintsPersistence {

    /**
     *
     * @param bp the new blueprint
     * @throws BlueprintPersistenceException if a blueprint with the same name
     *                                       already exists, or any other low-level
     *                                       persistence error occurs.
     */
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException;

    /**
     *
     * @param author     blueprint's author
     * @param bprintname blueprint's author
     * @return the blueprint of the given name and author
     * @throws BlueprintNotFoundException if there is no such blueprint
     */
    public Blueprint getBlueprint(String author, String bprintname) throws BlueprintNotFoundException;

    /**
     *
     * @param author blueprint's author
     * @return the blueprint of the given author
     * @throws BlueprintNotFoundException if there is no such blueprint
     */
    public List<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException;

    public Blueprint getBlueprintByName(String name) throws BlueprintNotFoundException;

    public List<Blueprint> getAllBluePrints();

    public Blueprint updateBlueprint(String author, String name, Blueprint blueprint) throws BlueprintNotFoundException;

}
