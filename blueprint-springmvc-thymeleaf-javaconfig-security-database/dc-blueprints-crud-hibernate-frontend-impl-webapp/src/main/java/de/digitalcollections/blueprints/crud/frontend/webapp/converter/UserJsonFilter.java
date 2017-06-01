package de.digitalcollections.blueprints.crud.frontend.webapp.converter;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class UserJsonFilter {

  @JsonIgnore
  abstract String getPasswordHash();
}
