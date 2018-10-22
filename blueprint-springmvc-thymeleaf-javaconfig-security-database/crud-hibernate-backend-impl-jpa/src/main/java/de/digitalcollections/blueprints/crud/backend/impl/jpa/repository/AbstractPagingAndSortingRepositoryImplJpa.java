package de.digitalcollections.blueprints.crud.backend.impl.jpa.repository;

import java.io.Serializable;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @param <T> jpa entity
 * @param <ID> unique entity id
 * @param <R> repository instance
 */
public abstract class AbstractPagingAndSortingRepositoryImplJpa<T, ID extends Serializable, R extends JpaRepository<T, ID>>
        implements PagingAndSortingRepository<T, ID> {

  protected R jpaRepository;

  abstract void setJpaRepository(R jpaRepository);

  @Override
  public long count() {
    return jpaRepository.count();
  }

  @Override
  public void deleteById(ID id) {
    jpaRepository.deleteById(id);
  }

  @Override
  public void delete(T entity) {
    jpaRepository.delete(entity);
  }

  @Override
  public void deleteAll(Iterable<? extends T> entities) {
    jpaRepository.deleteAll(entities);
  }

  @Override
  public void deleteAll() {
    jpaRepository.deleteAll();
  }

  @Override
  public boolean existsById(ID id) {
    return jpaRepository.existsById(id);
  }

  @Override
  public Iterable<T> findAll(Sort sort) {
    return jpaRepository.findAll(sort);
  }

  @Override
  public Page<T> findAll(Pageable pageable) {
    return jpaRepository.findAll(pageable);
  }

  @Override
  public Iterable<T> findAll() {
    return jpaRepository.findAll();
  }

  @Override
  public Iterable<T> findAllById(Iterable<ID> ids) {
    return jpaRepository.findAllById(ids);
  }

  @Override
  public Optional<T> findById(ID id) {
    return jpaRepository.findById(id);
  }

  @Override
  public <S extends T> S save(S entity) {
    return (S) jpaRepository.save(entity);
  }

  @Override
  public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
    return jpaRepository.saveAll(entities);
  }
}
