package com.kekulta.dummyproducts.features.list.data.impl

import com.kekulta.dummyproducts.features.list.data.api.CategoryDataStore
import com.kekulta.dummyproducts.features.list.data.mappers.CategoryDmMapper
import com.kekulta.dummyproducts.features.list.domain.models.CategoryDm
import com.kekulta.dummyproducts.features.list.domain.repos.CategoryRepo
import com.kekulta.dummyproducts.features.shared.AbstractCoroutineRepository
import com.kekulta.dummyproducts.features.shared.exceptions.IncorrectPageException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoryRepoImpl(
    private val datastore: CategoryDataStore,
    private val categoryDmMapper: CategoryDmMapper
) : AbstractCoroutineRepository(),
    CategoryRepo {
    override suspend fun getCategory(categoryName: String): Result<CategoryDm> {
        val skip = 0

        return try {
            withContext(Dispatchers.IO) {
                val category = categoryDmMapper.map(datastore.getCategory(categoryName, skip, ON_PAGE))

                if (category.currPage > category.pagesTotal) {
                    Result.failure(IncorrectPageException("Incorrect category ${category.currPage} out of ${category.pagesTotal}"))
                } else {
                    Result.success(category)
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        const val ON_PAGE = 20
    }
}

