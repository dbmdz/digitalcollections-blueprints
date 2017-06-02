package de.digitalcollections.blueprints.crud.model.impl.content;

import de.digitalcollections.blueprints.crud.model.api.ContentView;
import de.digitalcollections.blueprints.crud.model.api.Resource;
import de.digitalcollections.blueprints.crud.model.api.enums.ViewType;

/**
 * An HTML content.
 */
public class ContentViewImpl implements ContentView<Long> {

  private Long id;
  private ViewType viewType = ViewType.FULL;
  private Resource resource;

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public Resource getResource() {
    return resource;
  }

  @Override
  public ViewType getViewType() {
    return viewType;
  }

  @Override
  public void setViewType(ViewType viewType) {
    this.viewType = viewType;
  }

}
