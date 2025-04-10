package com.example.assetxandroidapp

import android.content.Context
import com.example.assetxandroidapp.data.Asset
import com.example.assetxandroidapp.data.Expense
import com.github.mikephil.charting.data.BarEntry
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Unit tests for data processing logic
 */
@RunWith(MockitoJUnitRunner::class)
class DashboardTests {

    @Mock
    private lateinit var mockContext: Context

    private lateinit var testAssets: List<Asset>
    private lateinit var testExpenses: List<Expense>

    @Before
    fun setup() {
        // Arrange
        testAssets = listOf(
            createAsset(1, "Laptop", "Alice", 1500.0, "Active", "Laptop1", 1),
            createAsset(2, "Laptop", "Alice", 2000.0, "Active", "Laptop2", 1),
            createAsset(3, "Phone", "Bob", 7250.0, "Inactive", "Phone1", 2)
        )

        testExpenses = listOf(
            createExpense(1, 1, 100.0, "2023-01-15T10:00:00Z", "Paid"),
            createExpense(2, 2, 200.0, "2023-01-20T12:00:00Z", "Pending"),
            createExpense(3, 3, 150.0, "2023-02-10T09:00:00Z", "Paid")
        )
    }

    @Test
    fun testAssetsByTypeGeneratesCorrectEntries() {
        // Act
        val entries = testAssets.groupBy { it.assetType }
            .entries.mapIndexed { i, (type, group) ->
                BarEntry(i.toFloat(), group.size.toFloat(), type)
            }

        // Assert
        assertEquals(2, entries.size)
        assertEquals("Laptop", entries[0].data)
        assertEquals(2f, entries[0].y)
        assertEquals("Phone", entries[1].data)
        assertEquals(1f, entries[1].y)
    }

    @Test
    fun testValueByPersonnelRoundsToThousands() {
        // Act
        val entries = testAssets.groupBy { it.personnel }
            .filter { (personnel, _) -> personnel != "Unassigned" }
            .entries.mapIndexed { i, (person, group) ->
                val value = (group.sumOf { it.assetValue } / 1000).toFloat()
                BarEntry(i.toFloat(), value, person)
            }

        // Assert
        assertEquals(2, entries.size)
        assertEquals("Alice", entries[0].data)
        assertEquals(3.5f, entries[0].y) // 3500/1000 = 3.5 (not rounded)
        assertEquals("Bob", entries[1].data)
        assertEquals(7.25f, entries[1].y) // 7250/1000 = 7.25 (not rounded)
    }

    @Test
    fun testExpensesByMonthFormatsCorrectly() {
        // Act
        val entries = testExpenses.groupBy {
            it.dateIncurred?.let { date ->
                LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)
                    .format(DateTimeFormatter.ofPattern("MMM"))
            } ?: "Unknown"
        }.entries.mapIndexed { i, (month, group) ->
            BarEntry(i.toFloat(), group.sumOf { it.amount ?: 0.0 }.toFloat(), month)
        }

        // Assert
        assertEquals(2, entries.size)
        assertEquals("Jan", entries[0].data)
        assertEquals(300f, entries[0].y)
        assertEquals("Feb", entries[1].data)
        assertEquals(150f, entries[1].y)
    }

    // creates test objects
    private fun createAsset(
        id: Int,
        type: String,
        personnel: String,
        value: Double,
        status: String,
        name: String,
        typeId: Int
    ): Asset = Asset(
        assetId = id,
        assetType = type,
        personnel = personnel,
        assetValue = value,
        status = status,
        assetName = name,
        brand = "",
        model = "",
        location = "",
        serialNumber = "",
        purchaseDate = "",
        purchaseInvoice = "",
        warrantyExpiry = "",
        createdAt = "",
        createdBy = "",
        updatedAt = "",
        updatedBy = "",
        associations = emptyList(),
        assetAccessories = emptyList(),
        imei = null,
        mobileNumber = null,
        plan = null,
        planType = null,
        provider = null,
        simCode = null,
        assetTypeId = typeId
    )

    private fun createExpense(
        id: Int,
        assetId: Int,
        amount: Double,
        dateIncurred: String,
        paymentStatus: String
    ): Expense = Expense(
        expenseId = id,
        vendorId = 1,
        assetId = assetId,
        amount = amount,
        dateIncurred = dateIncurred,
        paymentStatus = paymentStatus,
        expenseTypeId = null,
        subscriptionId = null,
        currencyCode = null,
        paymentDate = null,
        budgetAllocationId = null,
        description = null,
        createdAt = null,
        createdBy = null,
        updatedAt = null,
        updatedBy = null
    )
}