package io.github.enterprise.entity;

import org.springframework.data.domain.Persistable;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

/**
 * Created by Sheldon on 2017/12/07
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
public class BaseModel<T> implements Persistable<T> {
    @Override
    public T getId() {
        return null;
    }

    @Override
    public boolean isNew() {
        return false;
    }
}
