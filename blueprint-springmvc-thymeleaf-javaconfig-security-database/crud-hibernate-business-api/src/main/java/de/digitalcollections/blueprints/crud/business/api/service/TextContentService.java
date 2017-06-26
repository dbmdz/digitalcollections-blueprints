package de.digitalcollections.blueprints.crud.business.api.service;

import de.digitalcollections.blueprints.crud.model.api.TextContent;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @param <T> domain obejct
 * @param <ID> unique id
 */
public interface TextContentService<T extends TextContent, ID extends Serializable> {

  T create();

  T save(TextContent textContent);

  List<T> getAll();

  T get(ID id);

  void delete(ID id);
}
