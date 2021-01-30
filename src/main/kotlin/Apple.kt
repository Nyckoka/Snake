import pt.isel.canvas.*

data class Apple(val position: Position)

fun generatePositions() : List<Position>{
    val positions = mutableListOf<Position>()

    for(x in GRID_X){
        for(y in GRID_Y){
            positions.add(Position(x, y))
        }
    }

    return positions
}

val positions = generatePositions()

fun List<Chunk>.positions() = map { it.position }

const val APPLE_COLOR = RED
const val APPLE_LEAF_STALK_COLOR = 0x586e45
const val APPLE_LEAF_COLOR = GREEN

fun Canvas.drawAppleLeaf(apple: Apple){
    drawRect(apple.position.trueX() + TILE_SIDE / 2 - 1, apple.position.trueY() - 6, 3, 6, APPLE_LEAF_STALK_COLOR)
    drawRect(apple.position.trueX() + TILE_SIDE / 2 - 1, apple.position.trueY() - 8, 3, 4, APPLE_LEAF_COLOR)
}
fun Canvas.drawApple(apple: Apple) {
    drawRectTile(apple.position, APPLE_COLOR)
}

fun Game.spawnApple() : Game{
    val randomPosition = (positions - snake.chunks.positions() - apples.map { it.position }).random()

    return copy(apples = apples + Apple(randomPosition))
}