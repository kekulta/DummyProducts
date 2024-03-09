package com.kekulta.dummyproducts.di

import com.kekulta.dummyproducts.features.main.api.DummyProductsApi
import com.kekulta.dummyproducts.features.main.api.NetworkDataStore
import com.kekulta.dummyproducts.features.list.data.impl.NetworkDataStoreImpl
import com.kekulta.dummyproducts.features.list.data.impl.PageRepoImpl
import com.kekulta.dummyproducts.features.list.data.mappers.PageDmMapper
import com.kekulta.dummyproducts.features.list.data.mappers.ProductDmMapper
import com.kekulta.dummyproducts.features.list.domain.formatters.ListStateFormatter
import com.kekulta.dummyproducts.features.list.domain.formatters.ProductPreviewVoFormatter
import com.kekulta.dummyproducts.features.list.domain.repos.PageCacheRepository
import com.kekulta.dummyproducts.features.list.domain.repos.PageStateRepository
import com.kekulta.dummyproducts.features.list.domain.repos.PageRepo
import com.kekulta.dummyproducts.features.list.domain.viewmodels.ListViewModel
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val koinModule = module {
    single<NetworkDataStore> { NetworkDataStoreImpl(get()) }
    single<DummyProductsApi> { ktorfit(get(), "https://dummyjson.com/").create() }
    single { jsonClient() }
    single { PageCacheRepository(get(), get()) }
    single { PageStateRepository(get()) }

    factory { ListStateFormatter(get()) }
    factory<PageRepo> { PageRepoImpl(get(), get()) }
    factory { PageDmMapper(get()) }
    factory { ProductPreviewVoFormatter() }
    factory { ProductDmMapper() }
}

val viewModelsModule = module {
    viewModel { ListViewModel(get()) }
}

private fun ktorfit(client: HttpClient, baseUrl: String): Ktorfit {
    return Ktorfit.Builder().httpClient(client).baseUrl(baseUrl)
        .build()
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