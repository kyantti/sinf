package es.unex.cum.sinf.practica1.daos;

import java.util.Set;

public interface Dao <T, K>{

    T get(K k);
    
    Set<T> getAll();
    
    void insert(T t);
    
    void update(T t);
    
    void delete(K k);
}   
