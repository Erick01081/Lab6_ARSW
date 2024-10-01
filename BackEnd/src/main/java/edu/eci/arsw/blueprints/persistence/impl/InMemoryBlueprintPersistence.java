/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.persistence.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;

@Component
public class InMemoryBlueprintPersistence implements BlueprintsPersistence {
    private final ConcurrentHashMap<String, Blueprint> blueprints = new ConcurrentHashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public InMemoryBlueprintPersistence() {
        // load stub data
        Point[] pts = new Point[]{new Point(140, 140), new Point(115, 115)};
        Point[] pts1 = new Point[]{new Point(14, 180), new Point(115, 15)};
        Point[] pts2 = new Point[]{new Point(100, 140), new Point(50, 115)};
        Blueprint bp = new Blueprint("Oscar", "plano_1", pts);
        Blueprint bp1 = new Blueprint("Camilo", "plano_2", pts1);
        Blueprint bp2 = new Blueprint("Camilo", "plano_3", pts2);
        blueprints.put(String.valueOf(new Tuple<>(bp.getAuthor(), bp.getName())), bp);
        blueprints.put(String.valueOf(new Tuple<>(bp1.getAuthor(), bp1.getName())), bp1);
        blueprints.put(String.valueOf(new Tuple<>(bp2.getAuthor(), bp2.getName())), bp2);
    }

    @Override
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        lock.writeLock().lock();
        try {
            String key = getKey(bp.getAuthor(), bp.getName());
            if (blueprints.putIfAbsent(key, bp) != null) {
                throw new BlueprintPersistenceException("The given blueprint already exists: " + bp);
            }
            blueprints.put(key, bp);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Blueprint getBlueprint(String author, String bprintname) throws BlueprintNotFoundException {
        lock.readLock().lock();
        try {
            Blueprint bp = blueprints.get(getKey(author, bprintname));
            if (bp == null) {
                throw new BlueprintNotFoundException("The blueprint does not exist: " + author + " - " + bprintname);
            }
            return bp;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        lock.readLock().lock();
        try {
            List<Blueprint> authorBlueprints = blueprints.values().stream()
                .filter(bp -> bp.getAuthor().equals(author))
                .collect(Collectors.toList());
            if (authorBlueprints.isEmpty()) {
                throw new BlueprintNotFoundException("No blueprints found for the author: " + author);
            }
            return authorBlueprints;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Blueprint getBlueprintByName(String name) throws BlueprintNotFoundException {
        lock.readLock().lock();
        try {
            Blueprint bp = blueprints.values().stream()
                .filter(blueprint -> blueprint.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new BlueprintNotFoundException("No blueprint found with name: " + name));
            return bp;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Blueprint> getAllBluePrints() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(blueprints.values());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Blueprint updateBlueprint(String author, String name, Blueprint blueprint) throws BlueprintNotFoundException {
        String key = getKey(author, name);
        if (!blueprints.containsKey(key)) {
            throw new BlueprintNotFoundException("The blueprint does not exist: " + author + " - " + name);
        }
        blueprints.computeIfPresent(key, (k, oldBlueprint) -> blueprint);
        
        return blueprint;
    }

    private String getKey(String author, String name) {
        return author + ":" + name;
    }
}