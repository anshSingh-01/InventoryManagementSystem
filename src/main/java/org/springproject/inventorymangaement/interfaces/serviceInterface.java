package org.springproject.inventorymangaement.interfaces;

import java.util.List;

public interface serviceInterface<T,K> {

        K get();
        void add(K k);
        List<K> getList();
        void addList(List<K> kList);

}
