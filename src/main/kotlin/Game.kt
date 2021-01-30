import pt.isel.canvas.*

data class Game(val snake : Snake, val apples: List<Apple>, val state : String, val score : Int,
                val buttons : List<Button>)

fun Game.collideWithApple() : Game {
    apples.forEach { apple ->
        if (snake.head().position == apple.position) {
            playSound("eatSound")
            return copy(snake = snake.addChunk(), apples = apples - apple, score = score + 1)
        }
    }
    return this
}

fun Game.changeSnakeDir(dir: Char) = copy(snake = snake.changeDir(dir))

fun Game.move() = copy(snake = snake.move())

fun Game.checkDeath() = copy(state = if(snake.collidesToDeath()) "gameover" else "playing")

fun Canvas.drawGame(game: Game){
    erase()

    when(game.state) {
        "menu" -> {
            drawText(TRUE_WIDTH / 3 - TRUE_WIDTH / 8 - 3, TRUE_HEIGHT / 3 + 2, "Snake", BLACK, TRUE_WIDTH / 5)
            drawText(TRUE_WIDTH / 3 - TRUE_WIDTH / 8, TRUE_HEIGHT / 3, "Snake", GREEN, TRUE_WIDTH / 5)

            drawButton(game.buttons.button("PLAY")!!)
        }
        "playing" -> {
            game.apples.forEach { drawAppleLeaf(it) }
            game.apples.forEach { drawApple(it) }

            drawSnake(game.snake)

            //Draw Score
            drawText(TRUE_WIDTH / 2, TILE_SIDE, game.score.toString(), BLACK, 20)
        }
        "gameover" -> {
            //Draw Game Over
            drawText(TRUE_WIDTH / 3 - TRUE_WIDTH / 8 - 3, TRUE_HEIGHT / 3 + 2, "Game over!", BLACK, GRID_WIDTH * 2)
            drawText(TRUE_WIDTH / 3 - TRUE_WIDTH / 8, TRUE_HEIGHT / 3, "Game over!", RED, GRID_WIDTH * 2)

            drawButton(game.buttons.button("PLAY AGAIN")!!)
        }
    }
}