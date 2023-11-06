package es.unex.cum.sinf.practica1.model.daos;

import java.util.List;

public interface Dao <T, K>{

    T get(K k);
    
    List<T> getAll();
    
    void insert(T t);
    
    void update(T t);
    
    void delete(K k);
}   
