
import org.springframework.beans.factory.annotation.Value

class UrlMappings {

    @Value('${info.app.name}')
    String apiName
    @Value('${info.app.version}')
    String apiVersion

    String entrypoint = (apiName)?"${apiName}_v${apiVersion}":"v${apiVersion}"

    static mappings = {
        "/${entrypoint}/$controller/$action?/$id?(.$format)?"{
            controller = controller
            action = action
            parseRequest = true
        }
    }
}
