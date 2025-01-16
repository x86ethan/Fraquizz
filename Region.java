class Region {

    int regionNumber;
    String regionName;
    String stateName;
    String capitalCity;

    String[] questions;

    int[] characters;
    
    boolean guessed;

    String getQuestion(int difficulty) {
        return questions[difficulty];
    }

}