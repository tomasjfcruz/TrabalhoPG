//Este ficheiro desenha a nave, movimenta a nave com o mouse sem passar dos limites e faz a nave disparar com o movimento dos tiros
import pt.isel.canvas.*

const val SHIP_WIDTH = 50
const val SHIP_HEIGHT = 10
const val CANNON_WIDTH = 4
const val CANNON_HEIGHT = 5
const val SHIP_POS_Y = 450
const val SHIP_SHOT_SPEED = 4
data class Position(val x: Int, val y: Int)     //data class de uma posição generica
data class SpaceShip(val pos: Position, val width: Int, val height: Int, val color: Int, val shots : List<Shot>)  //data class da nave

//função para desenhar a nave
fun Canvas.drawShip(s: SpaceShip) {     //s de ship
    drawRect(s.pos.x, s.pos.y, s.width, s.height, s.color)      //desenhar a nave principal de acordo com o enunciado
    drawRect(s.pos.x + s.width / 2,s.pos.y - CANNON_HEIGHT, CANNON_WIDTH , CANNON_HEIGHT,YELLOW)    //desenhar o "canhao" em cima da nave, o seu x é +25 pois fica sempre no meio da nave(neste caso do rect verde) e menos 5 pois está diretamente em cima da nave, ou seja 5 unidades(o seu tamanho) acima da verde
}

//Função para a nave não passar dos limites do Canvas
//versão com CoerceIn
fun SpaceShip.boundary(mouseX: Int, maxWidth: Int): SpaceShip {    //maxWidth = 700
    val maxX = maxWidth - this.width                //o valor máximo que a nave pode ir até não passar do ecrã é o maximo do Canvas menos o seu tamanho
    return this.copy(pos = Position(mouseX.coerceIn(0,maxX),this.pos.y))   //o coerceIn faz com que se o valor for menor que o minimo(0) retorna o minimo, faz o mesmo para o maximo(maxX) ou retorna o valor em si ; como só é para modificar a pos usar copy pois tudo o resto é igual
}

//Função que faz a nave disparar, verifica também se realmente pode disparar
fun SpaceShip.fire() : SpaceShip  {
    //tiro na posição da nave, como o tiro aparece sempre em cima do canhao amarelo que está no meio da nave, o tiro tem que aparecer na metade da posição x da nave e em cima do canhao amarelo
    val shipShot = Shot(pos = Position(pos.x + this.width / 2,pos.y - CANNON_HEIGHT - SHOT_HEIGHT)  ,vel = Velocity(0, -SHIP_SHOT_SPEED), width = SHOT_WIDTH, height = SHOT_HEIGHT, color = WHITE)
    return if (shots.isEmpty() )  //se a lista de tiros está vazia, pode-se criar um tiro
        copy(shots = shots + shipShot)  //returnar uma copia da nave com um tiro adicionado na lista de tiros
    else   //se a lista de tiros não estiver vazia, significa que está um tiro em jogo então não se pode disparar, por isso retorna a nave sem mudaças
        this
}

//Função que coloca todas as função de logica de movimento e retirar dos limites, na lista de tiros da nave para ela poder disparar corretamente
fun SpaceShip.moveShots(area: Area): SpaceShip {
    return this.copy(shots = shots.moveAll(area))
}

