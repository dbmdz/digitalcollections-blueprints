package de.digitalcollections.blueprints.crud.model.impl.content;

import de.digitalcollections.blueprints.crud.model.api.Resource;
import de.digitalcollections.blueprints.crud.model.api.enums.MimeTypes;

/**
 * An HTML resource.
 */
public class HtmlResourceImpl implements Resource<Long, String> {

  private String html;
  private Long id;

  @Override
  public String getContent() {
    return html;
  }

  @Override
  public String getContentType() {
    return MimeTypes.MIME_TEXT_HTML;
  }

  @Override
  public void setContent(String html) {
    this.html = html;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

}
