package de.digitalcollections.blueprints.crud.model.api;

import java.io.Serializable;

/**
 * @param <T> unique id specifying instance
 */
public interface Identifiable<T extends Serializable> {

  T getId();

  void setId(T id);

}
