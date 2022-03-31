package com.marinofrancesco.core.bar.entity;

import com.marinofrancesco.core.AbstractEntity;
import com.marinofrancesco.core.foo.entity.FooEntity;

import javax.persistence.*;

@Entity
public class BarEntity extends AbstractEntity<Long> {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_bar_foo"), nullable = false)
    private FooEntity foo;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    protected void setId(Long id) {
        this.id = id;
    }

    public FooEntity getFoo() {
        return foo;
    }

    public void setFoo(FooEntity foo) {
        this.foo = foo;
    }

    @Override
    public String toString() {
        return """
                BarEntity:
                   id=%d
                """.formatted(id);
    }
}