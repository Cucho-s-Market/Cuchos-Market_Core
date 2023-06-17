package com.project.cuchosmarket;

import com.project.cuchosmarket.models.Category;

public class CategoryTestBase {

    protected Category validCategory = new Category("electronica", "cables", "carlos@");

    protected String validCategoryRequestJSON = "{\"name\":\"electronica\",\"descripcion\":\"cables\",\"image\":\"carlos@\"}";
    protected String invalidCategoryRequestJSON = "{\"name\":\"electronica\",\"descripcion\":\"cables\",\"image\":\"adri@\"}";

    public CategoryTestBase() throws Exception {

    }
}


