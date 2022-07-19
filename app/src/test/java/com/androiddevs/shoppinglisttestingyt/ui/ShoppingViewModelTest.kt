package com.androiddevs.shoppinglisttestingyt.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.androiddevs.shoppinglisttestingyt.MainCoroutineRule
import com.androiddevs.shoppinglisttestingyt.base.Constants.MAX_NAME_LENGTH
import com.androiddevs.shoppinglisttestingyt.base.Constants.MAX_PRICE_LENGTH
import com.androiddevs.shoppinglisttestingyt.base.Status
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import com.androiddevs.shoppinglisttestingyt.repositories.FakeShoppingRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class ShoppingViewModelTest {

    private lateinit var viewModel: ShoppingViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        viewModel = ShoppingViewModel(FakeShoppingRepository())
    }

    @Test
    fun `empty shopping item name, returns error`() {
        viewModel.insertShoppingItem("", "5", "3.0")

        val shoppingItem = viewModel.insertShoppingItemStatus.getOrAwaitValue()

        assertThat(shoppingItem.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `empty shopping item amount, returns error`() {
        viewModel.insertShoppingItem("name", "", "3.0")

        val shoppingItem = viewModel.insertShoppingItemStatus.getOrAwaitValue()

        assertThat(shoppingItem.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `empty shopping item price, returns error`() {
        viewModel.insertShoppingItem("name", "5", "")

        val shoppingItem = viewModel.insertShoppingItemStatus.getOrAwaitValue()

        assertThat(shoppingItem.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `shopping item name exceed max length, returns error`() {
        val shoppingItemName = buildString {
            for (i in 1..MAX_NAME_LENGTH + 1) {
                append('A')
            }
        }

        viewModel.insertShoppingItem(shoppingItemName, "5", "3.0")

        val shoppingItem = viewModel.insertShoppingItemStatus.getOrAwaitValue()

        assertThat(shoppingItem.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `shopping item price exceed max length, returns error`() {
        val shoppingItemPrice = buildString {
            for (i in 1..MAX_PRICE_LENGTH + 1) {
                append(1)
            }
        }

        viewModel.insertShoppingItem("name", "5", shoppingItemPrice)

        val shoppingItem = viewModel.insertShoppingItemStatus.getOrAwaitValue()

        assertThat(shoppingItem.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `shopping item with too high amount, returns error`() {
        viewModel.insertShoppingItem("name", "999999999999", "3.0")

        val shoppingItem = viewModel.insertShoppingItemStatus.getOrAwaitValue()

        assertThat(shoppingItem.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `shopping item with valid input, returns success`() {
        viewModel.insertShoppingItem("name", "5", "3.0")

        val shoppingItem = viewModel.insertShoppingItemStatus.getOrAwaitValue()

        assertThat(shoppingItem.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }
}