package evan.chen.tutorial.mvvmlivedatasample.utils

import android.arch.core.executor.testing.InstantTaskExecutorRule
import evan.chen.tutorial.mvvmlivedatasample.IProductRepository
import evan.chen.tutorial.mvvmlivedatasample.ProductViewModel
import evan.chen.tutorial.mvvmlivedatasample.api.ProductResponse
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations


class ProductViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var repository: IProductRepository
    private var productResponse = ProductResponse()
    private lateinit var viewModel: ProductViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        productResponse.id = "pixel3"
        productResponse.name = "Google Pixel 3"
        productResponse.price = 27000
        productResponse.desc = "Desc"

        viewModel = ProductViewModel(repository)
    }

    @Test
    fun getProductTest() {
        val productId = "pixel3"
        viewModel.getProduct(productId)

        val loadProductCallbackCaptor = argumentCaptor<IProductRepository.LoadProductCallback>()

        //驗證是否有呼叫IProductRepository.getProduct
        verify(repository).getProduct(eq(productId), capture(loadProductCallbackCaptor))

        //將callback攔截下載並指定productResponse的值。
        loadProductCallbackCaptor.value.onProductResult(productResponse)

        Assert.assertEquals(productResponse.name, viewModel.productName.value)
        Assert.assertEquals(productResponse.desc, viewModel.productDesc.value)
        Assert.assertEquals(productResponse.price, viewModel.productPrice.value)
    }

    @Test
    fun buySuccess() {
        val buyProductCallbackCaptor = argumentCaptor<IProductRepository.BuyProductCallback>()

        val productId = "pixel3"
        val items = 3
        val productViewModel = ProductViewModel(repository)
        productViewModel.productId.value =  productId
        productViewModel.productItems.value = items.toString()

        productViewModel.buy()

        //驗證是否有呼叫IProductRepository.getProduct
        verify(repository).buy(eq(productId), eq(items), capture(buyProductCallbackCaptor))

        buyProductCallbackCaptor.value.onBuyResult(true)

        Assert.assertTrue(productViewModel.buySuccessText.value != null)
    }

    @Test
    fun buyFail() {
        val buyProductCallbackCaptor = argumentCaptor<IProductRepository.BuyProductCallback>()

        val productId = "pixel3"
        val items = 11
        val productViewModel = ProductViewModel(repository)
        productViewModel.productId.value = productId
        productViewModel.productItems.value = items.toString()

        productViewModel.buy()

        //驗證是否有呼叫IProductRepository.getProduct
        verify(repository).buy(eq(productId), eq(items), capture(buyProductCallbackCaptor))

        buyProductCallbackCaptor.value.onBuyResult(false)

        Assert.assertTrue(productViewModel.alertText.value != null)
    }
}