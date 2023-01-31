package me.mengxiaolin.tidytracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import me.mengxiaolin.tidytracker.databinding.FragmentMainBinding
import me.mengxiaolin.tidytracker.mainview.MainViewItemAdapter

/**
 * The main fragment that present the list of items.
 */
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rv = binding.itemsRecyclerView
        val adapter = MainViewItemAdapter(
            appContext = requireContext().applicationContext,
            owner = viewLifecycleOwner)
        adapter.onItemClickedListener = { item ->
            findNavController().navigate(R.id.action_MainFragment_to_DetailsFragment, bundleOf(
                "item_id" to item.id
            ))
        }
        rv.adapter = adapter

        val fab = binding.fab
        fab.setOnClickListener { _ ->
            findNavController().navigate(R.id.action_MainFragment_to_DetailsFragment, bundleOf(
                "item_id" to 0
            ))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}