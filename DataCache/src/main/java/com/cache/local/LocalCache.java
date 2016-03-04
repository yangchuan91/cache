package com.cache.local;


public abstract interface LocalCache
{
  public abstract void put(Object paramObject1, Object paramObject2);

  public abstract void remove(Object paramObject);

  public abstract Object get(Object paramObject);
}