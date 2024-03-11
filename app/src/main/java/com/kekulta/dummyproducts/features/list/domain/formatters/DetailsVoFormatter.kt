package com.kekulta.dummyproducts.features.list.domain.formatters

import androidx.core.net.toUri
import com.kekulta.dummyproducts.features.list.domain.models.ProductDm
import com.kekulta.dummyproducts.features.list.presentation.vo.DetailsVo
import kotlin.math.ceil

class DetailsVoFormatter(
    private val productPreviewVoFormatter: ProductPreviewVoFormatter,
) {
    fun format(dm: ProductDm, recs: List<ProductDm> = emptyList()): DetailsVo {
        val oldPrice = if (dm.price == null || dm.discountPercentage == null) {
            null
        } else {
            ceil(dm.price / (1 - dm.discountPercentage / 100))
        }
        return DetailsVo(
            id = dm.id,
            title = dm.title,
            description = dm.description,
            images = listOf(dm.thumbnail) + (dm.images?.map { it.toUri() } ?: emptyList()),
            recommendations = productPreviewVoFormatter.format(recs),
            price = dm.price,
            oldPrice = oldPrice,
            rating = dm.rating,
            stock = dm.stock,
            brand = dm.brand,
            category = dm.category,
        )
    }
}