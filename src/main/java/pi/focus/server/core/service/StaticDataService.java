package pi.focus.server.core.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import pi.focus.server.api.context.IBaseContext;
import pi.focus.server.api.context.IInfoContext;
import pi.focus.server.api.models.IAboutDataBlock;
import pi.focus.server.api.models.IDataTab;
import pi.focus.server.api.models.ITextCard;
import pi.focus.server.core.exception.StaticDataLoadingException;
import pi.focus.server.core.json.JsonMapper;
import pi.focus.server.core.json.dto.ImagedTabsDto;
import pi.focus.server.core.json.dto.TextCardDto;
import pi.focus.server.core.service.api.IStaticDataService;
import pi.focus.server.service.context.InfoContext;
import pi.focus.server.service.context.mocks.BaseContextMock;
import pi.focus.server.service.models.AboutDataBlock;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

@Service
@Profile({"dev", "prod"})
public class StaticDataService implements IStaticDataService {
    @Override
    public IInfoContext getInfo() {
        return new InfoContext(
                loadAboutData(),
                loadRulesData(),
                loadPreviewData()
        );
    }

    private IAboutDataBlock loadAboutData() {
        try (InputStream inputStream = Objects.requireNonNull(getClass().getResourceAsStream("/data/about.json"))) {
            return JsonMapper.getInstance().readValue(inputStream, AboutDataBlock.class);
        } catch (IOException e) {
            throw new StaticDataLoadingException("File load error: about.json", e);
        } catch (NullPointerException e) {
            throw new StaticDataLoadingException("File not found: about.json", e);
        }
    }

    private List<ITextCard> loadRulesData() {
        try (InputStream inputStream = Objects.requireNonNull(getClass().getResourceAsStream("/data/rules.json"))){
            TextCardDto response = JsonMapper.getInstance()
                    .readValue(inputStream, TextCardDto.class);
            return response.cards().stream().map(card -> (ITextCard) card).toList();
        } catch (IOException e) {
            throw new StaticDataLoadingException("File load error: rules.json", e);
        } catch (NullPointerException e) {
            throw new StaticDataLoadingException("File not found: rules.json", e);
        }
    }

    private List<IDataTab> loadPreviewData() {
        try (InputStream inputStream = Objects.requireNonNull(getClass().getResourceAsStream("/data/preview.json"))) {
            ImagedTabsDto response = JsonMapper.getInstance()
                    .readValue(inputStream, ImagedTabsDto.class);
            return response.imagedTabs().stream().map(imagedTab -> (IDataTab) imagedTab).toList();
        } catch (IOException e) {
            throw new StaticDataLoadingException("File load error: preview.json", e);
        } catch (NullPointerException e) {
            throw new StaticDataLoadingException("File not found: preview.json", e);
        }
    }

    @Override
    public IBaseContext getBaseContext() {
        // TODO Replace with real service
        return new BaseContextMock();
    }
}
