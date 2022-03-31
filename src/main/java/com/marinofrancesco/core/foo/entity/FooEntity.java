package com.marinofrancesco.core.foo.entity;

import com.marinofrancesco.core.AbstractEntity;
import com.marinofrancesco.core.bar.entity.BarEntity;
import com.marinofrancesco.core.baz.entity.BazEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 *<p>
 *  I added two <code>*ToMany</code> relations to <code>FooEntity</code> to make
 *  <a href="https://github.com/hibernate/hibernate-orm/blob/5.6.5/hibernate-core/src/main/java/org/hibernate/tuple/entity/EntityMetamodel.java#L386">
 *      dynamicUpdate
 *  </a> evaluate to true.
 *</p>
 *
 * <p><code>dynamicUpdate=true</code> causes <code>initializeBeforeWrite=false</code>.</p>
 * @see <a href="https://github.com/hibernate/hibernate-orm/blob/5.6.5/hibernate-core/src/main/java/org/hibernate/bytecode/enhance/spi/interceptor/EnhancementAsProxyLazinessInterceptor.java#L76">initializeBeforeWrite</a>.
 */

@Entity
public class FooEntity extends AbstractEntity<Long> {

    public interface FooEntityRepository extends JpaRepository<FooEntity, Long> {
    }

    @Id
    @GeneratedValue
    private long id;
    @Version
    private int version;

    private String name;

    @OneToMany(mappedBy = "foo", cascade = CascadeType.ALL, targetEntity = BarEntity.class, orphanRemoval = true)
    public Set<BarEntity> bars = new HashSet<>();

    @OneToMany(mappedBy = "foo", cascade = CascadeType.ALL, targetEntity = BazEntity.class, orphanRemoval = true)
    public Set<BazEntity> bazzes = new HashSet<>();

    public static FooEntity of(String name) {
        final FooEntity f = new FooEntity();
        f.name = name;
        return f;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    protected void setId(Long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    protected void setVersion(int version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<BarEntity> getBars() {
        return bars;
    }

    public void addBar(BarEntity bar) {
        bars.add(bar);
        bar.setFoo(this);
    }

    public void removeBar(BarEntity bar) {
        bars.remove(bar);
        bar.setFoo(null);
    }

    public Set<BazEntity> getBazzes() {
        return bazzes;
    }

    public void addBaz(BazEntity baz) {
        bazzes.add(baz);
        baz.setFoo(this);
    }

    public void removeBaz(BazEntity baz) {
        bazzes.remove(baz);
        baz.setFoo(null);
    }

    @Override
    public String toString() {
        return """
                FooEntity:
                   id=%d,
                   version=%d,
                   name=%s
                """.formatted(id, version, name);
    }
}
