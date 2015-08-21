import grails.util.Metadata

class UrlMappings {

    static mappings = {
        Metadata metaData = Metadata.current

        String apiName = metaData.getApplicationName()
        String apiVersion = metaData.getApplicationVersion()

        String entrypoint = (apiName)?"${apiName}_v${apiVersion}":"v${apiVersion}"
        
        "/$entrypoint/$controller/$action?/$id?(.$format)?"{
            parseRequest = true
        }
    }
}
