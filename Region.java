class Region {

    int regionNumber;
    String regionName;

    String[] questions;
    
    boolean guessed;

    String getQuestion(int difficulty) {
        return questions[difficulty];
    }

}