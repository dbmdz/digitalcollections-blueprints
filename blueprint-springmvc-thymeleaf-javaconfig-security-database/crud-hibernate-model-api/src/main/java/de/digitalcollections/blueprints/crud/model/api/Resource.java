package de.digitalcollections.blueprints.crud.model.api;

import java.io.Serializable;

/**
 * A resource representing the raw data for content.
 * @param <ID> unique id
 * @param <C> the resource data
 */
public interface Resource<ID extends Serializable, C extends Object> extends Identifiable<ID> {

  C getContent();

  String getContentType();

  void setContent(C content);
}
