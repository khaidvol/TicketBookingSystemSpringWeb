package com.epam.jgmp.dao;

import java.util.List;

/** Interface for data access objects. */
public interface Dao<T> {

  T create(T t);

  T read(long id);

  List<T> readAll();

  T update(T t);

  T delete(long id);

  Long getMaxId();
}
