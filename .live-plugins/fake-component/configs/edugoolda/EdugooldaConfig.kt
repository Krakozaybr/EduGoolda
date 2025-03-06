package configs.edugoolda

import fakes.codegen.config.PluginConfig
import configs.common.commonProviders

object EdugooldaConfig : PluginConfig {

    override val appPackage = "ru.itmo.edugoolda"

    override fun isChild(
        interfaceName: String
    ) = "Child" == interfaceName

    override fun getFakeComponentName(
        interfaceName: String
    ) = "Fake$interfaceName"

    override fun isComponent(
        interfaceName: String
    ) = "Component" in interfaceName

    override val providers = buildList {
        addAll(exampleProviders)
        addAll(commonProviders)
    }
}

