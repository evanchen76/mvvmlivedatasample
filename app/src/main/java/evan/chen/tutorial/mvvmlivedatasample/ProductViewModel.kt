package evan.chen.tutorial.mvvmlivedatasample

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import evan.chen.tutorial.mvvmlivedatasample.api.ProductResponse
import android.view.View


class ProductViewModel(private val productRepository: IProductRepository) : ViewModel(){
    var productId: MutableLiveData<String> = MutableLiveData()
    var productName: MutableLiveData<String> = MutableLiveData()
    var productDesc: MutableLiveData<String> = MutableLiveData()
    var productPrice: MutableLiveData<Int> = MutableLiveData()
    var productItems: MutableLiveData<String> = MutableLiveData()

    var alertText: MutableLiveData<Event<String>> = MutableLiveData()
    var buySuccessText: MutableLiveData<Event<String>> = MutableLiveData()

    fun getProduct(productId: String) {
        this.productId.value = productId
        productRepository.getProduct(productId, object : IProductRepository.LoadProductCallback {
            override fun onProductResult(productResponse: ProductResponse) {
                productName.value = productResponse.name
                productDesc.value = productResponse.desc
                productPrice.value = productResponse.price
            }
        })
    }

    fun buy(){
        val productId = productId.value ?: ""
        val numbers = (productItems.value ?: "0").toInt()

        productRepository.buy(productId, numbers, object : IProductRepository.BuyProductCallback {
            override fun onBuyResult(isSuccess: Boolean) {
                if (isSuccess) {
                    buySuccessText.value = Event("購買成功")
                } else {
                    alertText.value = Event("購買失敗")
                }
            }
        })
    }
}


