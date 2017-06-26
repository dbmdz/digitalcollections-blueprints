package de.digitalcollections.blueprints.crud.backend.api.repository;

import de.digitalcollections.blueprints.crud.model.api.TextContent;
import java.io.Serializable;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @param <T> entity instance
 * @param <ID> unique id
 */
public interface TextContentRepository<T extends TextContent, ID extends Serializable> extends PagingAndSortingRepository<T, ID> {

  T create();

  @Override
  List<T> findAll(Sort sort);

}
