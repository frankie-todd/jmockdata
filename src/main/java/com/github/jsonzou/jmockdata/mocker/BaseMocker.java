package com.github.jsonzou.jmockdata.mocker;

import com.github.jsonzou.jmockdata.MockConfig;
import com.github.jsonzou.jmockdata.Mocker;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

@SuppressWarnings("unchecked")
public class BaseMocker<T> implements Mocker<T> {

  private Type type;

  private Type[] genericTypes;

  public BaseMocker(Type type, Type... genericTypes) {
    this.type = type;
    this.genericTypes = genericTypes;
  }

  @Override
  public T mock(MockConfig mockConfig) {
    Mocker mocker;
    if (type instanceof ParameterizedType) {
      mocker = new GenericMocker((ParameterizedType) type);
    } else if (type instanceof GenericArrayType) {
      mocker = new ArrayMocker(type);
    } else if (type instanceof TypeVariable) {
      Type actualType = mockConfig.getVariableType(((TypeVariable) type).getName());
      mocker = new BaseMocker(actualType);
    } else {
      mocker = new ClassMocker((Class) type, genericTypes);
    }
    return (T) mocker.mock(mockConfig);
  }

}
