package com.kekulta.dummyproducts.features.list.domain.repos

import com.kekulta.dummyproducts.features.list.domain.models.PageDm

interface PageRepo {
    suspend fun getPage(num: Int): Result<PageDm>
}