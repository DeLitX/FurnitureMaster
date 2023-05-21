package com.delitx.furnituremaster.data

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.delitx.furnituremaster.data.local.daos.*
import com.delitx.furnituremaster.data.network.DataLoadState
import com.delitx.furnituremaster.data.network.ServerRequestsImpl
import com.delitx.furnituremaster.data.network.exceptions.InternetConnectionErrorException
import com.delitx.furnituremaster.data_models.*
import com.delitx.furnituremaster.data_models.dtos.*
import com.delitx.furnituremaster.utils.truncate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.*

class ProductsRepositoryImpl(
    private val mProductDao: ProductDao,
    private val mPropertyDao: PropertyDao,
    private val mNumeralValueDao: NumeralValueDao,
    private val mNamedValueDao: NamedValueDao,
    private val mProductPropertiesVariationDao: ProductPropertiesVariationDao,
    private val mProductToImageDao: ProductToImageDao,
    private val mCommentedImageDao: CommentedImageDao,
    private val mSharedPreferences: SharedPreferences,
    private val mVersionCheck: VersionCheck,
) : ProductsRepository {
    override val isDataUpdated = MutableLiveData(false)
    override val errorStates: MutableLiveData<ErrorStates> = MutableLiveData(ErrorStates.AllRight)
    private val mPlaceSameOrder: MutableLiveData<DataLoadState<Boolean>> =
        MutableLiveData(DataLoadState.Undefined())
    override val placeSameOrderState: LiveData<DataLoadState<Boolean>> = mPlaceSameOrder
    override fun resetPlaceSameOrderState() {
        mPlaceSameOrder.postValue(DataLoadState.Undefined())
    }

    private val mAllProducts: MutableLiveData<List<Product>> = MutableLiveData(listOf())
    private val mServerRequests = ServerRequestsImpl()

    override fun getLiveProductByName(productId: Int): LiveData<ProductDTO?> {
        return mProductDao.getLiveById(productId)
    }

    override suspend fun getProductsByIds(products: List<Int>): List<Product> {
        return productDtoToProduct(mProductDao.getByIds(products))
    }

    override suspend fun getProductsByValueIds(ids: List<Int>): List<Product> {
        val products = mProductDao.getProductsFromPropertiesValueIds(ids)
        val result = filterProducts(products, ids)
        return if (result.isNotEmpty()) productDtoToProduct(result) else listOf()
    }

    init {
        CoroutineScope(IO).launch {
            try {
                if (isVersionCorrect()) {
                    if (isUpdateNeeded()) {
                        CoroutineScope(IO).launch {
                            clearDB()
                            getDataFromNet()
                            updateAllProducts()
                            //setUpdateTime()
                            isDataUpdated.postValue(true)
                        }
                    } else {
                        CoroutineScope(IO).launch {
                            updateAllProducts()
                            isDataUpdated.postValue(true)
                        }
                    }
                } else {
                    errorStates.postValue(ErrorStates.VersionIncorrect)
                }
            } catch (e: InternetConnectionErrorException) {
                errorStates.postValue(ErrorStates.InternetConnectionError)
            }
        }
    }

    override fun getAllProducts(): LiveData<List<Product>> {
        return mAllProducts
    }

    override fun getCollection(id: Int): LiveData<NamedValue> {
        return mNamedValueDao.getById(id)
    }

    private fun setUpdateTime() {
        val currentTime = Calendar.getInstance(TimeZone.getTimeZone("GMT")).timeInMillis
        val editor = mSharedPreferences.edit()
        editor.putLong("UpdateTime", currentTime)
        editor.apply()
    }

    private fun isUpdateNeeded(): Boolean {
        val timestamp = mServerRequests.getTimestamp()
        val updateTime = mSharedPreferences.getLong("UpdateTime", 0L)
        return timestamp > updateTime || timestamp == 0L
    }

    private fun isVersionCorrect(): Boolean {
        val neededVersion = mServerRequests.getAndroidVersion()
        val currentVersion = mVersionCheck.getAppVersion()
        return neededVersion.toInt() <= currentVersion
    }

    private fun clearDB() {
        mProductToImageDao.deleteAll()
        mCommentedImageDao.deleteAll()
        mNumeralValueDao.deleteAll()
        mPropertyDao.deleteAll()
        mNamedValueDao.deleteAll()
        mProductPropertiesVariationDao.deleteAll()
        mProductToImageDao.deleteAll()
    }

    private suspend fun getDataFromNet() {
        coroutineScope {
            val productsCall = async { mServerRequests.getAllProducts() }
            val propertiesCall = async { mServerRequests.getAllProperties() }
            val valuesCall = async { mServerRequests.getAllVariations() }
            val imagesCall = async { mServerRequests.getAllImages() }
            val products = try {
                productsCall.await()
            } catch (e: Exception) {
                listOf()
            }
            val properties = try {
                propertiesCall.await()
            } catch (e: Exception) {
                listOf()
            }
            val values = try {
                valuesCall.await()
            } catch (e: Exception) {
                listOf()
            }
            val images = try {
                imagesCall.await()
            } catch (e: Exception) {
                listOf()
            }
            val productPropertiesVariation = mutableListOf<ProductPropertiesVariation>()
            val productToImages = mutableListOf<ProductToImage>()
            for (i in products) {
                for (t in i.propertiesList) {
                    for (k in t.value) {
                        productPropertiesVariation.add(ProductPropertiesVariation(i.id, t.key, k))
                    }
                }
                for (t in i.imageUrl) {
                    productToImages.add(ProductToImage(productId = i.id, imageId = t))
                }
            }
            mProductDao.insert(products)
            mProductPropertiesVariationDao.insert(productPropertiesVariation)
            mPropertyDao.insert(properties)
            mPropertyDao.insert(properties)
            mCommentedImageDao.insert(images)
            mProductToImageDao.insert(productToImages)
            val namedValue = mutableListOf<NamedValue>()
            val numeralValue = mutableListOf<NumeralValue>()
            for (i in values) {
                if (i is NumeralValue) {
                    numeralValue.add(i)
                }
                if (i is NamedValue) {
                    namedValue.add(i)
                }
            }
            mNamedValueDao.insert(namedValue)
            mNumeralValueDao.insert(numeralValue)
            Log.d("typeBug", "getDataFromNet: values $values")
        }
    }

    private suspend fun updateAllProducts() {
        val products = mProductDao.getAll()
        if (products.isNotEmpty()) {
            mAllProducts.postValue(productDtoToProduct(products))
        }
    }

    override suspend fun productDtoToProduct(products: List<ProductDTO>): List<Product> {
        val startTime = Calendar.getInstance().timeInMillis
        Log.d("productDtoToProductConv", "productDtoToProduct: startTime=$startTime")
        val result = mutableListOf<Product>()
        val productIds = mutableListOf<Int>()
        for (i in products) {
            productIds.add(i.id)
        }
        Log.d("productDtoToProductConv", "productDtoToProduct: productIds=$productIds")
        val productToProperties = mProductPropertiesVariationDao.getByProductIdBulk(productIds)
        val propertiesIds = mutableListOf<Int>()
        val valuesIds = mutableListOf<Int>()
        val productToPropertyToValuesListMap = productToPropertiesToMap(
            productToProperties = productToProperties,
            propertiesIds = propertiesIds,
            valuesIds = valuesIds
        )
        val imagesIds = products.flatMap { it.imageUrl }

        imagesIds.truncate()
        propertiesIds.truncate()
        valuesIds.truncate()

        val images = mCommentedImageDao.getByIdsBulk(imagesIds)
        val properties = mPropertyDao.getByIdsBulk(propertiesIds)
        val numeralValues = mNumeralValueDao.getByIdsBulk(valuesIds)
        val namedValues = mNamedValueDao.getByIdsBulk(valuesIds)
        val values = mutableListOf<PropertyValue>()
        values.addAll(numeralValues)
        values.addAll(namedValues)

        for (i in products) {
            val propertiesMap = mutableMapOf<ProductProperty, List<PropertyValue>>()
            val productPropertiesIds = productToPropertyToValuesListMap[i.id]
            val imagesList = mutableListOf<CommentedImage>()
            val imageListIds = i.imageUrl
            for (t in imageListIds) {
                val image = images.find { it.id == t }
                if (image != null) {
                    imagesList.add(image)
                }
            }
            if (productPropertiesIds != null) {
                for (t in productPropertiesIds) {
                    val property = properties.find { it.id == t.key }
                    if (property != null) {
                        val propertyValuesIds = t.value
                        val propertyValues = mutableListOf<PropertyValue>()
                        for (k in propertyValuesIds) {
                            val propertyValue = values.find { it.id == k }
                            propertyValue?.let {
                                propertyValues.add(it)
                            }
                        }
                        propertiesMap[property.toModel()] =
                            propertyValues
                    }
                }
            }
            result.add(
                Product(
                    id = i.id,
                    name = i.name,
                    priceFormula = i.priceFormula,
                    propertiesList = propertiesMap,
                    order = i.order,
                    imageUrl = imagesList,
                    modelPath = i.modelPath
                )
            )
        }
        val finishTime = Calendar.getInstance().timeInMillis
        Log.d(
            "productDtoToProductConv",
            "productDtoToProduct: finishTime=$finishTime difference=${finishTime - startTime}"
        )
        return result
    }

    private fun productToImagesToMap(
        productToImages: List<ProductToImage>,
        imagesIds: MutableList<Int>
    ): Map<Int, List<Int>> {
        val result =
            mutableMapOf<Int, MutableList<Int>>()
        for (i in productToImages) {
            if (result[i.productId] == null) {
                result[i.productId] = mutableListOf(i.imageId)
            } else {
                result[i.productId]!!.add(i.imageId)
            }
            imagesIds.add(i.imageId)
        }
        return result
    }

    private fun productToPropertiesToMap(
        productToProperties: List<ProductPropertiesVariation>,
        propertiesIds: MutableList<Int>,
        valuesIds: MutableList<Int>
    ): Map<Int, MutableMap<Int, MutableList<Int>>> {
        val productToPropertyToValuesListMap =
            mutableMapOf<Int, MutableMap<Int, MutableList<Int>>>()
        for (i in productToProperties) {
            if (productToPropertyToValuesListMap[i.productId] == null) {
                productToPropertyToValuesListMap[i.productId] = mutableMapOf()
                productToPropertyToValuesListMap[i.productId]!![i.propertyId] =
                    mutableListOf(i.valueId)
                propertiesIds.add(i.propertyId)
                valuesIds.add(i.valueId)
            } else {
                if (productToPropertyToValuesListMap[i.productId]!![i.propertyId] == null) {
                    productToPropertyToValuesListMap[i.productId]!![i.propertyId] =
                        mutableListOf(i.valueId)
                    propertiesIds.add(i.propertyId)
                    valuesIds.add(i.valueId)
                } else {
                    productToPropertyToValuesListMap[i.productId]!![i.propertyId]!!.add(i.valueId)
                    valuesIds.add(i.valueId)
                }
            }
        }
        return productToPropertyToValuesListMap
    }

    private suspend fun filterProducts(
        products: List<ProductDTO>,
        valueIds: List<Int>
    ): List<ProductDTO> {
        val productIds = products.map { it.id }
        val productDependencies = mProductPropertiesVariationDao.getByProductId(productIds)
        val propertiesFilterBy = mutableSetOf<Int>()
        val propertiesMap = mutableMapOf<Int, MutableMap<Int, MutableList<Int>>>()
        for (i in productDependencies) {
            if (propertiesMap[i.productId] == null) {
                propertiesMap[i.productId] = mutableMapOf(i.propertyId to mutableListOf(i.valueId))
            } else {
                if (propertiesMap[i.productId]!![i.propertyId] == null) {
                    propertiesMap[i.productId]!![i.propertyId] = mutableListOf(i.valueId)
                } else {
                    propertiesMap[i.productId]!![i.propertyId]!!.add(i.valueId)
                }
            }
            if (valueIds.contains(i.valueId)) {
                propertiesFilterBy.add(i.propertyId)
            }
        }
        Log.d(
            "filterBug",
            "filterProducts: productIds $productIds \n productDependencies $productDependencies \n propertiesFilterBy $propertiesFilterBy \n propertiesMap $propertiesMap \n valueIds $valueIds"
        )
        val result = mutableListOf<ProductDTO>()
        // filter products
        // product fits if it have any of values in category A AND any of values in category B etc
        for (i in products) {
            var isFits = true
            for (t in propertiesMap[i.id]!!) {
                if (!isFits) {
                    break
                }
                // checking if product have any of values in category A AND any of values in category B etc
                for (k in propertiesFilterBy) {
                    if (!isFits) {
                        break
                    }
                    var isHaveValues = false
                    // checking if product have any of values in category
                    for (m in propertiesMap[i.id]!![k]!!) {
                        if (valueIds.contains(m)) {
                            isHaveValues = true
                        }
                    }
                    if (!isHaveValues) {
                        isFits = false
                        break
                    }
                }
            }
            if (isFits) {
                result.add(i)
            }
        }
        Log.d("filterBug", "filterProducts: result $result")

        return result
    }
}

interface VersionCheck {
    fun getAppVersion(): Int
    fun openPlayMarket()
}
