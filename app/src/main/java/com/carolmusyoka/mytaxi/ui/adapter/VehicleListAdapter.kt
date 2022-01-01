package com.carolmusyoka.mytaxi.ui.adapter

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.carolmusyoka.mytaxi.R
import com.carolmusyoka.mytaxi.data.model.Poi
import com.carolmusyoka.mytaxi.databinding.ListItemVehiclesBinding
import java.util.*

class VehicleListAdapter(private val context: Context, private val poiList: List<Poi>, private var clickListener: ItemClickListener)
    : RecyclerView.Adapter<VehicleListAdapter.VehicleVH>(){

    private val geocoder: Geocoder = Geocoder(context, Locale.getDefault())
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
        val addresses: List<Address> = geocoder.getFromLocation(
            item.coordinate.latitude,
            item.coordinate.longitude,
            1
        )
        val city: String = addresses[0].locality
        val state: String = addresses[0].adminArea
        val knownName: String = addresses[0].featureName
        holder.itemView.apply {
            binding.loc.text = "$city $state \n $knownName"
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