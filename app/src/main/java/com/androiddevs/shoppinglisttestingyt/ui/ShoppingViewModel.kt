package com.androiddevs.shoppinglisttestingyt.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.shoppinglisttestingyt.base.Constants.MAX_NAME_LENGTH
import com.androiddevs.shoppinglisttestingyt.base.Constants.MAX_PRICE_LENGTH
import com.androiddevs.shoppinglisttestingyt.base.Event
import com.androiddevs.shoppinglisttestingyt.base.Resource
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.androiddevs.shoppinglisttestingyt.data.remote.responses.ImageResponse
import com.androiddevs.shoppinglisttestingyt.repositories.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val repo: ShoppingRepository
) : ViewModel() {

    val shoppingItems = repo.observeAllShoppingItems()

    val totalPrice = repo.observeTotalPrice()

    private var _images = MutableLiveData<Event<Resource<ImageResponse>>>()
    val images: LiveData<Event<Resource<ImageResponse>>> = _images

    private var _curImageUrl = MutableLiveData<String>()
    val curImageUrl: LiveData<String> = _curImageUrl

    private var _insertShoppingItemStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItemStatus: LiveData<Event<Resource<ShoppingItem>>> =
        _insertShoppingItemStatus

    fun setCurImageUrl(url: String) {
        _curImageUrl.postValue(url)
    }

    fun insetShoppingItemIntoDB(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repo.insertShoppingItem(shoppingItem)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repo.deleteShoppingItem(shoppingItem)
    }

    fun insertShoppingItem(name: String, amountString: String, priceString: String) {
        if (name.isEmpty() || amountString.isEmpty() || priceString.isEmpty()) {
            _insertShoppingItemStatus.postValue(Event(Resource.error("the fields must not be empty")))
            return
        }

        if (name.length > MAX_NAME_LENGTH) {
            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.error(
                        "The name of the item"
                                + "must not exceed $MAX_NAME_LENGTH characters"
                    )
                )
            )
            return
        }

        if (priceString.length > MAX_PRICE_LENGTH) {
            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.error(
                        "The price of the item"
                                + "must not exceed $MAX_PRICE_LENGTH digits"
                    )
                )
            )
            return
        }


        val amount = try {
            amountString.toInt()
        } catch (e: Exception) {
            _insertShoppingItemStatus.postValue(Event(Resource.error("Please enter a valid amount")))
            return
        }

        val shoppingItem =
            ShoppingItem(name, amount, priceString.toFloat(), _curImageUrl.value ?: "")
        insetShoppingItemIntoDB(shoppingItem)
        setCurImageUrl("")
        _insertShoppingItemStatus.postValue(Event(Resource.success(shoppingItem)))
    }

    fun searchForImage(query: String) {
        if (query.isEmpty()) {
            return
        }
        _images.value = Event(Resource.loading())

        viewModelScope.launch {
            val response = repo.searchForImage(query)

            _images.value = Event(response)
        }
    }


}