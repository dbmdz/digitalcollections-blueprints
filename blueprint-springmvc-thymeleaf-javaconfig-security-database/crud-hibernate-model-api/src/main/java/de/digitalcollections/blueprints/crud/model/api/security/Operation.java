package de.digitalcollections.blueprints.crud.model.api.security;

import de.digitalcollections.blueprints.crud.model.api.Identifiable;
import java.io.Serializable;

/**
 * An operation the system can execute.
 * @param <ID> unique identifier
 */
public interface Operation<ID extends Serializable> extends Identifiable<ID> {

  public static final String PREFIX = "OP_";

  String getName();

  void setName(String name);
}
