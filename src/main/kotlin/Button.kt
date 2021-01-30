import pt.isel.canvas.*

data class Text(val string : String, val color : Int, val fontSize : Int)

data class Button(val position: Position, val width: Int, val height: Int, val color: Int, val text : Text, val hovered : Boolean)

const val GREY = 0xc0c0c0

const val BUTTON_COLOR = WHITE
const val BUTTON_HOVER_COLOR = GREY
const val BUTTON_FONT_SIZE = 30

object PlayButton{
    const val COLOR = WHITE
    const val HOVER_COLOR = GREY
    const val FONT_SIZE = 30
}

fun Button.isHovered(me: MouseEvent) : Boolean =
    me.x in position.x .. position.x + width && me.y in position.y .. position.y + height

fun Button.hover(me: MouseEvent) = copy(hovered = isHovered(me))

fun Canvas.drawButton(button: Button){
    button.apply {
        drawRect(position.x, position.y, width, height, if(hovered) BUTTON_HOVER_COLOR else color)
        drawText(position.x, position.y + height - text.fontSize / 6, text.string, BLACK, text.fontSize)
    }
}



data class Menu(val buttons: List<Button>)

fun List<Button>.button(name : String) : Button?{
    forEach { if(it.text.string == name) return it }
    return null!!
}

const val PLAY_BUTTON_WIDTH = TRUE_WIDTH / 5 + 4
const val PLAY_BUTTON_HEIGHT = TRUE_HEIGHT / 12
const val PLAYAGAIN_BUTTON_WIDTH = TRUE_WIDTH / 2 + 4
const val PLAYAGAIN_BUTTON_HEIGHT = TRUE_HEIGHT / 12

fun getButtons() : List<Button>{
    val playButton = Button(Position(TRUE_WIDTH / 2 - PLAY_BUTTON_WIDTH / 2, TRUE_HEIGHT / 2),
                            PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT, RED,
                            Text("PLAY", BLACK, TRUE_WIDTH / 12), false)

    val playAgainButton = Button(Position(TRUE_WIDTH / 2 - PLAYAGAIN_BUTTON_WIDTH / 2, TRUE_HEIGHT / 2),
                            PLAYAGAIN_BUTTON_WIDTH, PLAYAGAIN_BUTTON_HEIGHT, RED,
                            Text("PLAY AGAIN", BLACK, TRUE_WIDTH / 12), false)

    return listOf(playButton, playAgainButton)
}