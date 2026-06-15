package pi.focus.server.core.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;

import pi.focus.server.api.context.IBaseContext;
import pi.focus.server.api.context.IInfoContext;
import pi.focus.server.api.models.IAboutDataBlock;
import pi.focus.server.api.models.IDataCard;
import pi.focus.server.api.models.INamedData;
import pi.focus.server.api.models.ITextCard;
import pi.focus.server.core.exception.StaticDataLoadingException;
import pi.focus.server.core.mapper.JsonMapper;
import pi.focus.server.core.service.api.IStaticDataService;
import pi.focus.server.service.context.BaseContextDto;
import pi.focus.server.service.context.InfoContextDto;
import pi.focus.server.service.models.AboutDataBlockDto;
import pi.focus.server.service.models.DataCardDto;
import pi.focus.server.service.models.TabDto;
import pi.focus.server.service.models.TextCardDto;

@Service
@Profile({"dev", "prod", "test"})
public class StaticDataService implements IStaticDataService {
    private static final String ERROR_STRING = "File load or parsing error: ";
    private final IInfoContext infoContext;
    private final IBaseContext baseContext;

    public StaticDataService(
        @Value("${app.static-data.about-path:classpath:/data/about.json}") Resource aboutResource,
        @Value("${app.static-data.rules-path:classpath:/data/rules.json}") Resource rulesResource,
        @Value("${app.static-data.preview-path:classpath:/data/preview.json}") Resource previewResource,
        @Value("${app.static-data.base-path:classpath:/data/base.json}") Resource baseResource
    ) {
        this.infoContext = new InfoContextDto(
            loadAboutDataBlock(aboutResource),
            loadRulesData(rulesResource),
            loadPreviewData(previewResource)
        );
        this.baseContext = loadBaseData(baseResource);
    }

    @Override
    public IInfoContext getInfo() {
        return infoContext;
    }

    @Override
    public IBaseContext getBaseContext() {
        return baseContext;
    }
        private IAboutDataBlock loadAboutDataBlock(Resource resource) {
        try (InputStream inputStream = resource.getInputStream()) {
            return JsonMapper.getInstance().readValue(inputStream, AboutDataBlockDto.class);
        } catch (IOException e) {
            throw new StaticDataLoadingException(ERROR_STRING + resource.getDescription(), e);
        }
    }

    private List<ITextCard> loadRulesData(Resource resource) {
        try (InputStream inputStream = resource.getInputStream()) {
            List<TextCardDto> dtos = JsonMapper.getInstance().readValue(inputStream, new TypeReference<>() {});
            return List.copyOf(dtos); 
        } catch (IOException e) {
            throw new StaticDataLoadingException(ERROR_STRING + resource.getDescription(), e);
        }
    }

    private List<INamedData<IDataCard>> loadPreviewData(Resource resource) {
        try (InputStream inputStream = resource.getInputStream()) {
            List<TabDto<DataCardDto>> dtos = JsonMapper.getInstance().readValue(inputStream, new TypeReference<>() {});
            return dtos.stream()
                       .map(tab -> new TabDto<IDataCard>(tab.tabName(), tab.data()))
                       .map(tab -> (INamedData<IDataCard>) tab)
                       .toList();
        } catch (IOException e) {
            throw new StaticDataLoadingException(ERROR_STRING + resource.getDescription(), e);
        }
    }

    private IBaseContext loadBaseData(Resource resource) {
        try (InputStream inputStream = resource.getInputStream()) {
            return JsonMapper.getInstance().readValue(inputStream, BaseContextDto.class);
        } catch (IOException e) {
            throw new StaticDataLoadingException(ERROR_STRING + resource.getDescription(), e);
        }
    }

}