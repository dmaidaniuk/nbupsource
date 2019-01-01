package io.github.nbupsource.utils;

import java.io.File;
import org.openide.modules.InstalledFileLocator;
import org.openide.modules.Places;

public final class FileUtils {

    public static final String UPSOURCE_PROPERTIES_FILE_NAME = "upsource-properties.json";

    private FileUtils() {
    }

    public static File getUpsourcePropertiesFile() {
        File file = InstalledFileLocator.getDefault().locate(UPSOURCE_PROPERTIES_FILE_NAME, null, false);
        if (file == null) {
            file = new File(Places.getUserDirectory() + File.separator + UPSOURCE_PROPERTIES_FILE_NAME);
        }
        return file;
    }

}
