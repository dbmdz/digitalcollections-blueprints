package de.digitalcollections.blueprints.crud.model.api;

import java.io.Serializable;
import java.util.List;

/**
 * A content container of a Node containing contents in different groups.
 *
 * @param <ID> unique id
 */
public interface ContentContainer<ID extends Serializable> extends Identifiable<ID> {

  Node getNode();

  List<ContentView> getContent();

  List<ContentGroup> getContentGroups();
}
