package plus.xyc.maven.plugin.i18n;

import lombok.SneakyThrows;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import plus.xyc.maven.plugin.i18n.client.Client;
import plus.xyc.maven.plugin.i18n.utils.LogUtils;

/**
 * @author grant
 * @version 0.0.1
 */
@Mojo(name = "pull")
public class PullMojo extends AbstractMojo {

    @Parameter(name = "basedir", defaultValue = "${basedir}")
    private String basedir;

    @Parameter(name = "configPath", defaultValue = "${basedir}/src/main/resources/i18n/config.yml")
    private String configPath;

    @SneakyThrows
    public void execute() {
        LogUtils.set(getLog());
        Client client = new Client(basedir, configPath);
        client.pull();
    }
}
