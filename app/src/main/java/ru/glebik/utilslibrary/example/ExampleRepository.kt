package ru.glebik.utilslibrary.example

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.glebik.utilslibrary.repository.Parameters
import ru.glebik.utilslibrary.repository.RepositoryProperty
import ru.glebik.utilslibrary.repository.inMemoryRepositoryProperty
import ru.glebik.utilslibrary.repository.repositoryProperty
import ru.glebik.utilslibrary.repository.sources.CachedSource

data class ExampleResponse(
    val id: Int,
)

data class ExampleParameters(
    val id: Int,
    val name: String,
) : Parameters

class ExampleRepository(
    service: ExampleService,
    cachedSourceForExampleResponse: CacheSourceForExampleResponse,
) {
    /**
     * Приватное свойство, которое хранит состояние вызовов и кеш в оперативной памяти
     */
    private val exampleProperty: RepositoryProperty<ExampleResponse, ExampleParameters> =
        inMemoryRepositoryProperty(
            remoteSource = { params ->
                service.getItem(params.id, params.name)
            }
        )

    /**
     * Приватное свойство, которое хранит состояние вызовов и данные в кастомном локальном хранилище,
     * например в базе данных
     */
    private val examplePropertyDataBase: RepositoryProperty<ExampleResponse, ExampleParameters> =
        repositoryProperty(
            remoteSource = { params ->
                service.getItem(params.id, params.name)
            },
            cachedSource = cachedSourceForExampleResponse
        )

    /**
     * Функция через которую общаемся со свойством
     * Если хотим получить данные из кеша, указываем forceUpdate == false
     * Вся логика взятия из кеша хранится под коробкой
     */
    suspend fun loadExampleProperty(
        forceUpdate: Boolean,
        id: Int,
        name: String,
    ): ExampleResponse = withContext(Dispatchers.IO) {
        val parameters = ExampleParameters(id, name)
        exampleProperty.load(forceUpdate, parameters)
    }
}

class ExampleService {
    fun getItem(id: Int, count: String): ExampleResponse {
        throw NotImplementedError("STUB")
    }
}

class CacheSourceForExampleResponse : CachedSource<ExampleResponse, ExampleParameters> {
    override suspend fun get(param: ExampleParameters): ExampleResponse? {
        //Тут обращаемся к базе данных
        throw NotImplementedError("STUB")
    }

    override suspend fun put(value: ExampleResponse, param: ExampleParameters) {
        //Тут обращаемся к базе данных
        throw NotImplementedError("STUB")
    }
}