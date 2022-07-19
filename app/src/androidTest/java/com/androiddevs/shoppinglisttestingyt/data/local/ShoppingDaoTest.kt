package com.androiddevs.shoppinglisttestingyt.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class ShoppingDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @Inject
    @Named("test_db")
    lateinit var database: ShoppingItemDatabase
    private lateinit var dao: ShoppingDao

    @Before
    fun setup() {
        hiltAndroidRule.inject()
        dao = database.shoppingDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun testInsert() = runTest {
        val shoppingItem = ShoppingItem("name", 1, 1f, "url", 1)

        dao.insertShoppingItem(shoppingItem)

        val shoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

        assertThat(shoppingItems).contains(shoppingItem)
    }

    @Test
    fun testDelete() = runTest {
        val shoppingItem = ShoppingItem("name", 1, 1f, "url", 1)

        dao.insertShoppingItem(shoppingItem)

        val shoppingItemsAfterInsert = dao.observeAllShoppingItems().getOrAwaitValue()

        assertThat(shoppingItemsAfterInsert).contains(shoppingItem)

        dao.deleteShoppingItem(shoppingItem)

        val shoppingItemsAfterDelete = dao.observeAllShoppingItems().getOrAwaitValue()

        assertThat(shoppingItemsAfterDelete).doesNotContain(shoppingItem)
    }

}