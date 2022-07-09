package kitonpompom.playing_english.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kitonpompom.playing_english.R

class LetterTwoAdapterCollectWord(var letterTwoInterface: LetterTwoInterface): RecyclerView.Adapter <LetterTwoAdapterCollectWord.ItemHolder>() {
    val mainArray = ArrayList<String>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_rc_letter_two_collect_word, parent, false)
                return ItemHolder(view)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(mainArray[position], letterTwoInterface)
    }

    override fun getItemCount(): Int {
        return mainArray.size
    }

    class ItemHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        lateinit var textViewLetter : TextView
        lateinit var cardView : CardView
        fun setData (dataLetter : String, letterTwoInterface: LetterTwoInterface){
            textViewLetter = itemView.findViewById(R.id.tvItLeterTwo)
            textViewLetter.text = dataLetter
            cardView = itemView.findViewById(R.id.id_cardView)
            if(textViewLetter.text != "" ) cardView.visibility = View.VISIBLE
            else cardView.visibility = View.GONE
            itemView.setOnClickListener(){
                if(textViewLetter.text != ""){ // проверка для того что бы по пустому не нажималось
                letterTwoInterface.onClickItemViewLetterTwo(adapterPosition, textViewLetter.text.toString())
                }
            }
        }
    }

    fun updateAdapter(newList : ArrayList<String> ){ // функция обновляет адаптер
        mainArray.clear()
        mainArray.addAll(newList)
        notifyDataSetChanged()
    }

    interface LetterTwoInterface{
        fun onClickItemViewLetterTwo(pos: Int, letter: String)
        fun onClickItemViewLetterOne(letterOne: String)
    }
}