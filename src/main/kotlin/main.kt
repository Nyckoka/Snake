import pt.isel.canvas.*


const val GRID_WIDTH = 30
const val GRID_HEIGHT = 30
const val BACKGROUND_COLOR = WHITE

const val TILE_SIDE = 20
const val TRUE_WIDTH = GRID_WIDTH * TILE_SIDE
const val TRUE_HEIGHT = GRID_HEIGHT * TILE_SIDE
val GRID_X = 0 until GRID_WIDTH
val GRID_Y = 0 until GRID_WIDTH

const val UP = 'w'
const val LEFT = 'a'
const val DOWN = 's'
const val RIGHT = 'd'

val Directions = listOf(UP, LEFT, DOWN, RIGHT)

fun getStartingSnake() = Snake(RIGHT, listOf(Chunk(Position(GRID_WIDTH / 2, GRID_HEIGHT / 2))))

fun getMenuGame() = Game(snake = getStartingSnake(), apples = emptyList(), state = "menu", score = 0,
                             buttons = getButtons())

fun getStartingGame() = Game(snake = getStartingSnake(), apples = emptyList(), state = "playing", score = 0,
                             buttons = getButtons()).spawnApple()

fun Game.playButtonHovered() = state == "menu" && buttons.button("PLAY")!!.hovered
fun Game.playAgainButtonHovered() = state == "gameover" && buttons.button("PLAY AGAIN")!!.hovered


const val SOUND_ON = true

fun playSound(sound: String) {
    if(SOUND_ON) pt.isel.canvas.playSound(sound)
}

fun main() {
    onStart {
        loadSounds("snakeDeath", "eatSound")

        val arena = Canvas(TRUE_WIDTH, TRUE_HEIGHT, BACKGROUND_COLOR)
        var game = getMenuGame()

        var nextDir = RIGHT

        arena.onKeyPressed { ke ->
            if(ke.char in Directions){
                nextDir = ke.char
            }
            else if(ke.char == 'p'){
                if(game.state == "gameover") game = getStartingGame()
            }
        }

        arena.onMouseMove { me ->
            if(game.state == "menu"){
                game = game.copy(buttons = game.buttons.map { button -> button.hover(me) })
            }
            else if(game.state == "gameover"){
                game = game.copy(buttons = game.buttons.map { button -> button.hover(me) })
            }
        }

        arena.onMouseDown {
            if(game.playButtonHovered() || game.playAgainButtonHovered()){
                game = getStartingGame()
                arena.drawGame(game)
            }
        }

        arena.onTimeProgress(10){
            arena.drawGame(game)
        }

        arena.onTimeProgress(100){
            game = game.changeSnakeDir(nextDir)

            if(game.state == "playing"){
                game = game.move().collideWithApple().checkDeath()
                if(game.state == "gameover") playSound("snakeDeath")

                arena.drawGame(game)
            }
        }

        arena.onTimeProgress(5000){
            if(game.state == "playing") game = game.spawnApple()
        }
    }
    onFinish {

    }
}