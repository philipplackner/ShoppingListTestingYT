package com.androiddevs.shoppinglisttestingyt.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ShoppingDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ShoppingItemDatabase
    private lateinit var dao: ShoppingDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ShoppingItemDatabase::class.java
        ).allowMainThreadQueries().build()
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