package org.springproject.inventorymangaement.dtos;

public interface DtoImpl<T,V>{
     T DtoToEntity(V v);
     V EntityToDto(T t);
}
