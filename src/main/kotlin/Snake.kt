import pt.isel.canvas.*

data class Position(val x: Int, val y : Int)

data class Chunk(val position: Position)


data class Snake(val direction : Char, val chunks : List<Chunk>)

const val SNAKE_COLOR = GREEN
const val VELOCITY = 1

fun Position.change(dir: Char) =
    when(dir){
        UP -> copy(y = y - VELOCITY)
        LEFT -> copy(x = x - VELOCITY)
        DOWN -> copy(y = y + VELOCITY)
        else -> copy(x = x + VELOCITY)
    }

fun Position.trueX() = x * TILE_SIDE
fun Position.trueY() = y * TILE_SIDE


fun Snake.changeDir(dir: Char) : Snake =
    copy(direction =
        if(dir == UP && direction != DOWN) UP
        else if(dir == LEFT && direction != RIGHT) LEFT
        else if(dir == DOWN && direction != UP) DOWN
        else if(dir == RIGHT && direction != LEFT) RIGHT
        else direction
    )

fun Snake.head() = chunks.first()
fun Snake.body() = chunks.drop(1)

fun Chunk.move(dir: Char) : Chunk = copy(position = position.change(dir))

fun Snake.movedHead() = head().move(direction)


fun Snake.addChunk() : Snake = copy(chunks = chunks + Chunk(Position(-50, -50)))


fun Snake.move() : Snake{
    val newChunks = mutableListOf<Chunk>()

    newChunks.add(movedHead())
    newChunks += body().mapIndexed { i, chunk -> chunk.copy(position = chunks[i].position) }

    return copy(chunks = newChunks)
}



fun Snake.collidesToDeath() : Boolean = collidesWithBody() || collidesWithWalls()


fun Snake.collidesWithBody() = body().any { chunk -> head().position == chunk.position }
fun Snake.collidesWithWalls() = !(head().position.x in GRID_X && head().position.y in GRID_Y)

object Tongue{
    const val HEIGHT = 10
    const val WIDTH = 3
    const val CENTER_CORRECTION = TILE_SIDE / 2 - WIDTH / 2
}

fun Canvas.drawSnakeTongue(snake: Snake) {
    Tongue.apply {
        val Y_ADD = when(snake.direction) { LEFT, RIGHT -> CENTER_CORRECTION; UP -> - HEIGHT ; else -> TILE_SIDE }
        val X_ADD = when(snake.direction){ UP, DOWN -> CENTER_CORRECTION; LEFT -> - HEIGHT; else -> TILE_SIDE }
        val tongue_width = if(snake.direction in listOf(UP, DOWN)) WIDTH else HEIGHT
        val tongue_height = if(snake.direction in listOf(RIGHT, LEFT)) WIDTH else HEIGHT

        drawRect(snake.head().position.trueX() + X_ADD, snake.head().position.trueY() + Y_ADD, tongue_width, tongue_height, MAGENTA)
    }
}

fun Canvas.drawSnakeEyes(snake: Snake){
    val eye1_X_ADD =
        when(snake.direction){
            UP, DOWN -> TILE_SIDE / 4 - 2
            LEFT -> TILE_SIDE / 4
            else -> 3 * TILE_SIDE / 4 - 2
        }
    val eye1_Y_ADD =
        when(snake.direction){
            UP -> TILE_SIDE / 4
            DOWN -> 3 * TILE_SIDE / 4 - 2
            LEFT -> TILE_SIDE / 4 - 2
            else -> TILE_SIDE / 4 - 2
        }
    val eye2_X_ADD =
        when(snake.direction){
            UP, DOWN -> 3 * TILE_SIDE / 4 - 2
            LEFT -> TILE_SIDE / 4
            else -> 3 * TILE_SIDE / 4 - 2
        }
    val eye2_Y_ADD =
        when(snake.direction){
            UP -> TILE_SIDE / 4
            DOWN -> 3 * TILE_SIDE / 4 - 2
            LEFT -> 3* TILE_SIDE / 4 - 2
            else -> 3* TILE_SIDE / 4 - 2
        }

    drawRect(snake.head().position.trueX() + eye1_X_ADD, snake.head().position.trueY() + eye1_Y_ADD, 5, 5, BLACK)
    drawRect(snake.head().position.trueX() + eye2_X_ADD, snake.head().position.trueY() + eye2_Y_ADD, 5, 5, BLACK)
}


fun Canvas.drawRectTile(position: Position, color: Int, thickness : Int = 0){
    drawRect(position.trueX(), position.trueY(), TILE_SIDE, TILE_SIDE, color, thickness)
}

const val CHUNK_BORDER = 3

fun Canvas.drawSnake(snake: Snake){
    drawSnakeTongue(snake)

    snake.chunks.forEach { chunk ->
        drawRectTile(chunk.position, SNAKE_COLOR)
        drawRectTile(chunk.position, BLACK, CHUNK_BORDER)
    }

    drawSnakeEyes(snake)
}