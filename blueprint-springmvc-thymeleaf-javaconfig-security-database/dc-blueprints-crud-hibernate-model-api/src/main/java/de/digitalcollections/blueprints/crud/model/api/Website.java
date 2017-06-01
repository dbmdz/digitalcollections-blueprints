package de.digitalcollections.blueprints.crud.model.api;

import java.net.URL;

/**
 * A Website.
 */
public interface Website {

  URL getUrl();

  Sitemap getSitemap();
}
