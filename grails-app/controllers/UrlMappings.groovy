
class UrlMappings {



    static mappings = {
        String apiName = getGrailsApplication().config.getProperty('info.app.name')
        String apiVersion = getGrailsApplication().config.getProperty('info.app.version')
        String entrypoint = (apiName)?"${apiName}_v${apiVersion}":"v${apiVersion}"
println(entrypoint)
        "/$entrypoint/$controller/$action?/$id?(.$format)?"{
            parseRequest = true
        }
    }
}
