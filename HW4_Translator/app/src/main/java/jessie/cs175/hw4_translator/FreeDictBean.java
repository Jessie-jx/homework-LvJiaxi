package jessie.cs175.hw4_translator;

import java.util.List;

public class FreeDictBean {

    private String word;
    private List<Phonetics> phonetics;
    private List<Meanings> meanings;
    public void setWord(String word) {
        this.word = word;
    }
    public String getWord() {
        return word;
    }

    public void setPhonetics(List<Phonetics> phonetics) {
        this.phonetics = phonetics;
    }
    public List<Phonetics> getPhonetics() {
        return phonetics;
    }

    public void setMeanings(List<Meanings> meanings) {
        this.meanings = meanings;
    }
    public List<Meanings> getMeanings() {
        return meanings;
    }

}

class Phonetics {

    private String text;
    private String audio;
    public void setText(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }
    public String getAudio() {
        return audio;
    }

}

class Definitions {

    private String definition;
    private String example;
    public void setDefinition(String definition) {
        this.definition = definition;
    }
    public String getDefinition() {
        return definition;
    }

    public void setExample(String example) {
        this.example = example;
    }
    public String getExample() {
        return example;
    }

}

class Meanings {

    private String partOfSpeech;
    private List<Definitions> definitions;
    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }
    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setDefinitions(List<Definitions> definitions) {
        this.definitions = definitions;
    }
    public List<Definitions> getDefinitions() {
        return definitions;
    }

}