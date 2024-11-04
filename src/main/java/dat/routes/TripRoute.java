package dat.routes;

import dat.controllers.impl.TripController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class TripRoute {

    private final TripController tripController = new TripController();

    protected EndpointGroup getRoutes() {

        return () -> {
            get("/", tripController::getAll, Role.USER);
            get("/{id}", tripController::getByID, Role.USER);
            post("/", tripController::create, Role.ADMIN);
            put("/{id}", tripController::update, Role.ADMIN);
            delete("/{id}", tripController::delete, Role.ADMIN);
            put("/{tripId}/guides/{guideId}", tripController::addGuide, Role.ADMIN);
            post("/populate", tripController::populate, Role.ADMIN);
            get("/filter/{category}", tripController::filterByCategory, Role.USER);
        };
    }
}
