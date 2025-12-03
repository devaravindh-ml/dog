package com.example.dog

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.example.dog.databinding.ActivityLostFoundBinding
// ⭐ NEW IMPORT: Import the generated binding class for the item layout ⭐
import com.example.dog.databinding.ItemLostFoundBinding

class LostFoundActivity : AppCompatActivity() {

    // 1. View Binding setup
    private lateinit var binding: ActivityLostFoundBinding
    private lateinit var adapter: LostFoundAdapter

    // Dummy data source
    // NOTE: R.drawable.pet_max, etc., must exist in your res/drawable folder.
    private val allListings = listOf(
        PetListing("Max", "Golden Retriever", "Lost", "New York", R.drawable.pet_max),
        PetListing("Luna", "Maine Coon", "Found", "California", R.drawable.pet_luna),
        PetListing("Buddy", "Beagle", "Lost", "Texas", R.drawable.pet_buddy),
        PetListing("Milo", "Mixed", "Found", "Florida", R.drawable.pet_milo)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivityLostFoundBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupTabLayout()
        setupFab()

        // Initial load
        filterListings("All")
    }

    /**
     * Sets up the RecyclerView with a dummy adapter and layout manager.
     */
    private fun setupRecyclerView() {
        adapter = LostFoundAdapter(allListings) // Start with all data
        binding.rvLostFound.layoutManager = LinearLayoutManager(this)
        binding.rvLostFound.adapter = adapter
    }

    /**
     * Sets up the TabLayout listener to handle filtering.
     */
    private fun setupTabLayout() {
        binding.tabLayoutFilter.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // Get the text from the selected tab and filter the list
                val filter = tab.text.toString()
                filterListings(filter)
                Log.d("LostFoundActivity", "Selected tab: $filter")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Re-filtering on re-selection might be desired (e.g., to refresh/scroll to top)
                tab?.text?.toString()?.let { filterListings(it) }
            }
        })
    }

    /**
     * Filters the RecyclerView based on the selected tab.
     */
    private fun filterListings(filter: String) {
        val filteredList = when (filter) {
            "Lost" -> allListings.filter { it.status == "Lost" }
            "Found" -> allListings.filter { it.status == "Found" }
            else -> allListings // "All" or any other case
        }

        adapter.updateList(filteredList)

        // Check for empty state and update UI visibility
        if (filteredList.isEmpty()) {
            binding.rvLostFound.visibility = View.GONE
            binding.tvEmptyState.visibility = View.VISIBLE
        } else {
            binding.rvLostFound.visibility = View.VISIBLE
            binding.tvEmptyState.visibility = View.GONE
        }
    }

    /**
     * Sets up the Floating Action Button click listener.
     */
    private fun setupFab() {
        binding.fabReport.setOnClickListener {
            Toast.makeText(this, "Opening Report Pet Screen...", Toast.LENGTH_SHORT).show()
            // In a real app, you would navigate to a new activity/fragment here.
            // Example: startActivity(Intent(this, ReportPetActivity::class.java))
        }
    }
}

// ----------------------------------------------------------------------------------

/**
 * Simple data class to represent a Pet Listing.
 */
data class PetListing(
    val name: String,
    val breed: String,
    val status: String, // "Lost" or "Found"
    val location: String,
    val imageResId: Int // Placeholder image resource ID
)

/**
 * Updated Adapter for the RecyclerView to inflate and bind item_lost_found.xml.
 */
class LostFoundAdapter(private var listings: List<PetListing>) :
    RecyclerView.Adapter<LostFoundAdapter.ViewHolder>() {

    // ⭐ 1. Updated ViewHolder class using ItemLostFoundBinding ⭐
    inner class ViewHolder(private val binding: ItemLostFoundBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pet: PetListing) {
            // Set text content
            binding.tvPetNameBreed.text = "${pet.name} (${pet.breed})"
            binding.tvStatusLocation.text = "${pet.status} in ${pet.location}"

            // Set image
            binding.ivPetPhoto.setImageResource(pet.imageResId)

            // Set exclusive color based on status for better visual distinction
            val color = if (pet.status == "Lost")
            // Using a vivid orange/red for Lost
                Color.parseColor("#FF5722")
            else
            // Using a green for Found (joyful reunion)
                Color.parseColor("#4CAF50")

            binding.tvStatusLocation.setTextColor(color)
        }
    }

    // ⭐ 2. Inflate the new layout using binding in onCreateViewHolder ⭐
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLostFoundBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    // ⭐ 3. Bind the data using the bind function in onBindViewHolder ⭐
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listings[position])
    }

    override fun getItemCount() = listings.size

    /**
     * Updates the list and notifies the adapter to refresh the UI.
     */
    fun updateList(newList: List<PetListing>) {
        listings = newList
        notifyDataSetChanged()
    }
}