package com.androiddevs.shoppinglisttestingyt.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.androiddevs.shoppinglisttestingyt.base.Resource
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.androiddevs.shoppinglisttestingyt.data.remote.responses.ImageResponse

class FakeShoppingRepository : ShoppingRepository {

    private val shoppingItems = mutableListOf<ShoppingItem>()

    private val shoppingItemsLiveData = MutableLiveData<List<ShoppingItem>>(shoppingItems)

    private val totalPriceLiveData = MutableLiveData<Float>()

    private var shouldReturnNetworkError = false

    fun setShouldShowError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    private fun refreshLiveData() {
        shoppingItemsLiveData.postValue(shoppingItems)
        totalPriceLiveData.postValue(getTotalPrice())
    }

    private fun getTotalPrice(): Float {
        return shoppingItems.sumOf { it.price.toDouble() }.toFloat()
    }

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.add(shoppingItem)
        refreshLiveData()
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.remove(shoppingItem)
        refreshLiveData()
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return shoppingItemsLiveData
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return totalPriceLiveData
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        return if (shouldReturnNetworkError) {
            Resource.error("Error")
        } else {
            Resource.success(ImageResponse(listOf(), 0, 0))
        }
    }


}