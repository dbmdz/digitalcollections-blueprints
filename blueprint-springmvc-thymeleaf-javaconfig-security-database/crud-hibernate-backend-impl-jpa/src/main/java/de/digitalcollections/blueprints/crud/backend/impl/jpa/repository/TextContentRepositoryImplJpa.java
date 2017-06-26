package de.digitalcollections.blueprints.crud.backend.impl.jpa.repository;

import de.digitalcollections.blueprints.crud.backend.api.repository.TextContentRepository;
import de.digitalcollections.blueprints.crud.backend.impl.jpa.entity.TextContentImplJpa;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Matthias Lindinger [Matthias.Lindinger at bsb-muenchen.de]
 */
@Repository
public class TextContentRepositoryImplJpa extends AbstractPagingAndSortingRepositoryImplJpa<TextContentImplJpa, Long, TextContentRepositoryJpa> implements TextContentRepository<TextContentImplJpa, Long> {

  @Autowired
  @Override
  void setJpaRepository(TextContentRepositoryJpa jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public TextContentImplJpa create() {
    return new TextContentImplJpa();
  }

  @Override
  public List<TextContentImplJpa> findAll(Sort sort) {
    return jpaRepository.findAll(sort);
  }

}
