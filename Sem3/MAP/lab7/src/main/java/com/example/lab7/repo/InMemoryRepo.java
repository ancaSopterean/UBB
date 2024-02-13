package com.example.lab7.repo;

import com.example.lab7.domain.Entity;
import com.example.lab7.domain.validators.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepo <ID, E extends Entity<ID>> implements Repo<ID,E>{
    private Validator<E> validator;
    Map<ID, E> entities;

    public InMemoryRepo(Validator<E> validator){
        this.validator = validator;
        entities = new HashMap<ID,E>();
    }

    /**
     * @param id - the id of the entity to be returned
     *           must not be null
     * @return the entity with the coresponding id
     * @throws RepositoryException if the id is null
     */
    @Override
    public Optional<E> findOne(ID id) {
        if(id == null)
            throw new RepositoryException("id must not be null");
        return Optional.ofNullable(entities.get(id));
    }

    /**
     * @return all the saved entities
     */
    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    /**
     * @param entity entity must be not null
     * @return the saved entity
     * @throws RepositoryException if the enitiy is null
     */
    @Override
    public Optional<E> save(E entity) {
        if (entity == null)
            throw new RepositoryException("entity must not be null");
        validator.validate((entity));
        if(entities.get(entity.getId()) != null)
            return Optional.of(entity);
        else entities.put(entity.getId(),entity);
        return Optional.empty();
    }

    /**
     * @param id id must be not null
     * @throws RepositoryException if the enitiy is null
     * @return the deleted entity
     */
    @Override
    public Optional<E> delete(ID id) {
        E deleted = entities.remove(id);
        //System.out.println(entities.values());
        if( deleted == null){
            throw new RepositoryException("entity does not exist");
        }
        return Optional.ofNullable(deleted);
    }

    /**
     * @param entity entity must not be null
     * @throws RepositoryException if the enitiy is null
     * @return the new updated entity
     */
    @Override
    public Optional<E> update(E entity) {
        if(entity == null)
            throw new RepositoryException("entity must not be null");
        validator.validate(entity);
        entities.put(entity.getId(),entity);

        if(entities.get(entity.getId()) != null){
            //entities.put(entity.getId(),entity);
            return Optional.empty();
        }
        return Optional.ofNullable(entity);
    }
}
