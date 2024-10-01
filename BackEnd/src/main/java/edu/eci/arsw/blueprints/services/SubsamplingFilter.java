package edu.eci.arsw.blueprints.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;

@Service
public class SubsamplingFilter implements Filter {

    public SubsamplingFilter() {
    }

    @Override
    public Blueprint filterPlain(Blueprint bp) {
        List<Point> points = bp.getPoints();
        List<Point> newPoints = new ArrayList<>();
        for (int i = 0; i < points.size() - 1; i += 2) {
            newPoints.add(points.get(i));
        }
        bp.setPoints(newPoints);
        return bp;
    }

}
