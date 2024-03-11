package com.kekulta.dummyproducts.features.list.data.impl

import com.kekulta.dummyproducts.features.list.data.api.ProductsDataStore
import com.kekulta.dummyproducts.features.list.data.mappers.PageDmMapper
import com.kekulta.dummyproducts.features.shared.exceptions.IncorrectPageException
import com.kekulta.dummyproducts.features.list.domain.repos.PageRepo
import com.kekulta.dummyproducts.features.list.domain.models.PageDm
import com.kekulta.dummyproducts.features.shared.AbstractCoroutineRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PageRepoImpl(
    private val datastore: ProductsDataStore,
    private val pageDmMapper: PageDmMapper
) : AbstractCoroutineRepository(),
    PageRepo {
    override suspend fun getPage(num: Int): Result<PageDm> {
        val skip = (num - 1) * ON_PAGE

        return try {
            withContext(Dispatchers.IO) {
                val page = pageDmMapper.map(datastore.getProducts(skip, ON_PAGE))

                if (page.currPage > page.pagesTotal) {
                    Result.failure(IncorrectPageException("Incorrect page ${page.currPage} out of ${page.pagesTotal}"))
                } else {
                    Result.success(page)
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

