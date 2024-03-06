package com.desfio.transactionservice.util;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.util.ArrayList;
import java.util.List;

public class PaginationUtil {
    public static Sort getSort(String sort, Direction defaultDirection, String defaultProperty) {
        if (sort != null) {
            List<Sort.Order> orders = new ArrayList<>();

            String[] sortArray = sort.split(",");

            for (int i = 0; i < sortArray.length; i = i + 2) {
                Sort.Order order = new Sort.Order(Direction.valueOf(sortArray[i + 1].toUpperCase()), sortArray[1]);
                orders.add(order);
            }

            return Sort.by(orders);
        }

        return Sort.by(defaultDirection, defaultProperty);
    }
}
