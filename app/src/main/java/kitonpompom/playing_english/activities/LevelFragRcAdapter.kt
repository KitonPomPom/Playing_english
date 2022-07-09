package kitonpompom.playing_english.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kitonpompom.playing_english.R
import kitonpompom.playing_english.databinding.ItemRcFraglvlaBinding
import kitonpompom.playing_english.entities.LevelListItem

class LevelFragRcAdapter(private val listener: Listener): ListAdapter<LevelListItem, LevelFragRcAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position),listener)
    }

    class ItemHolder(view: View) : RecyclerView.ViewHolder(view){
        private val binding = ItemRcFraglvlaBinding.bind(view)
        val context = view.context
        fun setData(note: LevelListItem, listener: Listener) = with(binding){
            if(note.lock == 0) {
                tvNameLavel.setTextColor(ContextCompat.getColor(context, R.color.grey))
            }else{tvNameLavel.setTextColor(ContextCompat.getColor(context, R.color.white))
                idStarVik.visibility = View.VISIBLE
            }

            linLayCloseRecRViss.isVisible = note.lock == 0
            imLockVis.isVisible = note.lock == 0

            if (note.typedSuccess == 15){
                linLayRecVinVis.visibility = View.VISIBLE
            }else{
                linLayRecVinVis.visibility = View.GONE
            }

            if (note.lockCollect == 1){
                idStarCollect.visibility = View.VISIBLE
            }else{
                idStarCollect.visibility = View.GONE
            }

            if (note.lockSnake == 1){
                idStarSnake.visibility = View.VISIBLE
            }else{
                idStarSnake.visibility = View.GONE
            }


            tvNameLavel.text = note.nameLevel
            tvNap.text = note.typedSuccess.toString()
            tvUgad.text = note.selectedSuccess.toString()

            btStartGame.setOnClickListener(){
                listener.openFragGame(note.numTask!!, note.id!!.toInt(), note.nameLevel, note.lockCollect, note.lockSnake)
            }

            btTranscliteFrag.setOnClickListener(){
                listener.openFragTranslite(note.numTask, note.id!!.toInt(), note.nameLevel, note.lockCollect, note.lockSnake )
            }

        }
        companion object{
            fun create(parent: ViewGroup): ItemHolder{
            return ItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_rc_fraglvla, parent, false))
            }
        }
    }

    class ItemComparator: DiffUtil.ItemCallback<LevelListItem>(){
        override fun areItemsTheSame(oldItem: LevelListItem, newItem: LevelListItem): Boolean { //сравниваем id
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LevelListItem, newItem: LevelListItem): Boolean {// сравниваем весь контент
           return oldItem == newItem
        }
    }

    interface Listener{
        fun openFragGame(numTask: Int, id: Int, nameLevel:String, lockCollect:Int, lockSnake:Int)
        fun openFragTranslite(numTask: Int, id: Int, nameLevel: String, lockCollect: Int, lockSnake: Int)
    }


}