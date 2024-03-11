package com.kekulta.dummyproducts.features.list.domain.models

import androidx.core.net.toUri
import com.kekulta.dummyproducts.features.list.data.models.ProductDto
import logcat.logcat

class ProductDmMapper() {
    fun map(dtos: List<ProductDto>): List<ProductDm> {
        return dtos.mapNotNull { dto ->
            if (dto.id != null && dto.thumbnail != null && dto.description != null && dto.title != null) {
                ProductDm(
                    id = dto.id,
                    title = dto.title,
                    description = dto.description,
                    thumbnail = dto.thumbnail.toUri(),
                    brand = dto.brand,
                    category = dto.category,
                    discountPercentage = dto.discountPercentage,
                    images = dto.images,
                    price = dto.price,
                    rating = dto.rating,
                    stock = dto.stock,
                )
            } else {
                logcat { "Absent of required parameters in $dto" }
                null
            }
        }
    }
}