package edu.eci.arsw.blueprints.services;

import org.springframework.stereotype.Service;

import edu.eci.arsw.blueprints.model.Blueprint;

@Service
public interface Filter {

    public Blueprint filterPlain(Blueprint blueprint);
}
