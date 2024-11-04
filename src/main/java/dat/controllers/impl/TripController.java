package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.config.Populate;
import dat.controllers.IController;
import dat.daos.impl.GuideDAO;
import dat.daos.impl.TripDAO;
import dat.dtos.TripDTO;
import dat.security.exceptions.ApiException;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Map;

public class TripController implements IController<TripDTO, Integer> {

    private final TripDAO dao;

    public TripController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = TripDAO.getInstance(emf);
    }

    @Override
    public void getByID(Context ctx) {
        try{

        // request
        int id = ctx.pathParamAsClass("id", Integer.class)
                .check(integer -> integer > 0, "ID must be a positive integer")
                .get();
        // DTO
        TripDTO tripDTO = dao.getByID(id);

        // response
        if (tripDTO != null) {
            ctx.res().setStatus(200);
            ctx.json(Map.of(
                    "guide", tripDTO.getGuide(),
                    "trip", tripDTO
            ));
        } else {
            ApiException apiException = new ApiException(404, "Trip not found - /api/trips/" + id);
            ctx.res().setStatus(404);
            ctx.json(apiException);
            throw apiException;
        }
        } catch (Exception e) {
            ApiException apiException = new ApiException(404, "Error looking for trip: " + e.getMessage());
            ctx.res().setStatus(404);
            ctx.json(apiException);
            throw apiException;
        }
    }

    @Override
    public void getAll(Context ctx) {
        // List of DTOS
        List<TripDTO> tripDTOS = dao.getAll();

        if (tripDTOS.isEmpty()) {
            throw new ApiException(404, "Trips not found - /api/trips/");
        } else {
            ctx.res().setStatus(200);
            ctx.json(tripDTOS);
        }
    }

    @Override
    public void create(Context ctx) {
        try {
            // request
            TripDTO jsonRequest = ctx.bodyAsClass(TripDTO.class);
            // DTO
            TripDTO tripDTO = dao.create(jsonRequest);
            // response
            ctx.res().setStatus(201);
            ctx.json(tripDTO, TripDTO.class);
        } catch (Exception e) {
            throw new ApiException(400, "Error creating trip: " + e.getMessage());
        }
    }

    @Override
    public void update(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class)
                .check(integer -> integer > 0, "ID must be a positive integer")
                .get();
        try {
            // dto
            TripDTO tripDTO = dao.update(id, validateEntity(ctx));

            // response
            if (tripDTO != null) {
                ctx.res().setStatus(200);
                ctx.json(tripDTO, TripDTO.class);
            } else {
                ApiException apiException = new ApiException(404, "Trip not found - /api/trips/" + id);
                ctx.res().setStatus(404);
                ctx.json(apiException);
                throw apiException;
            }
        } catch (Exception e) {
            ApiException apiException = new ApiException(404, "Error looking for trip: " + e.getMessage());
            ctx.res().setStatus(404);
            ctx.json(apiException);
            throw apiException;
        }
    }

    @Override
    public void delete(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class)
                .check(integer -> integer > 0, "ID must be a positive integer")
                .get();
        try {
            TripDTO tripDTO = dao.getByID(id);
            // response
            if (tripDTO != null) {
                dao.delete(id);
                ctx.res().setStatus(204);
            } else {
                ApiException apiException = new ApiException(404, "Trip not found - /api/trips/" + id);
                ctx.res().setStatus(404);
                ctx.json(apiException);
                throw apiException;
            }
        } catch (Exception e) {
            ApiException apiException = new ApiException(404, "Error looking for trip: " + e.getMessage());
            ctx.res().setStatus(404);
            ctx.json(apiException);
            throw apiException;
        }
    }

    //@Override
    public TripDTO addGuide(Context ctx){
        // Trip
        int tripId = ctx.pathParamAsClass("tripId", Integer.class)
                .check(integer -> integer > 0, "ID must be a positive integer")
                .get();
        // DTO
        TripDTO tripDTO = dao.getByID(tripId);

        // Guide
        int guideId = ctx.pathParamAsClass("guideId", Integer.class)
                .check(integer -> integer > 0, "ID must be a positive integer")
                .get();

        dao.addGuideToTrip(tripId,guideId);
        // response
        if (tripDTO != null) {
            ctx.res().setStatus(200);
            ctx.json(tripDTO, TripDTO.class);
        } else {
            throw new ApiException(404, "Trip not found - /api/trips/" + tripId);
        }
        return tripDTO;
    }

    public void populate(Context ctx) {
        Populate.populateDataWithRequest();
        ctx.res().setStatus(200);
    }

    public void filterByCategory(Context ctx){
        // request
        String category = ctx.pathParam("category");
        // DTO
        List<TripDTO> tripDTOS = dao.filterByCategory(category);
        // response
        if (tripDTOS.isEmpty()) {
            throw new ApiException(404, "Trips not found - /api/trips/");
        } else {
            ctx.res().setStatus(200);
            ctx.json(tripDTOS);
        }
    }

    @Override
    public TripDTO validateEntity(Context ctx) {
        return ctx.bodyValidator(TripDTO.class)
                .check(h -> h.getName() != null && !h.getName().isEmpty(), "Trip name must be set")
                .check(h -> h.getStartTime() != null, "Trip email must be set")
                .get();
    }
}
