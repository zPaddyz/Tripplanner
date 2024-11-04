package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.impl.GuideDAO;
import dat.dtos.GuideDTO;
import dat.security.exceptions.ApiException;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class GuideController implements IController<GuideDTO, Integer> {

    private final GuideDAO dao;

    public GuideController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = GuideDAO.getInstance(emf);
    }

    @Override
    public void getByID(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class)
                .check(integer -> integer > 0, "ID must be a positive integer")
                .get();
        // DTO
        GuideDTO guideDTO = dao.getByID(id);

        // response
        if (guideDTO != null) {
            ctx.res().setStatus(200);
            ctx.json(guideDTO, GuideDTO.class);
        } else {
            throw new ApiException(404, "Guide not found - /api/guides/" + id);
        }
    }

    @Override
    public void getAll(Context ctx) {
        // List of DTOS
        List<GuideDTO> guideDTOS = dao.getAll();

        if (guideDTOS.isEmpty()) {
            throw new ApiException(404, "Guides not found - /api/guides/");
        } else {
            ctx.res().setStatus(200);
            ctx.json(guideDTOS);
        }
    }

    @Override
    public void create(Context ctx) {
        try {
            // request
            GuideDTO jsonRequest = ctx.bodyAsClass(GuideDTO.class);
            // DTO
            GuideDTO guideDTO = dao.create(jsonRequest);
            // response
            ctx.res().setStatus(201);
            ctx.json(guideDTO, GuideDTO.class);
        } catch (Exception e) {
            throw new ApiException(400, "Error creating guide: " + e.getMessage());
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
            GuideDTO guideDTO = dao.update(id, validateEntity(ctx));

            // response
            if (guideDTO != null) {
                ctx.res().setStatus(200);
                ctx.json(guideDTO, GuideDTO.class);
            } else {
                throw new ApiException(404, "Guide not found - /api/guides/" + id);
            }
        } catch (Exception e) {
            throw new ApiException(400, "Error updating guide: " + e.getMessage());
        }
    }

    @Override
    public void delete(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class)
                .check(integer -> integer > 0, "ID must be a positive integer")
                .get();
        try {
            GuideDTO guideDTO = dao.getByID(id);
            // response
            if (guideDTO != null) {
                dao.delete(id);
                ctx.res().setStatus(204);
            } else {
                throw new ApiException(404, "Guide not found - /api/guides/" + id);
            }
        } catch (Exception e) {
            throw new ApiException(400, "Error deleting guide: " + e.getMessage());
        }
    }

    @Override
    public GuideDTO validateEntity(Context ctx) {
        return ctx.bodyValidator(GuideDTO.class)
                .check(h -> h.getFirstName() != null && !h.getFirstName().isEmpty(), "Guide name must be set")
                .check(h -> h.getEmail() != null, "Guide email must be set")
                .get();
    }
}
