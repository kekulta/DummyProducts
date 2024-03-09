package com.kekulta.dummyproducts.features.list.domain.formatters

import com.kekulta.dummyproducts.features.list.domain.models.ProductDm
import com.kekulta.dummyproducts.features.list.presentation.vo.ProductPreviewVo

class ProductPreviewVoFormatter {
    fun format(dos: List<ProductDm>): List<ProductPreviewVo> {
        return dos.map { pdo ->
            ProductPreviewVo(
                id = pdo.id,
                title = pdo.title,
                description = pdo.description,
                thumbnail = pdo.thumbnail
            )
        }
    }
}