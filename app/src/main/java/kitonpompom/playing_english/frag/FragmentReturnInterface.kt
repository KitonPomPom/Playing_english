package kitonpompom.playing_english.frag

import kitonpompom.playing_english.entities.TaskListItem

interface FragmentReturnInterface {
    fun returnGame(newList: ArrayList<String>?, ItList: List<TaskListItem>)
}