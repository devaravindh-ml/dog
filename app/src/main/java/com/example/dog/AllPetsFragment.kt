package com.example.dog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dog.R // Import R class for resource access

class AllPetsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout containing the RecyclerView
        // NOTE: This assumes you have created res/layout/fragment_pet_list.xml
        val view = inflater.inflate(R.layout.fragment_pet_list, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

        // 1. Create Dummy Data (In a real application, this would come from an API or database)
        val petList = createDummyPetList()

        // 2. Setup the RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Use the adapter created previously
        recyclerView.adapter = PetListAdapter(petList)

        // Optional: Hide empty state if there is data
        // You would typically manage an empty state TextView visibility here

        return view
    }

    // Helper function to generate example data
    private fun createDummyPetList(): List<Pet> {
        return listOf(
            Pet("1", "Buddy", "Golden Retriever", "Wearing a red collar.", "url1", "Central Park, NY", PetStatus.LOST),
            Pet("2", "Whiskers", "Tabby Cat", "Found wandering near the library.", "url2", "Downtown Library", PetStatus.FOUND),
            Pet("3", "Max", "Labrador", "Lost since Tuesday, reward offered.", "url3", "Forest Hills, CA", PetStatus.LOST),
            Pet("4", "Leo", "Pomeranian", "Found running loose in the neighborhood.", "url4", "Main Street Park", PetStatus.FOUND)
        )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of this fragment.
         */
        fun newInstance() = AllPetsFragment()
    }
}