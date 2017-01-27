package edu.ben.rate_review.daos;

import java.util.List;

public interface Dao<T> {

    /**
     * Save the entity as new or create
     *
     * @param entity The object to save
     * @return The saved object from the DB
     */
    public T save(T entity);

    /**
     * Returns a list
     *
     * @return A list of the Objects
     */
    public List<T> all();

    /**
     * Finds an object based on the id passed in
     *
     * @param id The id of the object to find
     * @return The object T
     */
    public T find(Long id);

}
