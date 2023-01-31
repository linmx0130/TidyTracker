package me.mengxiaolin.tidytracker

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import kotlinx.coroutines.launch
import me.mengxiaolin.tidytracker.data.AppDatabase
import me.mengxiaolin.tidytracker.data.WashableItem
import me.mengxiaolin.tidytracker.databinding.FragmentDetailsBinding
import java.text.DateFormat
import java.time.Instant
import java.util.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val data: MutableLiveData<WashableItem> = MutableLiveData()
    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemId = requireArguments().getInt("item_id", 0)
        db = Room.databaseBuilder(
            requireContext().applicationContext,
            AppDatabase::class.java, "appdb"
        ).build()
        binding.apply {
            lastCleanTimeValuePickButton.isEnabled = false
            lastCleanTimeValuePickButton.setOnClickListener {
                lastCleanTimeValuePickButton.isEnabled = false
                val dialog = DatePickerDialog(requireContext())
                dialog.setOnDateSetListener { _, year, month, dayOfMonth ->
                    val calendar = Calendar.getInstance()
                    calendar.set(year, month, dayOfMonth)
                    data.value = checkNotNull(data.value).copy(lastCleanTime = calendar.time.time)
                }
                dialog.setOnDismissListener {
                    lastCleanTimeValuePickButton.isEnabled = true
                }
                dialog.show()
            }
            buttonSave.setOnClickListener {
                data.value?.let{
                    val savedValue = it.copy(name = nameEdittext.text.toString())
                    viewLifecycleOwner.lifecycleScope.launch {
                        if (itemId != 0) {
                            db.washableItemDao().update(savedValue)
                        } else {
                            db.washableItemDao().insert(savedValue)
                        }
                    }
                }
                findNavController().popBackStack()
            }
        }

        data.observe(this.viewLifecycleOwner) {value ->
            val lastCleanTimeString = DateFormat.getDateInstance().format(Date(value.lastCleanTime))
            binding.apply {
                nameEdittext.setText(value.name)
                lastCleanTimeValueTextview.text = lastCleanTimeString
                lastCleanTimeValuePickButton.isEnabled = true
            }
        }


        lifecycleScope.launch {
            if (itemId!=0) {
                val results = db.washableItemDao().getById(itemId)
                if (results.isNotEmpty()) {
                    data.value = results[0]
                }
            } else {
                data.value = WashableItem(0, "", Date().time, 10)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}