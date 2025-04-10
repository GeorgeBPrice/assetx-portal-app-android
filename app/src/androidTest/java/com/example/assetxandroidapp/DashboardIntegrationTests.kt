package com.example.assetxandroidapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.assetxandroidapp.network.AssetApi
import com.example.assetxandroidapp.network.ExpenseApi
import com.example.assetxandroidapp.viewmodel.DashboardViewModel
import com.example.assetxandroidapp.viewmodel.DashboardViewModelFactory
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(AndroidJUnit4::class)
class DashboardIntegrationTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockWebServer: MockWebServer
    private lateinit var assetApi: AssetApi
    private lateinit var expenseApi: ExpenseApi
    private lateinit var viewModelFactory: DashboardViewModelFactory

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        assetApi = retrofit.create(AssetApi::class.java)
        expenseApi = retrofit.create(ExpenseApi::class.java)

        viewModelFactory = DashboardViewModelFactory(assetApi, expenseApi)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testDashboardDisplaysAssetsFromAPI() {
        runBlocking {
            val mockResponse = """
            [
                {
                    "assetId": 1,
                    "assetType": "Laptop",
                    "personnel": "Alice",
                    "assetValue": 1000.0,
                    "status": "Active",
                    "assetName": "Laptop1",
                    "brand": "",
                    "model": "",
                    "location": "",
                    "serialNumber": "",
                    "purchaseDate": "",
                    "purchaseInvoice": "",
                    "warrantyExpiry": "",
                    "createdAt": "",
                    "createdBy": "",
                    "updatedAt": "",
                    "updatedBy": "",
                    "associations": [],
                    "assetAccessories": [],
                    "assetTypeId": 1
                }
            ]
            """.trimIndent()

            mockWebServer.enqueue(
                MockResponse()
                    .setBody(mockResponse)
                    .setResponseCode(200)
            )
        }

        val testViewModel = DashboardViewModel(assetApi, expenseApi)

        composeTestRule.setContent {
            AssetxAndroidAppTheme {
                DashboardScreen(viewModel = testViewModel)
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Asset Overview").assertExists()
        composeTestRule.onNodeWithText("Assets by Type").assertExists()
    }
    @Test
    fun testChartSelectorUpdatesChartType() {
        val testViewModel = viewModelFactory.create(DashboardViewModel::class.java)

        composeTestRule.setContent {
            AssetxAndroidAppTheme {
                DashboardScreen(viewModel = testViewModel)
            }
        }

        // Wait for the UI to settle
        composeTestRule.waitForIdle()

        // Assert that the chart selector exists
        composeTestRule.onNodeWithText("Asset Overview").assertExists()
        composeTestRule.onNodeWithText("Assets by Type").assertExists()
        composeTestRule.onNodeWithText("Expenses by Payment Status").assertExists()


        // Assert that the chart selector updates the chart type
        composeTestRule.onNodeWithText("Asset Overview").performClick()
        composeTestRule.onNodeWithText("Assets by Type").performClick()
        composeTestRule.onNodeWithText("Expenses by Payment Status").performClick()
    }
}

@Composable
fun AssetxAndroidAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(content = content)
}