package kitonpompom.playing_english.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kitonpompom.playing_english.R

class SnakeAdapterWord(var snakeInterface: SnakeInterface): RecyclerView.Adapter <SnakeAdapterWord.ItemHolder>() {
    val mainArray = ArrayList<String>()
    val mainArrayPosVis = ArrayList<Int>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_rc_snake_word, parent, false)
                return ItemHolder(view)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(mainArray[position],mainArrayPosVis[position], snakeInterface)
    }

    override fun getItemCount(): Int {
        return mainArray.size
    }

    class ItemHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        lateinit var textViewLetter : TextView
        lateinit var cardView : CardView
        val context = itemView.context

        fun setData (dataLetter : String, dataPosVis: Int, snakeInterface: SnakeInterface){

            textViewLetter = itemView.findViewById(R.id.tvItLeterSnake)
            cardView = itemView.findViewById(R.id.id_cardView)
            textViewLetter.text = dataLetter

            if(dataPosVis == 0){
                cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.grey_white))
            }else{
                cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.green))
                snakeInterface.addLetterTextSnakeItem(textViewLetter.text.toString())
            }
        }
    }

    fun updateAdapter(newList : ArrayList<String>, newListPosVis: ArrayList<Int> ){ // функция обновляет адаптер
        mainArrayPosVis.clear()
        mainArrayPosVis.addAll(newListPosVis)
        mainArray.clear()
        mainArray.addAll(newList)
        notifyDataSetChanged()
    }

    fun updateItem(position: Int){
        mainArrayPosVis[position] = 1
        notifyItemChanged(position)
    }

    fun updateAllArrayPosVis(newListPosVis: ArrayList<Int>){
        mainArrayPosVis.clear()
        mainArrayPosVis.addAll(newListPosVis)
        notifyDataSetChanged()
    }

    interface SnakeInterface{
        fun addLetterTextSnakeItem(letter:String)
    }
}