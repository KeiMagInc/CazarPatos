package com.kerlly.vizuete.cazarpatos

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RankingAdapter(private val dataSet: ArrayList<Player>) : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
    private val TYPE_HEADER : Int = 0

    class ViewHolderHeader(view : View) : RecyclerView.ViewHolder(view){
        val textViewPosicion: TextView = view.findViewById(R.id.textViewPosicion)
        val textViewPatosCazados: TextView = view.findViewById(R.id.textViewPatosCazados)
        val textViewUsuario: TextView = view.findViewById(R.id.textViewUsuario)

    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewPosicion: TextView
        val textViewPatosCazados: TextView
        val textViewUsuario: TextView
        val imageViewMedal: ImageView
        init {
            textViewPosicion = view.findViewById(R.id.textViewPosicion)
            textViewPatosCazados = view.findViewById(R.id.textViewPatosCazados)
            textViewUsuario = view.findViewById(R.id.textViewUsuario)
            imageViewMedal = view.findViewById(R.id.imageViewMedal)
        }
    }
    override fun getItemViewType(position: Int): Int {
        if(position == 0){
            return TYPE_HEADER
        }
        return 1
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == TYPE_HEADER){
            val header = LayoutInflater.from(parent.context).inflate(R.layout.ranking_list,parent,false)
            return ViewHolderHeader(header)
        }
        val header = LayoutInflater.from(parent.context).inflate(R.layout.ranking_list,parent,false)
        return ViewHolder(header)
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolderHeader){
            holder.textViewPosicion.text = "#"
            holder.textViewPosicion.paintFlags = holder.textViewPosicion.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            holder.textViewPosicion.setTextColor(holder.textViewPosicion.context.getColor( R.color.colorPrimaryDark))
            holder.textViewPatosCazados.paintFlags = holder.textViewPatosCazados.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            holder.textViewPatosCazados.setTextColor(holder.textViewPatosCazados.context.getColor( R.color.colorPrimaryDark))
            holder.textViewUsuario.paintFlags = holder.textViewUsuario.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            holder.textViewUsuario.setTextColor(holder.textViewUsuario.context.getColor( R.color.colorPrimaryDark))
        }
        else if (holder is ViewHolder) {
            holder.textViewPosicion.text = position.toString()
            holder.textViewPatosCazados.text = dataSet[position-1].huntedDucks.toString()
            holder.textViewUsuario.text = dataSet[position-1].username
        }

        when (position) {
            0 -> { // Primer lugar
                viewHolder.imageViewMedal.setImageResource(R.drawable.gold)
                viewHolder.imageViewMedal.visibility = View.VISIBLE
            }
            1 -> { // Segundo lugar
                viewHolder.imageViewMedal.setImageResource(R.drawable.silver)
                viewHolder.imageViewMedal.visibility = View.VISIBLE
            }
            2 -> { // Tercer lugar
                viewHolder.imageViewMedal.setImageResource(R.drawable.bronze)
                viewHolder.imageViewMedal.visibility = View.VISIBLE
            }
            else -> { // Del cuarto lugar en adelante
                viewHolder.imageViewMedal.visibility = View.GONE
            }
        }
    }
    override fun getItemCount() = dataSet.size + 1
}

