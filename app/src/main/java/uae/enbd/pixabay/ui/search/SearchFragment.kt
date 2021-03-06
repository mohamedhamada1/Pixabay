package uae.enbd.pixabay.ui.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingComponent
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.search_fragment.*
import uae.enbd.pixabay.BuildConfig
import uae.enbd.pixabay.R
import uae.enbd.pixabay.databinding.SearchFragmentBinding
import uae.enbd.pixabay.di.Injectable
import uae.enbd.pixabay.repository.AppExecutors
import uae.enbd.pixabay.repository.Status
import uae.enbd.pixabay.ui.base.BaseFragment
import uae.enbd.pixabay.ui.details.DetailsFragmentArgs
import uae.enbd.pixabay.utils.FragmentDataBindingComponent
import uae.enbd.pixabay.utils.isNetworkAvailable
import javax.inject.Inject


class SearchFragment : BaseFragment<SearchFragmentBinding, SearchViewModel>(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var adapter: HitsAdapter? = null

    val viewModel: SearchViewModel
        get() = ViewModelProvider(this, viewModelFactory).get(SearchViewModel::class.java)


    override val layoutId: Int
        get() = R.layout.search_fragment


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.listIsEmpty()) {
            input.setText("apple")
            doSearch()
        }
        initAdapter()
        initSearchInputListener()
        getState()
        pullToRefresh()

    }


    private fun doSearch() = viewDataBinding.apply {
        // Dismiss keyboard
        hideKeyboard()
        viewModel.research(query = input.text.toString())
    }

    private fun initAdapter() = viewDataBinding.apply {
        if (adapter == null) {
            initRecyclerView()
            if (adapter == null)
                adapter = HitsAdapter(
                    dataBindingComponent,
                    appExecutors
                ) { item ->
                    findNavController().navigate(
                        SearchFragmentDirections.actionSearchFragmentToDetailsFragment(item)
                    )
                }

            RVHits.adapter = adapter
            viewModel.loadData.observe(viewLifecycleOwner, Observer { result ->
                adapter?.submitList(result)
                swipToRefresh?.isRefreshing = false


            })

        } else {
            initRecyclerView()
            RVHits.adapter = adapter
        }
    }

    private fun initRecyclerView() = viewDataBinding?.RVHits?.apply {
        setHasFixedSize(true)
    }

    private fun pullToRefresh() = viewDataBinding?.swipToRefresh?.apply {
        setOnRefreshListener {
            viewModel.invalidate()
        }

    }

    private fun getState() {
        viewModel.getState().observe(viewLifecycleOwner, Observer { state ->
            viewDataBinding?.let {
                it.loadingStatus = state.status
                if (state.status == Status.ERROR) {
                    viewModel.changeLiveData(false)
                    viewModel.invalidate()
                } else if (state.status == Status.SUCCESS) {
                    emptyView.visibility =
                        if (adapter?.itemCount ?: 0 > 0) View.GONE else View.VISIBLE
                }
            }
        })
    }

    // handle keybad click
    private fun initSearchInputListener() = viewDataBinding.apply {
        input.setOnEditorActionListener { view: View, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch()
                true
            } else {
                false
            }
        }
        input.setOnKeyListener { view: View, keyCode: Int, event: KeyEvent ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                doSearch()
                true
            } else {
                false
            }
        }
        btbDone.setOnClickListener {
            doSearch()
        }

    }

}