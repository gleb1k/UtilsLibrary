# Android Utility Library

This library provides a set of utilities for Android development, including repository management, shared preferences management, keyboard visibility handling, location services, authentication token management, and Adapter Delegate (Composite Adapter) for RecyclerView.

## Features

- **Repository Management**: Simplifies data fetching and caching with in-memory and database-backed repositories.
- **AdapterDelegate (Composite Adapter)**: Allows the use of multiple different adapters as one, supporting multiple views in a single RecyclerView.
- **Shared Preferences Manager**: Provides a centralized way to manage shared preferences for various data types.
- **Keyboard Helper**: Detects keyboard visibility changes and provides utility methods to hide the keyboard.
- **Location Helper**: Ensures location permissions and retrieves the current location.
- **Auth Token Manager**: Manages authentication tokens for access and refresh purposes.

## Installation

To include this library in your project, add the following dependency to your `build.gradle` file using [JitPack](https://jitpack.io/).

1. Add the JitPack repository to your project-level `build.gradle`:

```kotlin
allprojects {
    repositories {
        ...
        maven("https://jitpack.io")
    }
}
```

2. Add the dependency in your module-level `build.gradle`:
   
```kotlin
dependencies {
    ...
    implementation("com.github.gleb1k:UtilsLibrary:X.X.X")
}
```

## Usage
**1. Repository Management**

```kotlin
class ExampleRepository(
    service: ExampleService,
    cacheSourceExample: CacheSourceExample,
) {
    /**
     * Private property storing the state of calls and cache in memory.
     */
    private val exampleProperty: RepositoryProperty<ExampleResponse, ExampleParameters> =
        inMemoryRepositoryProperty(
            remoteSource = { params ->
                service.getItem(params.id, params.name)
            }
        )

    /**
     * Private property storing the state of calls and data in a custom local storage,
     * such as a database.
     */
    private val examplePropertyDataBase: RepositoryProperty<ExampleResponse, ExampleParameters> =
        repositoryProperty(
            remoteSource = { params ->
                service.getItem(params.id, params.name)
            },
            cachedSource = cacheSourceExample
        )

    /**
     * Function through which we communicate with the property.
     * If we want to retrieve data from the cache, specify forceUpdate == false.
     * All logic of retrieving from the cache is hidden under the hood.
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
```
**2. Adapter delegate**

Create Item
```kotlin
class ExampleItem(
    val model: ExampleUiModel,
) : DiffListItem {

    override fun areItemsSame(other: DiffListItem): Boolean = other is ExampleItem

    override fun areContentsSame(other: DiffListItem): Boolean = other is ExampleItem
            && other.model == model

}
data class ExampleUiModel(
    val id: Int,
    val name: Int,
)
```
Create Adapter and Holder

```kotlin
class ExampleAdapter : AdapterDelegate<ExampleItem> {
    override fun isForViewType(item: ListItem): Boolean = item is ExampleItem
    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): DelegateViewHolder<ExampleItem> {
        return ExampleViewHolder(
            inflater.inflate(R.layout.item_some, parent, false),
        )
    }

    class ExampleViewHolder(
        view: View,
    ) : DelegateViewHolder<ExampleItem>(view) {

        override fun bind(item: ExampleItem) {
            with(item.model) {
                //bind your model here
            }
        }
    }
}
```

Create a Composite Adapter

```kotlin
// In your Fragment or Activity:

// Create a composite adapter
private val compositeAdapter: CompositeAdapter<ExampleItem> by lazy {
    CompositeAdapter(
        DateAdapter().castAdapterDelegate(),
        ExampleAdapter().castAdapterDelegate()
    )
}

// Inside your setup code, such as onCreate() or onCreateView():
recycler = findViewById<RecyclerView?>(R.id.rv_example).apply {
    adapter = compositeAdapter
}

// Update RecyclerView data with new items
compositeAdapter.updateData(listOf())
// This method updates the RecyclerView with new data
```

**3. Shared Preferences Manager**
```kotlin
// Initialize, for example, in the Application class
PrefManager.init(this)

// After initialization, preferences can be accessed from anywhere in the application
val intPreference = PrefManager.intPreference("some_key")
intPreference.put(100)
val valueFromPreference = intPreference.get() // 100
```

**4. Keyboard Helper**
```kotlin
// Inside your Fragment:

// Define a keyboard listener
private val keyboardListener = object :
    KeyboardVisibilityListener {
    override fun onKeyboardShown() {
        // Perform fragment-specific actions when the keyboard is shown
        // For example, modify the UI elements within the fragment
    }

    override fun onKeyboardHidden() {
        // Perform fragment-specific actions when the keyboard is hidden
        // For example, reset UI elements within the fragment
    }
}

override fun onResume() {
    super.onResume()
    // Register the keyboard listener when the fragment is resumed
    KeyboardHelper.registerListener(
        requireActivity(),
        keyboardListener
    )
}

override fun onStop() {
    // Unregister the keyboard listener when the fragment is stopped
    KeyboardHelper.unregisterListener(keyboardListener)
    super.onStop()
}
```

**5. Auth Token Manager**
```kotlin
// Create or get an instance of the AuthTokenManager implementation
val authTokenManager = AuthTokenManagerImpl()

// Set tokens
authTokenManager.setAccess("access_token")
authTokenManager.setRefresh("refresh_token")
// Retrieve tokens
val accessToken = authTokenManager.access() // access_token
val refreshToken = authTokenManager.refresh() // refresh_token
```
## Contributors
- Idea and code - Gleb Gafeev (tg: @glebgafeev)

## ❗❗❗ Note ❗❗❗
This project was undertaken as a learning exercise. While it demonstrates various Android development concepts, it is not recommended for download or use in production environments.

