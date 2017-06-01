package de.digitalcollections.blueprints.crud.model.api;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 * A group of content with title.
 *
 * @param <ID> unique id
 */
public interface ContentGroup<ID extends Serializable> extends Identifiable<ID> {

  String getDescription(Locale locale);

  String getTitle(Locale locale);

  List<ContentView> getContent();
}
