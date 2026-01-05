package com.example.dog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.dog.databinding.ActivityLostfoundBinding // CRITICAL FIX: Package name corrected
import com.google.android.material.tabs.TabLayoutMediator

// ===================================================================
// MAIN ACTIVITY
// ===================================================================

class LostFoundActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLostfoundBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLostfoundBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
        setupListeners()

        binding.tvEmptyState.visibility = View.GONE
    }

    private fun setupViewPager() {
        val tabTitles = listOf("All Listings", "Lost Pets", "Found Pets")

        // Pass 'this' (the AppCompatActivity) as the lifecycle owner to the adapter
        binding.viewPagerContent.adapter = LostFoundPagerAdapter(this)

        TabLayoutMediator(binding.tabLayoutFilter, binding.viewPagerContent) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    private fun setupListeners() {
        // FAB Listener
        binding.fabReport.setOnClickListener {
            Toast.makeText(this, "Opening Report Pet Screen...", Toast.LENGTH_SHORT).show()
        }

        // Filter Icon Listener
        binding.searchLayout.setEndIconOnClickListener {
            Toast.makeText(this, "Showing advanced Filter options", Toast.LENGTH_SHORT).show()
        }

        // Search Submission Listener
        binding.etSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH || event?.keyCode == android.view.KeyEvent.KEYCODE_ENTER) {
                val query = v.text.toString()
                Toast.makeText(this, "Searching for: $query", Toast.LENGTH_SHORT).show()
                return@setOnEditorActionListener true
            }
            false
        }
    }
}

// ===================================================================
// PAGER ADAPTER
// ===================================================================

/**
 * FragmentStateAdapter for the ViewPager2.
 */
private class LostFoundPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllPetsFragment.newInstance()
            1 -> LostPetsFragment.newInstance()
            2 -> FoundPetsFragment.newInstance()
            else -> throw IllegalStateException("Invalid tab position: $position")
        }
    }
}

// ===================================================================
// FRAGMENT IMPLEMENTATIONS
// ===================================================================

// NOTE: These fragments require a resource file named 'fragment_pet_list.xml'
// containing a RecyclerView with the ID 'rvPetList' to run without crashing.

/**
 * Fragment for "All Listings" tab.
 */
class AllPetsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Replace R.layout.fragment_pet_list with your actual layout file name
        return inflater.inflate(R.layout.fragment_pet_list, container, false)
    }

    companion object {
        fun newInstance() = AllPetsFragment()
    }
}

/**
 * Fragment for "Lost Pets" tab.
 */
class LostPetsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Replace R.layout.fragment_pet_list with your actual layout file name
        return inflater.inflate(R.layout.fragment_pet_list, container, false)
    }

    companion object {
        fun newInstance() = LostPetsFragment()
    }
}

/**
 * Fragment for "Found Pets" tab.
 */
class FoundPetsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Replace R.layout.fragment_pet_list with your actual layout file name
        return inflater.inflate(R.layout.fragment_pet_list, container, false)
    }

    companion object {
        fun newInstance() = FoundPetsFragment()
    }
}