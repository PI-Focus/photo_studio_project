package pi.focus.server.service.context.mocks;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import pi.focus.server.api.context.IExampleContext;

@Service
@Profile("dev")
public class ExampleContextMock implements IExampleContext {

    @Override
    public int getNumber() {
        return 420;
    }

    @Override
    public List<Integer> getNumbers() {
        return List.of(1, 2, 3, 4, 5);
    }

    @Override
    public String getTextString() {
        return "Very cool text!";
    }

    @Override
    public List<String> getTextStrings() {
        return List.of("Text number one", "Text number two", "Text number three", "Text number four", "Text number five");
    }

    @Override
    public String getPicture() {
        return "/images/placeholder.png";
    }

    @Override
    public List<String> getPictures() {
        return Collections.nCopies(5, "/images/placeholder.png");
    }
    
}
