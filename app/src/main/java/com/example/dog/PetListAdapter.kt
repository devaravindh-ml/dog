// PetListAdapter.kt
package com.example.dog

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide // NOTE: You should use a library like Glide or Picasso for image loading
import com.example.dog.R

class PetListAdapter(private val petList: List<Pet>) :
    RecyclerView.Adapter<PetListAdapter.PetViewHolder>() {

    // --- 1. ViewHolder Definition ---
    class PetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivPetImage: ImageView = view.findViewById(R.id.ivPetImage)
        val tvStatusTag: TextView = view.findViewById(R.id.tvStatusTag)
        val tvPetName: TextView = view.findViewById(R.id.tvPetName)
        val tvPetLocation: TextView = view.findViewById(R.id.tvPetLocation)

        // Optional: Interface for click handling
        // init { view.setOnClickListener { listener.onItemClick(adapterPosition) } }
    }

    // --- 2. Create the ViewHolder ---
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_pet, parent, false)
        return PetViewHolder(view)
    }

    // --- 3. Bind Data to Views ---
    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val pet = petList[position]

        // Load Image (Requires Glide/Picasso dependency in build.gradle)
        Glide.with(holder.itemView.context)
            .load(pet.imageUrl)
            .placeholder(R.drawable.placeholder_dog) // Use a temporary placeholder drawable
            .into(holder.ivPetImage)

        // Set Text Fields
        holder.tvPetName.text = pet.name
        holder.tvPetLocation.text = pet.location

        // Handle Status Tag (CRITICAL: Shows LOST/FOUND distinction)
        holder.tvStatusTag.text = pet.status.name

        when (pet.status) {
            PetStatus.LOST -> {
                holder.tvStatusTag.setBackgroundResource(R.drawable.rounded_status_background_lost)
                holder.tvStatusTag.text = "LOST"
            }
            PetStatus.FOUND -> {
                holder.tvStatusTag.setBackgroundResource(R.drawable.rounded_status_background_found)
                holder.tvStatusTag.text = "FOUND"
            }
        }
    }

    // --- 4. Get Item Count ---
    override fun getItemCount() = petList.size
}