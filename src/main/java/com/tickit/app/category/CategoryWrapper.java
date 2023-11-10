package com.tickit.app.category;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryWrapper {
    private List<Category> categories;

    public CategoryWrapper(List<Category> categories) {
        this.categories = categories;
    }
}
