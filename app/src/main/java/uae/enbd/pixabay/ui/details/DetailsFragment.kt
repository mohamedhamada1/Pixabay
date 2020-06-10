package uae.enbd.pixabay.ui.details

import android.os.Bundle

import android.view.View
import androidx.databinding.DataBindingComponent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import uae.enbd.pixabay.R
import uae.enbd.pixabay.databinding.DetailsFragmentBinding
import uae.enbd.pixabay.databinding.SearchFragmentBinding
import uae.enbd.pixabay.di.Injectable
import uae.enbd.pixabay.repository.AppExecutors
import uae.enbd.pixabay.ui.base.BaseFragment
import uae.enbd.pixabay.utils.FragmentDataBindingComponent
import javax.inject.Inject


class DetailsFragment : BaseFragment<DetailsFragmentBinding, DetailsViewModel>(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val params by navArgs<DetailsFragmentArgs>()
    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    val viewModel: DetailsViewModel
        get() = ViewModelProvider(this, viewModelFactory).get(DetailsViewModel::class.java)

    override fun getBindingComponent(): DataBindingComponent? {
         return dataBindingComponent
    }

    override val layoutId: Int
        get() = R.layout.details_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.let {
            it.item=params.hit
        }
    }

}