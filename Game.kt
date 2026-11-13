//Ficheiro com a logica do jogo inteiro, junta todas as funcionalidades da nave e dos tiros no jogo em si
import pt.isel.canvas.*

const val WIDTH = 700
const val HEIGHT = 500
const val FRAME_TIME = 15 //70 frames por segundo, 1000 / 70 = 14/15
const val ALIEN_SHOT_INTERVAL = 250  //os tiros dos aliens são criados num intervalo de 250 ms
const val FONT_SIZE = 32

data class Area (val width: Int, val height: Int)

data class Game(
    val area: Area,
    val alienShots: List<Shot>,
    val ship: SpaceShip,
    val over: Boolean
)

//Funçao que retorna uma copia do jogo com a nave a disparar
fun Game.fire(): Game {
    return if (this.over) this //se a condição se o jogo acabar for ativada, retorna o jogo sem mudanças para não ser possível disparar mais a nave depois de o jogo acaba
    else
        this.copy(ship = ship.fire())
}

//Função que retorna uma copia do jogo com a nave em movimento
fun Game.moveShip(x: Int): Game {
    return if (this.over) this   //se a condição se o jogo acabar for ativada, retorna o jogo sem mudanças para não ser possível mover mais a nave depois de o jogo acaba
    else
        this.copy(ship = ship.boundary(x, area.width))
}

//Função que criar os tiros dos aliens com base numa percentagem de 50%
fun Game.tryCreateAlienShot() : Game {
    val chance = 0..1       //os tiros têm 50% de aparecer, assim criar um int range que só vai entre dois valores
    return if (this.over) this         // se a condição se o jogo acabar for ativada, retorna o jogo sem mudanças para não serem criados mais tiros de alien. Apesar de não poderem ser vistos, criar mais depois do jogo ter acabado gasta recursos desnecessário
    else if (chance.random() == 0)   //ao aplicar .random() entre um 1 e 0, cria uma chance de 50/50, se for 0 cria um tiro de alien, c.c retorna o jogo sem mudar nada
        this.copy(alienShots = alienShots + createAlienShot(area))
    else
        this
}

//Função que dita o que acontece em cada frame do jogo, faz a logica da condição de acabar o jogo, faz a logica de apagar os tiros dos aliens, e retorna o todos os elementos a movimentarem-se
fun Game.loop(): Game {
    if (this.over) return this
    if (alienShots.any{ it.hitShip(ship) })   //se qualquer tiro dos aliens acertar na nave, o jogo acaba
        return this.copy(over = true)          //para fazer o jogo "congelar" por completo, retornar o jogo só com o over atualizado a true, assim nada de vai mexer na tela e as outras funções de disparo e movimentção de nave tambem têm checks para não funcionarem mais
    val movedShip = ship.moveShots(area)
    val movedAlienShot = alienShots.moveAll(area)
    val remainingAlienShots = movedAlienShot.filter{alien -> movedShip.shots.none {shipShot -> shipShot.hitShot(alien)}}   //apaga todos os tiros dos aliens acertados pela nave, ao filtrar qualquer tiro que foi acertado, ou seja deixando todos os outros
    return this.copy(ship = movedShip, alienShots = remainingAlienShots)
}

fun main() {
    onStart {
        val area = Area(WIDTH, HEIGHT)
        val arena = Canvas(area.width, area.height, BLACK)
        var game = Game(
            area = area,
            alienShots = emptyList(),
            ship = SpaceShip(pos = Position(arena.width / 2, SHIP_POS_Y), width = SHIP_WIDTH, height = SHIP_HEIGHT, color = GREEN, shots = emptyList()),
            over = false)
        arena.onMouseDown {
            game = game.fire()
        }
        arena.onKeyPressed { ke ->
            if (ke.char == ' ')  //disparar só quando clicar na barra de espaço
                game = game.fire()
        }
        arena.onMouseMove { me ->
            game = game.moveShip(me.x)
        }
        arena.onTimeProgress(ALIEN_SHOT_INTERVAL) {
            game = game.tryCreateAlienShot()
        }
        arena.onTimeProgress(FRAME_TIME) {
            game = game.loop()
            arena.drawGame(game)
        }
    }
    onFinish { }
}
