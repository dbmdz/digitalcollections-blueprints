package de.digitalcollections.blueprints.crud.frontend.webapp.converter;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class GrantedAuthorityJsonFilter {

  @JsonIgnore
  abstract String getAuthority();
}
