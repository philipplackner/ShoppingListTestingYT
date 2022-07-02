package com.androiddevs.shoppinglisttestingyt.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
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
        println("setup1")
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ShoppingItemDatabase::class.java
        ).allowMainThreadQueries().build()
        println("setup2")
        dao = database.shoppingDao()
        println("setup3")
    }

    @After
    fun teardown() {
        println("teardown1")
        database.close()
        println("teardown2")
    }

    @Test
    fun testInsert() = runBlockingTest {
        println("testInsert1")
        val shoppingItem = ShoppingItem("name", 1, 1f, "url", 1)

        println("testInsert2")
        dao.insertShoppingItem(shoppingItem)

        println("testInsert3")
        val shoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

        assertThat(shoppingItems).contains(shoppingItem)
    }

}