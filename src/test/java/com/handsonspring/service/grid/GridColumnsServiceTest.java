package com.handsonspring.service.grid;

import com.handsonspring.model.Identifiable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GridColumnsServiceTest {

    private GridColumnsService<Identifiable>  gridColumnService;

    private Identifiable newEntity;

    private Identifiable existingEntity;

    private UUID id;

    @BeforeEach
    void initUseCase() {

        gridColumnService = Mockito.mock(
                GridColumnsService.class,
                Mockito.CALLS_REAL_METHODS);
        newEntity = mock(Identifiable.class);
        when(gridColumnService.getNewEntity()).thenReturn(newEntity);

        id = UUID.randomUUID();
        existingEntity = mock(Identifiable.class);
        when(gridColumnService.findEntityById(id)).thenReturn(existingEntity);
    }

    @Test
    void getEntity_whenIdNoProvided_thenReturnNewEntity() {

        // given
        // init use case

        // when
        Identifiable entity = gridColumnService.getEntity(null);

        // then
        assertEquals(newEntity, entity);
    }

    @Test
    void getEntity_whenIdProvided_thenReturnFoundEntity() {

        // given
        // init use case
        String idAsString = id.toString();

        // when
        Identifiable entity = gridColumnService.getEntity(idAsString);

        // then
        assertEquals(existingEntity, entity);
    }
}