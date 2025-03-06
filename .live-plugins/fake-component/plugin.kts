import liveplugin.registerIntention
import fakes.view.CreateFakeComponentIntention
import configs.edugoolda.EdugooldaConfig

// depends-on-plugin org.jetbrains.kotlin
// add-to-classpath $PLUGIN_PATH/kotlinpoet-1.14.0.jar

registerIntention(CreateFakeComponentIntention(EdugooldaConfig))
