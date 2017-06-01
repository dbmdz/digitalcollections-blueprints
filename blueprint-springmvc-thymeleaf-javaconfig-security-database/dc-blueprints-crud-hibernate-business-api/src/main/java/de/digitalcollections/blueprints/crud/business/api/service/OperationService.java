package de.digitalcollections.blueprints.crud.business.api.service;

import de.digitalcollections.blueprints.crud.model.api.security.Operation;
import java.io.Serializable;
import java.util.List;

/**
 * @param <T> domain object
 * @param <ID> unique id
 */
public interface OperationService<T extends Operation, ID extends Serializable> {

  List<T> getAll();
}
