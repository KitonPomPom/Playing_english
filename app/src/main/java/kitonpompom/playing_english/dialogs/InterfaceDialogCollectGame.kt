package kitonpompom.playing_english.dialogs

interface InterfaceDialogCollectGame {
    fun dialogStartQuizGame(numTask:Int, id:Int, lockCollect:Int, lockSnake:Int)
    fun dialogStartCollectGame(numTask:Int, id:Int, lockCollect:Int, lockSnake:Int)
    fun dialogStartSnakeGame(numTask:Int, id:Int, lockCollect:Int, lockSnake:Int)

}