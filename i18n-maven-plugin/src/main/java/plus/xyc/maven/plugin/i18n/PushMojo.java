package plus.xyc.maven.plugin.i18n;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import plus.xyc.maven.plugin.i18n.client.Client;

/**
 * @author grant
 * @version 0.0.1
 */
@Mojo(name = "push")
public class PushMojo extends AbstractMojo {

    @Parameter(name = "basedir", defaultValue = "${basedir}")
    private String basedir;

    @Parameter(name = "configPath", defaultValue = "${basedir}/src/main/resources/i18n/config.yml")
    private String configPath;

    public void execute() {
        Client client = new Client(basedir, configPath);
        client.push();
    }
}
