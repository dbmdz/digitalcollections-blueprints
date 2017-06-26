package de.digitalcollections.blueprints.crud.model.api;

import java.io.Serializable;

/**
 * A text content in the system
 *
 * @param <ID> unique id
 */
public interface TextContent<ID extends Serializable> extends Identifiable<ID> {

  String getTitle();

  void setTitle(String title);

  String getText();

  void setText(String text);

  String getDate();

  void setDate(String date);
}
