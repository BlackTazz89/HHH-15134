package com.marinofrancesco.core.foo;

import com.marinofrancesco.dto.NewFooEntityDTO;
import com.marinofrancesco.core.foo.entity.FooEntity;
import com.marinofrancesco.core.foo.entity.FooEntity.FooEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface FooEntityService {

    Long createFooEntity(NewFooEntityDTO newFooEntity);

    void changeName(Long id, String name);

    @Service
    @Transactional
    class FooEntityServiceImpl implements FooEntityService {

        private final FooEntityRepository fooEntityRepository;

        public FooEntityServiceImpl(FooEntityRepository fooEntityRepository) {
            this.fooEntityRepository = fooEntityRepository;
        }

        @Override
        public Long createFooEntity(final NewFooEntityDTO newFooEntity) {
            return fooEntityRepository
                    .save(FooEntity.of(newFooEntity.name()))
                    .getId();
        }

        @Override
        public void changeName(final Long id, final String name) {
            fooEntityRepository
                    .getById(id)
                    .setName(name);
        }
    }

}
