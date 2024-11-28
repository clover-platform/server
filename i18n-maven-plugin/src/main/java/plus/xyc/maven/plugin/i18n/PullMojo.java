package plus.xyc.maven.plugin.i18n;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * @author grant
 * @version 0.0.1
 */
@Mojo(name = "pull")
public class PullMojo extends AbstractMojo {

    @Parameter(name = "basedir", defaultValue = "${basedir}")
    private String basedir;

    public void execute() throws MojoExecutionException, MojoFailureException {
        Log log = getLog();
        log.info("PullMojo execute");
    }
}
