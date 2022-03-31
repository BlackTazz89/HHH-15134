package com.marinofrancesco;

import com.marinofrancesco.core.foo.FooEntityService;
import com.marinofrancesco.dto.NewFooEntityDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/*
I omitted @Transactional to make test behave like in production code
(i.e. without an artificial outer transaction)
 */

@SpringBootTest
public class HHH15134IT {

    @Autowired
    private FooEntityService fooEntityService;

    @Test
    public void shotgun() {
        final Long fooId = fooEntityService.createFooEntity(new NewFooEntityDTO("foo"));
        fooEntityService.changeName(fooId, "bar");
    }
}

