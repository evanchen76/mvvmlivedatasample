package evan.chen.tutorial.mvvmlivedatasample

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class ProductViewModelFactory(private val productRepository: IProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            return ProductViewModel(productRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
