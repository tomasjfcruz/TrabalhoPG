//Ficheiro com as função para desenhar todos os elementos do jogo no Canvas
import pt.isel.canvas.*

const val GAME_OVER_X_POS = 80
const val GAME_OVER_Y_POS = 12

//Função que escreve a frase de "Game over" quando se perde o jogo
fun Canvas.drawGameOver() {
    val x = width / 2 - GAME_OVER_X_POS
    val y = height - GAME_OVER_Y_POS
    val txt = "Game over"
    val fontSize = FONT_SIZE
    drawText(x, y, txt,RED,fontSize)
}

//Função que desenha os elementos no Canvas
fun Canvas.drawGame(g: Game) {
    erase()
    drawShip(g.ship)   //desenhar a nave
    g.ship.shots.forEach { drawShot (it) } //desenhar os tiros da nave
    g.alienShots.forEach { drawShot (it) } //desenhar os aliens
    if (g.over)        //se a condição de o jogo acabar é ativada, escreve a frase "Game over" na tela
        drawGameOver()
}