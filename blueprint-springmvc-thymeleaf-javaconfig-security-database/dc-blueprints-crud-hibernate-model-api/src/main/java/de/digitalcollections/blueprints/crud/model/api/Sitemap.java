package de.digitalcollections.blueprints.crud.model.api;

import java.util.List;

/**
 * The sitemap of a website.
 */
public interface Sitemap {

  List<Node> getRootNodes();
}
