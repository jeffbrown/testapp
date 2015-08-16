
import org.springframework.beans.factory.annotation.Value

class UrlMappings {

    @Value('${info.app.name}')
    String apiName
    @Value('${info.app.version}')
    String apiVersion

    static mappings = {
        String entrypoint = (apiName)?"${apiName}_v${apiVersion}":"v${apiVersion}"
        "/$entrypoint/$controller/$action?/$id?(.$format)?"{
        }
    }
}
