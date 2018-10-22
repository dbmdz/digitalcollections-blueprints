package de.digitalcollections.blueprints.crud.backend.impl.jpa.repository;

import de.digitalcollections.blueprints.crud.backend.api.repository.UserRepository;
import de.digitalcollections.blueprints.crud.backend.config.SpringConfigBackendForTest;
import de.digitalcollections.blueprints.crud.backend.impl.jpa.entity.TestUserFactory;
import de.digitalcollections.blueprints.crud.backend.impl.jpa.entity.UserImplJpa;
import de.digitalcollections.blueprints.crud.model.api.security.User;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfigBackendForTest.class})
@Transactional
public class UserRepositoryImplJpaTest {

  @Autowired
  private UserRepository repository;

  @Test
  @Rollback(true)
  public void testSaveAndFind() {
    UserImplJpa entity = TestUserFactory.build("test@test.org");
    repository.save(entity);
    Optional result = repository.findById(entity.getId());
    User user = (User) result.get();

    assertNotNull(user);
    assertEquals(entity.getId(), user.getId());
  }

  @Test
  @Rollback(true)
  public void testSaveAndDelete() throws Exception {
    UserImplJpa entity = TestUserFactory.build("test@test.org");
    repository.save(entity);
    Optional result = repository.findById(entity.getId());
    User foundEntity = (User) result.get();
    assertEquals(entity.getId(), foundEntity.getId());

    repository.delete(foundEntity);
    result = repository.findById(entity.getId());
    Assert.assertTrue(!result.isPresent());
  }

  @Test
  @Rollback(true)
  public void testFindByEmail() throws Exception {
    UserImplJpa entity = TestUserFactory.build("test@test.org");
    repository.save(entity);
    User user = repository.findByEmail("test@test.org");
    assertEquals(entity.getId(), user.getId());
  }
}
