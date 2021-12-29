package com.carolmusyoka.mytaxi.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.carolmusyoka.mytaxi.R
import com.carolmusyoka.mytaxi.data.model.Poi
import com.carolmusyoka.mytaxi.databinding.ListItemVehiclesBinding

class VehicleListAdapter(private val poiList: List<Poi>, private var clickListener: ItemClickListener)
    : RecyclerView.Adapter<VehicleListAdapter.VehicleVH>(){

    private lateinit var binding: ListItemVehiclesBinding
    inner class VehicleVH(itemView: ListItemVehiclesBinding): RecyclerView.ViewHolder(itemView.root)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VehicleListAdapter.VehicleVH {
        binding = ListItemVehiclesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VehicleVH(binding)
    }

    override fun onBindViewHolder(holder: VehicleListAdapter.VehicleVH, position: Int) {
        val item = poiList[position]
        holder.itemView.apply {
            binding.loc.text = item.coordinate.toString()
            binding.vehicleName.text = item.fleetType

            if (item.fleetType == "TAXI"){
                Glide.with(binding.vehicleImage.context)
                    .load("")
                    .centerCrop()
                    .placeholder(R.drawable.taxi)
                    .into(binding.vehicleImage)
            } else{
                Glide.with(binding.vehicleImage.context)
                    .load("")
                    .centerCrop()
                    .placeholder(R.drawable.pooling)
                    .into(binding.vehicleImage)
            }
        }
        binding.mainCl.setOnClickListener {
            clickListener.onCardClick(item)
        }
    }

    override fun getItemCount(): Int {
        return poiList.size
    }

}
interface ItemClickListener{
    fun onCardClick(poi: Poi)
}