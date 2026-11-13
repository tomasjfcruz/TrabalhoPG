//Ficheiro para desenhar e criar os tiros(nave e alien), fazer a logica de movimentação, verifica se os tiros estão no limite e retira casos passarem dos limites
import pt.isel.canvas.*

const val SHOT_WIDTH = 4
const val SHOT_HEIGHT = 7
const val ALIEN_SHOT_MIN_SPEED = 1
const val ALIEN_SHOT_MAX_SPEED = 4
data class Velocity(val vx: Int, val vy: Int)
data class Shot(val pos: Position, val vel: Velocity, val width: Int, val height: Int, val color: Int)

fun Canvas.drawShot(sh : Shot){  //sh = shot
    drawRect(sh.pos.x, sh.pos.y, sh.width, sh.height, sh.color)
}

//Função que faz a logica do movimento dos tiros
fun Shot.move() : Shot {
    val newY = pos.y + vel.vy    //mover para baixo, ao adicionar a posição y com a velocidade, para cima basta inverter a velocidade
    return copy(pos = Position(pos.x, newY))   //retornar a nova posição
}

//Função que verifica se os tiros(nave e aliens) não ultrapassaram os limites do Canvas
fun Shot.isInBounds(area : Area) : Boolean {
    return pos.y + height > 0 && pos.y < area.height //retorna true se ainda tiverem dentro dos limites e false c.c
}

//Função que movimenta todos os tiros e retira-os da lista caso saiem dos limites
fun List<Shot>.moveAll(area : Area) : List<Shot> {
    return this.map {it.move()}.filter { it.isInBounds(area) }  //coloca todos os tiros a movimentar ao usar o .move e com o filter coloca retira todos os tiros que passaram dos limites, deixando só os que estão na tela
}

//Função que cria os tiros dos aliens numa posição x aleatória e com uma velocidade aleatória
fun createAlienShot(area: Area) : Shot {
    val x = (0..<area.width).random() //posição x aleatoria da tela, vai desde o incio ao fim do x do Canvas
    val y = -SHOT_HEIGHT   //os tiros dos aliens começam sempre no topo da tela, como é menos a altura do tamanho do tiro os tiors vao deslizar para baixo mais naturalmente inves de só aparecerem repentinamente na tela
    val vy = ALIEN_SHOT_MIN_SPEED..ALIEN_SHOT_MAX_SPEED   //a velociade vai entre 1 a 4 pixeis
    return Shot(pos = Position(x,y) ,vel = Velocity(0, vy.random()), width = SHOT_WIDTH, height = SHOT_HEIGHT, color = RED)
}




