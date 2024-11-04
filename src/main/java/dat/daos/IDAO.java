package dat.daos;

import java.util.List;

public interface IDAO<T, I> {

    T getByID(I i);
    List<T> getAll();
    T create(T t);
    T update(I i, T t);
    void delete(I i);
    boolean validatePrimaryKey(I i);

}
