package com.lajming.investmentsapp;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

class CollectionUtils {

    static <T, K, U> Map<K, U> toMap(Collection<T> collection,
        Function<? super T, ? extends K> keyMapper,
        Function<? super T, ? extends U> valueMapper) {
        return collection.stream().collect(Collectors.toMap(keyMapper, valueMapper));
    }

    static <T> List<T> allElementsExceptFirst(List<T> funds) {
        return funds.subList(1, funds.size());
    }

    static <T> T firstElement(Collection<T> collection) {
        return collection.iterator().next();
    }
}
