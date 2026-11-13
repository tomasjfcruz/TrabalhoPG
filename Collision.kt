//Ficheiro que calcula e verifica as colisões dos tiros e com a nave durante o jogo

//Função que verifica se houve colisão entre tiros e a nave, usa dois "retangulos" genericos(A e B), para simular a nave e os tiros.
//Inves de calcular se aconteceu uma colisão direta, verifica os casos onde não há e se alguem caso for true não houve colisão.
fun collision(posA: Position, widthA: Int, heightA: Int,
              posB: Position, widthB: Int, heightB: Int): Boolean {
    val leftA = posA.x ; val rightA = posA.x + widthA ; val topA = posA.y ; val bottomA = posA.y + heightA
    val leftB = posB.x ; val rightB = posB.x + widthB ; val topB = posB.y ; val bottomB = posB.y + heightB
    //verificação de colisão, caso A esteja completamente à esquerda, direita, cima ou baixo de B não houve colisão, então se todos os casos forem false significa que houve colisão
    return !(rightA <= leftB || leftA >= rightB || bottomA <= topB || topA >= bottomB)
}

//Função auxiliar que verifica se houve colisão entre o tiro do alien a nave em si
fun Shot.hitShip(ship: SpaceShip): Boolean {
    return (collision(pos,width,height,ship.pos,ship.width,ship.height))
}

//Função auxiliar que verifica se houve colisão entre o tiro da nave e os tiros dos aliens
fun Shot.hitShot(other: Shot): Boolean {
    return (collision(pos,width,height,other.pos,other.width,other.height))
}