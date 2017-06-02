package de.digitalcollections.blueprints.crud.model.api;

import de.digitalcollections.blueprints.crud.model.api.enums.ViewType;
import java.io.Serializable;

/**
 * A specific view on a content.
 *
 * @param <ID> unique id
 */
public interface ContentView<ID extends Serializable> extends Identifiable<ID> {

  Resource getResource();

  ViewType getViewType();

  void setViewType(ViewType viewType);
}
