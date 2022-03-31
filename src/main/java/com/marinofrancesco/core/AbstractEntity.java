package com.marinofrancesco.core;

import org.springframework.data.util.ProxyUtils;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public abstract class AbstractEntity<T extends Serializable> {

    public abstract T getId();

    protected abstract void setId(T id);

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (getClass() != ProxyUtils.getUserClass(obj)) return false;

        final AbstractEntity<?> other = (AbstractEntity<?>) obj;
        return getId() != null && getId().equals(other.getId());
    }
}
