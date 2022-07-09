package kitonpompom.playing_english.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kitonpompom.playing_english.R
import kitonpompom.playing_english.databinding.ItemRcTransliteBinding
import kitonpompom.playing_english.entities.TaskListItem

class ShowTranslationRcAdapter(private val listenerShTr: ListenerShTr) : ListAdapter<TaskListItem, ShowTranslationRcAdapter.ItemHolder>(ItemComparatorShTr()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position),listenerShTr)
    }

    class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemRcTransliteBinding.bind(view)

        fun setData(note: TaskListItem, listenerShTr: ListenerShTr) = with(binding){
            tvEng.text = note.rightAnswer
            tvRus.text = note.word
        }

        companion object{
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_rc_translite, parent, false))
            }
        }
    }

    class ItemComparatorShTr: DiffUtil.ItemCallback<TaskListItem>(){
        override fun areItemsTheSame(oldItem: TaskListItem, newItem: TaskListItem): Boolean { //сравниваем id
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TaskListItem, newItem: TaskListItem): Boolean {// сравниваем весь контент
            return oldItem == newItem
        }
    }

    interface ListenerShTr

}