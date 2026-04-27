package pi.focus.server.service.context;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import pi.focus.server.api.context.IExampleContext;

@SuppressWarnings({"PMD.DataClass"})
// Realization of context for example page
@Service         
@Profile("prod")

public final class ExampleContext implements IExampleContext {
    private int number;
    private List<Integer> numbers;
    private String textString;
    private List<String> textStrings;
    private String picture;
    private List<String> pictures;

    public ExampleContext(
        int number,
        List<Integer> numbers,
        String textString,
        List<String> textStrings,
        String picture,
        List<String> pictures
    ) {
        this.number = number;
        this.numbers = numbers;
        this.textString = textString;
        this.textStrings = textStrings;
        this.picture = picture;
        this.pictures = pictures;
    }

    @Override
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public List<Integer> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<Integer> numbers) {
        this.numbers = numbers;
    }

    @Override
    public String getTextString() {
        return textString;
    }

    public void setTextString(String textString) {
        this.textString = textString;
    }

    @Override
    public List<String> getTextStrings() {
        return textStrings;
    }

    public void setTextStrings(List<String> textStrings) {
        this.textStrings = textStrings;
    }

    @Override
    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }
}
