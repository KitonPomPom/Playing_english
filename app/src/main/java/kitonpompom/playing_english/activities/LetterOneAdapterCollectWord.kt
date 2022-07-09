package kitonpompom.playing_english.activities

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kitonpompom.playing_english.R

class LetterOneAdapterCollectWord(var letterOneInterface: LetterTwoAdapterCollectWord.LetterTwoInterface): RecyclerView.Adapter <LetterOneAdapterCollectWord.ItemHolder>() {
    val mainArray = ArrayList<String>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        when (mainArray.size) {
            1,2,3,4,5,6,7,8,9 ->{
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_rc_letter_collect_word, parent, false)
                return ItemHolder(view)
            }

            10 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_rc_letter_collect_word_10, parent, false)
                return ItemHolder(view)
            }
            11 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_rc_letter_collect_word_11, parent, false)
                return ItemHolder(view)
            }
            12 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_rc_letter_collect_word_12, parent, false)
                return ItemHolder(view)
            }
            13 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_rc_letter_collect_word_14, parent, false)
                return ItemHolder(view)
            }
            14 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_rc_letter_collect_word_14, parent, false)
                return ItemHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_rc_letter_collect_word, parent, false)
                return ItemHolder(view)
            }

        }
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(mainArray[position], letterOneInterface )
    }

    override fun getItemCount(): Int {
        return mainArray.size
    }

    class ItemHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        lateinit var textViewLetter : TextView
        fun setData (dataLetter : String, letterOneInterface: LetterTwoAdapterCollectWord.LetterTwoInterface){

            textViewLetter = itemView.findViewById(R.id.tvItLeter)
            textViewLetter.text = dataLetter

            itemView.setOnClickListener(){
                var tempLetterOne:String = textViewLetter.text.toString()
                letterOneInterface.onClickItemViewLetterOne(tempLetterOne)
            }
        }
    }

    fun updateAdapter(newList : ArrayList<String> ){ // функция обновляет адаптер
        mainArray.clear()
        mainArray.addAll(newList)
        notifyDataSetChanged()
    }
}