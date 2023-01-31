package me.mengxiaolin.tidytracker.mainview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.launch
import me.mengxiaolin.tidytracker.data.AppDatabase
import me.mengxiaolin.tidytracker.data.WashableItem
import me.mengxiaolin.tidytracker.databinding.ContentMainviewItemBinding
import java.text.DateFormat
import java.util.*

class MainViewItemAdapter(appContext: Context, owner:LifecycleOwner): RecyclerView.Adapter<MainViewItemAdapter.ViewHolder>()  {
    private val viewModel: DbViewModel = DbViewModel(appContext)
    var onItemClickedListener: ((item: WashableItem) -> Unit)? = null
    init {
        viewModel.dataSet.observe(owner) {
            notifyDataSetChanged()
        }
    }

    class ViewHolder(val binding: ContentMainviewItemBinding):
        RecyclerView.ViewHolder(binding.root)

    class DbViewModel(appContext: Context): ViewModel() {
        val dataSet: MutableLiveData<List<WashableItem>> = MutableLiveData(listOf())
        init {
            val db = Room.databaseBuilder(
                appContext,
                AppDatabase::class.java, "appdb"
            ).build()
            viewModelScope.launch {
                dataSet.value = db.washableItemDao().getAll()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ContentMainviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = checkNotNull(viewModel.dataSet.value)[position]
        holder.binding.apply {
            title.text = item.name
            lastCleanTime.text = DateFormat.getDateInstance().format(Date(item.lastCleanTime))
            root.setOnClickListener {
                onItemClickedListener?.invoke(item)
            }
        }
    }

    override fun getItemCount(): Int = viewModel.dataSet.value?.size ?: 0
}