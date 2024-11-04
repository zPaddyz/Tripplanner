package dat.routes;

import dat.controllers.impl.GuideController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class GuideRoute {

    private final GuideController guideController = new GuideController();

    protected EndpointGroup getRoutes() {

        return () -> {
            post("/", guideController::create, Role.ADMIN);
            get("/", guideController::getAll, Role.USER);
            get("/{id}", guideController::getByID, Role.USER);
            put("/{id}", guideController::update, Role.ADMIN);
            delete("/{id}", guideController::delete, Role.ADMIN);
        };
    }
}
