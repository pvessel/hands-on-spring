package com.handsonspring.service.grid;

import com.handsonspring.model.grid.GridEntries;
import com.handsonspring.model.Identifiable;

import java.util.List;
import java.util.UUID;

public abstract class GridColumnsService<T extends Identifiable> {

    /**
     * Return list of rows and columns values by list of entities
     * @param entities source
     * @return column values. Grid requires Object as output
     */
    abstract public List<Object> getGridValues(List<T> entities);

    /**
     * Save entities by grid entries/values.
     * Internaly search for entity, convert grid values and save them
     * @param entries grid values
     */
    abstract public void saveEntities(GridEntries entries);

    /**
     * Creates new entity
     * @return entity
     */
    abstract protected T getNewEntity();

    /**
     * Save entity
     * @param entity entity
     */
    abstract protected void saveEntity(T entity);

    /**
     * Gets entity by given identifier
     * @param id identifier
     * @return entity
     */
    abstract protected T findEntityById(UUID id);

    /**
     * Return found entity if id provided or new one if not
     * @param data identifier
     * @return entity
     */
    protected T getEntity(Object data) {
        String id = (String) data;
        T entity;
        if(null == id || id.isEmpty()) {
            entity = getNewEntity();
        } else {
            UUID uid = UUID.fromString(id);
            entity = findEntityById(uid);
        }
        return entity;
    }
}
