public class StudentPlayer extends Player{
    public StudentPlayer(int playerIndex, int[] boardSize, int nToConnect) {
        super(playerIndex, boardSize, nToConnect);
    }

    public int evaluation(Board board){
        //eset szetvalasztas
        //ha szerzek pontot +
        //ha enemy szerez -
        //4,3+ures,2+2 ures egy sorban
        int score=0;
        int[][] table=board.getState();
        //sorba
        for(int i=0;i<6;i++){
            for(int j=0;j<4;j++){
                //sorba
                Quadron q=new Quadron(table[i][j],table[i][j+1],table[i][j+2],table[i][j+3]);
                score +=q.score(this.playerIndex);
            }
        }
        //oszlopba
        for(int i=0;i<3;i++){
            for(int j=0;j<7;j++){
                Quadron q=new Quadron(table[i][j],table[i+1][j],table[i+2][j],table[i+3][j]);
                score +=q.score(this.playerIndex);
            }
        }
        // "\" atlo
        for(int i=0;i<3;i++){
            for(int j=0;j<4;j++){
                Quadron q=new Quadron(table[i][j],table[i+1][j+1],table[i+2][j+2],table[i+3][j+3]);
                score +=q.score(this.playerIndex);
            }
        }
        for(int i=5;i>2;i--){
            for(int j=0;j<4;j++){
                Quadron q=new Quadron(table[i][j],table[i-1][j+1],table[i-2][j+2],table[i-3][j+3]);
                score +=q.score(this.playerIndex);
            }

        }
        return score;


    }

    public Pair minimax(Board board, int depth, int alfa, int beta, boolean maxPlayer){
        if (depth == 0 || board.gameEnded())
            return new Pair(-1,evaluation(board));//innen indulunk felfele


        if (maxPlayer) {
            Pair maxPair=new Pair(-1,-9999999);
            for(int i=0;i<7;i++){
                Board child=new Board(board);
                if(child.stepIsValid(i)){
                    child.step(this.playerIndex,i);
                    Pair pair=minimax(child,depth-1,alfa,beta,false);
                    //ertekadas ha van optimalisabb
                    if(pair.score > maxPair.score) {
                        maxPair.score = pair.score;
                        maxPair.oszlop =i;
                    }
                    alfa = Math.max(alfa,pair.score);

                    if (beta <= alfa)
                        break;
                }
            }
            return maxPair;
        }
        else {
            Pair minPair = new Pair(-1,9999999);
            for(int i=0;i<7;i++){
                Board child=new Board(board);
                if(child.stepIsValid(i)) {
                    child.step(this.playerIndex-1, i);

                    Pair pair = minimax(child, depth - 1, alfa, beta, true);
                    //ertekadas ha van optimalisabb
                    if(pair.score < minPair.score){
                        minPair.score=pair.score;
                        minPair.oszlop=i;
                    }
                    beta = Math.min(beta, pair.score);
                    if (beta <= alfa)
                        break;
                }
            }
            return minPair;
        }
    }



    @Override
    public int step(Board board) {
       Pair pair = minimax(board,7,-9999999,9999999,true);
       //System.out.println(evaluation(board));
       return pair.oszlop;
    }
    //segedosztaly az oszlopokhoz
    //
    public class Pair {
        private int oszlop;
        private int score;

        public Pair(int first, int second) {
            this.oszlop = first;
            this.score = second;
        }

        public int getOszlop() {
            return oszlop;
        }

        public int getScore() {
            return score;
        }

        public void setOszlop(int oszlop) {
            this.oszlop = oszlop;
        }

        public void setScore(int score) {
            this.score = score;
        }

        @Override
        public String toString() {
            return "(" + oszlop + ", " + score + ")";
        }
    }
    public class Quadron {
        private int[] values = new int[4];

        public Quadron(int value1, int value2, int value3, int value4) {
            values[0] = value1;
            values[1] = value2;
            values[2] = value3;
            values[3] = value4;
        }
        public int score(int playerIndex){
            int quadScore=0;
            int counter=0;
            int enemyCounter=0;
            int freeSpace=0;
            int oppIndex=2;
            if(playerIndex==2)
                oppIndex=1;
            for (int i=0;i<4;i++) {
                if (values[i] == playerIndex) {
                    counter++;
                }
                if (values[i]==0){
                    freeSpace++;
                }
                if (values[i]==oppIndex){
                    enemyCounter++;
                }
            }
            if(counter == 4)
                quadScore+=1000;
            else if(counter == 3 && freeSpace==1)
                quadScore+=100;
            else if(counter == 2 && freeSpace==2)
                quadScore+=10;
            if(enemyCounter==4)
                quadScore-=900;
            else if(enemyCounter==3 && freeSpace==1)
                quadScore-=100;
            else if(enemyCounter==2 && freeSpace==2)
                quadScore-=10;

            return quadScore;
        }

        public int getValue(int index) {
            if (index >= 0 && index < 4) {
                return values[index];
            } else {
                throw new IllegalArgumentException("Érvénytelen index: " + index);
            }
        }

        public void setValue(int index, int value) {
            if (index >= 0 && index < 4) {
                values[index] = value;
            } else {
                throw new IllegalArgumentException("Érvénytelen index: " + index);
            }
        }
    }



}
