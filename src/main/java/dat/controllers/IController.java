package dat.controllers;

import io.javalin.http.Context;

public interface IController<T, D> {
    void getByID(Context ctx);
    void getAll(Context ctx);
    void create(Context ctx);
    void update(Context ctx);
    void delete(Context ctx);
    T validateEntity(Context ctx);

}
