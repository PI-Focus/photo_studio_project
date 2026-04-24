package pi.focus.server.context.mocks;

import pi.focus.server.context.IInfoContext;
import pi.focus.server.models.IAboutDataBlock;
import pi.focus.server.models.mocks.AboutDataBlockMock;

public class InfoContextMock implements IInfoContext {
    @Override
    public IAboutDataBlock getAboutBlock() {
        return new AboutDataBlockMock();
    }
    
}
