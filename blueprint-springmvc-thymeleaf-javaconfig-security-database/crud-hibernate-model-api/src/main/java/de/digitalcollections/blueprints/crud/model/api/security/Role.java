package de.digitalcollections.blueprints.crud.model.api.security;

import de.digitalcollections.blueprints.crud.model.api.Identifiable;
import java.io.Serializable;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;

/**
 * A user's role.
 * @param <ID> unique id
 */
public interface Role<ID extends Serializable> extends Identifiable<ID>, GrantedAuthority {

  public static final String PREFIX = "ROLE_";

  List<Operation> getAllowedOperations();

  void setAllowedOperations(List<Operation> allowedOperations);

  String getName();

  void setName(String name);
}
