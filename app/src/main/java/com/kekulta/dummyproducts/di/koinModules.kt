package com.kekulta.dummyproducts.di

import com.kekulta.dummyproducts.features.list.data.api.CategoryDataStore
import com.kekulta.dummyproducts.features.list.data.api.DummyProductsApi
import com.kekulta.dummyproducts.features.list.data.api.ProductDataStore
import com.kekulta.dummyproducts.features.list.data.api.ProductsDataStore
import com.kekulta.dummyproducts.features.list.data.impl.CategoryDataStoreImpl
import com.kekulta.dummyproducts.features.list.data.impl.CategoryRepoImpl
import com.kekulta.dummyproducts.features.list.data.impl.PageRepoImpl
import com.kekulta.dummyproducts.features.list.data.impl.ProductDataStoreImpl
import com.kekulta.dummyproducts.features.list.data.impl.ProductRepoImpl
import com.kekulta.dummyproducts.features.list.data.impl.ProductsDataStoreImpl
import com.kekulta.dummyproducts.features.list.data.mappers.CategoryDmMapper
import com.kekulta.dummyproducts.features.list.data.mappers.PageDmMapper
import com.kekulta.dummyproducts.features.list.domain.formatters.DetailsVoFormatter
import com.kekulta.dummyproducts.features.list.domain.formatters.ListStateFormatter
import com.kekulta.dummyproducts.features.list.domain.formatters.ProductPreviewVoFormatter
import com.kekulta.dummyproducts.features.list.domain.models.ProductDmMapper
import com.kekulta.dummyproducts.features.list.domain.repos.CategoryCacheRepository
import com.kekulta.dummyproducts.features.list.domain.repos.CategoryRepo
import com.kekulta.dummyproducts.features.list.domain.repos.CategoryStateRepository
import com.kekulta.dummyproducts.features.list.domain.repos.PageCacheRepository
import com.kekulta.dummyproducts.features.list.domain.repos.PageRepo
import com.kekulta.dummyproducts.features.list.domain.repos.PageStateRepository
import com.kekulta.dummyproducts.features.list.domain.repos.ProductCacheRepository
import com.kekulta.dummyproducts.features.list.domain.repos.ProductRepo
import com.kekulta.dummyproducts.features.list.domain.repos.ProductStateRepository
import com.kekulta.dummyproducts.features.list.domain.viewmodels.DetailsViewModel
import com.kekulta.dummyproducts.features.list.domain.viewmodels.ListViewModel
import com.kekulta.dummyproducts.features.list.domain.viewmodels.MainViewModel
import com.kekulta.dummyproducts.features.list.presentation.ui.Dispatcher
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val koinModule = module {
    single<ProductsDataStore> { ProductsDataStoreImpl(get()) }
    single<ProductDataStore> { ProductDataStoreImpl(get()) }
    single<CategoryDataStore> { CategoryDataStoreImpl(get()) }
    single<DummyProductsApi> { ktorfit(get(), "https://dummyjson.com/").create() }
    single { jsonClient() }
    single { PageCacheRepository(get()) }
    single { PageStateRepository(get(), get()) }
    single { ProductStateRepository(get(), get()) }
    single { ProductCacheRepository(get(), get()) }
    single { CategoryCacheRepository(get()) }
    single { CategoryStateRepository(get(), get()) }
    single { Dispatcher() }
    single<ProductRepo> { ProductRepoImpl(get(), get()) }
    single<PageRepo> { PageRepoImpl(get(), get()) }
    single<CategoryRepo> { CategoryRepoImpl(get(), get()) }

    factory { DetailsVoFormatter(get()) }
    factory { ListStateFormatter(get()) }
    factory { PageDmMapper(get()) }
    factory { CategoryDmMapper(get()) }
    factory { ProductPreviewVoFormatter() }
    factory { ProductDmMapper() }
}

val viewModelsModule = module {
    viewModel { ListViewModel(get(), get(), get(), get(), get()) }
    viewModel { DetailsViewModel(get(), get(), get(), get()) }
    viewModel { MainViewModel(get()) }
}

private fun ktorfit(client: HttpClient, baseUrl: String): Ktorfit {
    return Ktorfit.Builder().httpClient(client).baseUrl(baseUrl).build()
}

private fun jsonClient(): HttpClient {
    return HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            })
        }
    }
}