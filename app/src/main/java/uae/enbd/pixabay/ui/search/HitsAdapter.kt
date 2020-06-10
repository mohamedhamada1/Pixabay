package uae.enbd.pixabay.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import uae.enbd.pixabay.R
import uae.enbd.pixabay.databinding.HitCellBinding
import uae.enbd.pixabay.models.Hit

import uae.enbd.pixabay.repository.AppExecutors
import uae.enbd.pixabay.ui.base.common.DataBoundPagedListAdapter

class HitsAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val callback: ((Hit) -> Unit)?
) : DataBoundPagedListAdapter<Hit, HitCellBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Hit>() {
        override fun areItemsTheSame(oldItem: Hit, newItem: Hit): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Hit, newItem: Hit): Boolean {
            return false
        }
    }
) {
    override fun createBinding(parent: ViewGroup, viewType: Int): HitCellBinding {
        val binding = DataBindingUtil
            .inflate<HitCellBinding>(
                LayoutInflater.from(parent.context),
                R.layout.hit_cell,
                parent,
                false,
                dataBindingComponent
            )
        binding.root.setOnClickListener {
            binding.item.let {
                callback?.invoke(it!!)
            }
        }

        return binding
    }

    override fun bind(binding: HitCellBinding, item: Hit) {
        binding.item = item
    }


}