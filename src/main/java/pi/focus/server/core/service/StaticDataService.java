package pi.focus.server.core.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;

import pi.focus.server.api.context.IBaseContext;
import pi.focus.server.api.context.IInfoContext;
import pi.focus.server.api.models.IAboutDataBlock;
import pi.focus.server.api.models.IDataCard;
import pi.focus.server.api.models.ITab;
import pi.focus.server.api.models.ITextCard;
import pi.focus.server.core.exception.StaticDataLoadingException;
import pi.focus.server.core.json.JsonMapper;
import pi.focus.server.core.service.api.IStaticDataService;
import pi.focus.server.service.context.BaseContextDto;
import pi.focus.server.service.context.InfoContextDto;
import pi.focus.server.service.models.AboutDataBlockDto;
import pi.focus.server.service.models.DataCardDto;
import pi.focus.server.service.models.TabDto;
import pi.focus.server.service.models.TextCardDto;

@Service
@Profile({"dev", "prod"})
public class StaticDataService implements IStaticDataService {

    @Override
    public IInfoContext getInfo() {
        return new InfoContextDto(
            loadAboutDataBlock(),
            loadRulesData(),
            loadPreviewData()
        );
    }

    @Override
    public IBaseContext getBaseContext() {
        return loadBaseData();
    }

    private IAboutDataBlock loadAboutDataBlock() {
        InputStream inputStream = getClass().getResourceAsStream("/data/rules.json");

        if (inputStream == null) {
            throw new StaticDataLoadingException("File not found: rules.json");
        }

        try (inputStream) { 
            return JsonMapper.getInstance().readValue(inputStream, AboutDataBlockDto.class);
        } catch (IOException e) {
            throw new StaticDataLoadingException("File load or parsing error: rules.json", e);
        }
    }

    private List<ITextCard> loadRulesData() {
        InputStream inputStream = getClass().getResourceAsStream("/data/rules.json");
        if (inputStream == null) throw new StaticDataLoadingException("File not found: rules.json");

        try (inputStream) {
            List<TextCardDto> dtos = JsonMapper.getInstance().readValue(
                inputStream, 
                new TypeReference<List<TextCardDto>>() {}
            );
            
            return List.copyOf(dtos); 
        } catch (IOException e) {
            throw new StaticDataLoadingException("File load or parsing error: rules.json", e);
        }
    }

    private List<ITab<IDataCard>> loadPreviewData() {
        InputStream inputStream = getClass().getResourceAsStream("/data/preview.json");
        if (inputStream == null) {
            throw new StaticDataLoadingException("File not found: preview.json");
        }

        try (inputStream) {
            List<TabDto<DataCardDto>> dtos = JsonMapper.getInstance().readValue(
                inputStream, 
                new TypeReference<List<TabDto<DataCardDto>>>() {}
            );
            
            return dtos.stream()
                       .map(tab -> new TabDto<IDataCard>(tab.tabName(), tab.data()))
                       .map(tab -> (ITab<IDataCard>) tab)
                       .toList();
                       
        } catch (IOException e) {
            throw new StaticDataLoadingException("File load or parsing error: preview.json", e);
        }
    }

    private IBaseContext loadBaseData() {
        InputStream inputStream = getClass().getResourceAsStream("/data/base.json");
        
        if (inputStream == null) {
            throw new StaticDataLoadingException("File not found: base.json");
        }

        try (inputStream) {
            return JsonMapper.getInstance().readValue(inputStream, BaseContextDto.class);
        } catch (IOException e) {
            throw new StaticDataLoadingException("File load or parsing error: base.json", e);
        }
    }
}