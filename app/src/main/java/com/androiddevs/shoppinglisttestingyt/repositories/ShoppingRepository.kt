package com.androiddevs.shoppinglisttestingyt.repositories

import androidx.lifecycle.LiveData
import com.androiddevs.shoppinglisttestingyt.base.Resource
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.androiddevs.shoppinglisttestingyt.data.remote.responses.ImageResponse

interface ShoppingRepository {
    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

    fun observeTotalPrice(): LiveData<Float>

    suspend fun searchForImage(imageQuery: String): Resource<ImageResponse>
}