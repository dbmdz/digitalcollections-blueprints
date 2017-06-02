package de.digitalcollections.blueprints.crud.model.api;

import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.Locale;

/**
 * A node in the sitemap of a website.
 */
public interface Node extends Identifiable<Serializable> {

  Node getParent();

  List<Node> getChildNodes();

  String getDescription(Locale locale);

  String getTitle(Locale locale);

  URL getUrl();

  ContentContainer getContentContainer();

  boolean hasContent();

  boolean hasChildren();
}
