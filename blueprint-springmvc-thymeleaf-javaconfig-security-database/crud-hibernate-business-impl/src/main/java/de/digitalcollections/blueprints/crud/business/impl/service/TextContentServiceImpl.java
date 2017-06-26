package de.digitalcollections.blueprints.crud.business.impl.service;

import de.digitalcollections.blueprints.crud.backend.api.repository.TextContentRepository;
import de.digitalcollections.blueprints.crud.business.api.service.TextContentService;
import de.digitalcollections.blueprints.crud.model.api.TextContent;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for TextContent handling.
 */
@Service
@Transactional(readOnly = true)
public class TextContentServiceImpl implements TextContentService<TextContent, Long> {

  @Autowired
  private TextContentRepository textContentRepository;

  @Override
  public TextContent create() {
    return textContentRepository.create();
  }

  @Override
  @Transactional(readOnly = false)
  public TextContent save(TextContent textContent) {
    textContent.setDate(new Date().toString());
    textContentRepository.save(textContent);
    return textContent;
  }

  @Override
  public List<TextContent> getAll() {
    return textContentRepository.findAll(new Sort(Sort.Direction.DESC, "id"));
  }

  @Override
  public TextContent get(Long id) {
    return (TextContent) textContentRepository.findOne(id);
  }

  @Override
  @Transactional(readOnly = false)
  public void delete(Long id) {
    textContentRepository.delete(id);
  }
}
