package com.kekulta.dummyproducts.features.list.domain.repos

import com.kekulta.dummyproducts.features.list.domain.models.CategoryDm
import com.kekulta.dummyproducts.features.list.domain.models.ProductDm

interface CategoryRepo {
    suspend fun getCategory(categoryName: String): Result<CategoryDm>
}