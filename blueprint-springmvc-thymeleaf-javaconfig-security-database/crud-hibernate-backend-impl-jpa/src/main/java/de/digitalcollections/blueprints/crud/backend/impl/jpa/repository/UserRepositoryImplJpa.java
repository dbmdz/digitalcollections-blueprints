package de.digitalcollections.blueprints.crud.backend.impl.jpa.repository;

import com.mysema.query.types.expr.BooleanExpression;
import de.digitalcollections.blueprints.crud.backend.api.repository.UserRepository;
import de.digitalcollections.blueprints.crud.backend.impl.jpa.entity.QUserImplJpa;
import de.digitalcollections.blueprints.crud.backend.impl.jpa.entity.UserImplJpa;
import de.digitalcollections.blueprints.crud.model.api.enums.UserRole;
import de.digitalcollections.blueprints.crud.model.api.security.Role;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImplJpa extends AbstractPagingAndSortingRepositoryImplJpa<UserImplJpa, Long, UserRepositoryJpa>
        implements UserRepository<UserImplJpa, Long> {

  QUserImplJpa user = QUserImplJpa.userImplJpa;
  BooleanExpression userIsEnabled = user.enabled.eq(true);
  BooleanExpression userIsAdmin = user.roles.isNotEmpty().and(user.roles.any().name.
          equalsIgnoreCase(Role.PREFIX + UserRole.ADMIN.name()));

  @Override
  public UserImplJpa create() {
    return new UserImplJpa();
  }

  @Override
  public List<UserImplJpa> findActiveAdminUsers() {
    Iterable result = jpaRepository.findAll(userIsEnabled.and(userIsAdmin));
    return (List<UserImplJpa>) result;
  }

  @Override
  public List<UserImplJpa> findAll(Sort sort) {
    return jpaRepository.findAll(sort);
  }

  @Override
  public UserImplJpa findByEmail(String email) {
    return jpaRepository.findByEmail(email);
  }

  @Autowired
  @Override
  void setJpaRepository(UserRepositoryJpa jpaRepository) {
    this.jpaRepository = jpaRepository;
  }
}
