package io.github.nbupsource.ui.props;

import io.github.nbupsource.dto.UpsourceProperties;
import io.github.nbupsource.utils.AES;
import io.github.nbupsource.utils.FileUtils;
import io.github.nbupsource.utils.JsonUtils;
import java.io.File;
import java.util.Collections;
import java.util.ResourceBundle;
import javax.swing.JComponent;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.spi.project.ui.support.ProjectCustomizer;
import org.netbeans.spi.project.ui.support.ProjectCustomizer.Category;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

public class UpsourcePropertiesTab implements ProjectCustomizer.CompositeCategoryProvider {
    
    private static final String PROPERTIES_TAB_NAME = "LBL_Config_Upsource_properties";

    private final String name;

    private UpsourcePropertiesTab(String name) {
        this.name = name;
    }

    @Override
    public Category createCategory(Lookup lkp) {
        ResourceBundle bundle = NbBundle.getBundle(UpsourcePropertiesTab.class);
        return ProjectCustomizer.Category.create(name, bundle.getString(PROPERTIES_TAB_NAME), null);
    }

    @Override
    public JComponent createComponent(Category category, Lookup lookup) {
        
        String projectName = "testProject";
        String upsourceUrl = "https://dev.crxmarkets.com/upsource/";
        String userName = "maidaniuk";
        String password = "Snickers23!";
        UpsourceProperties upsourceProperties = new UpsourceProperties(projectName, upsourceUrl, userName, password);
        try {
            Project project = lookup.lookup(Project.class);
            String name = ProjectUtils.getInformation(project).getName();
            File upsourcePropertiesFile = FileUtils.getUpsourcePropertiesFile();
            JsonUtils.writePropertiesToFile(Collections.singletonList(upsourceProperties), upsourcePropertiesFile);
//            byte[] cipher = AES.encrypt(password, userName);
//            String decrypted = AES.decrypt(cipher, userName);
//            if (!password.equals(decrypted)) {
//                throw new IllegalStateException("Worng encription algorithm!");
//            }
        }
        catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
        return new PropertiesPanel(upsourceProperties);
    }

    @ProjectCustomizer.CompositeCategoryProvider.Registration(
        projectType = "org-netbeans-modules-maven", 
        position = 200)
    public static UpsourcePropertiesTab createUpsourceConfigurationTab() {
        return new UpsourcePropertiesTab("Upsource");
    }
    
}