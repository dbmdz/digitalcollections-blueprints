package de.digitalcollections.blueprints.crud.backend.impl.jpa.repository;

import de.digitalcollections.blueprints.crud.backend.api.repository.TextContentRepository;
import de.digitalcollections.blueprints.crud.backend.config.SpringConfigBackendForTest;
import de.digitalcollections.blueprints.crud.backend.impl.jpa.entity.TextContentImplJpa;
import de.digitalcollections.blueprints.crud.model.api.TextContent;
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
public class TextContentRepositoryImplJpaTest {

  @Autowired
  private TextContentRepository repository;

  @Test
  @Rollback(true)
  public void testSaveAndFind() {
    TextContentImplJpa entity = new TextContentImplJpa();
    repository.save(entity);
    TextContent content = (TextContent) repository.findOne(entity.getId());

    assertNotNull(content);
    assertEquals(entity.getId(), content.getId());
  }
}
