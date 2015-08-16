import grails.util.Metadata

class UrlMappings {

    static mappings = {
        def apiName = Metadata.current.getApplicationName()
        def apiVersion = Metadata.current.getApplicationVersion()
        def entryPoint = (apiName)?"${apiName}_v${apiVersion}":"v${apiVersion}"

        "/${entryPoint}/$controller/$action?/$id?(.$format)?"{
        }
    }
}
