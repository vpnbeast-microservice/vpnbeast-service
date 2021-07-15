package com.vpnbeast.vpnbeastservice.persistent.service;

import java.util.List;

public interface CRUDService<T> {

    List<T> getAll(Integer pageNo, Integer pageSize, String sortBy);
    void update(T entity);
    T get(T entity);
    T getById(Long id);
    boolean isExists(T entity);
    void delete(T entity);
    boolean deleteById(Long id);
    
}