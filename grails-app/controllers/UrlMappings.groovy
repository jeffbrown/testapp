import grails.util.Metadata


class UrlMappings {

    static mappings = {
        String apiName = Metadata.current.getApplicationName()
        String apiVersion = Metadata.current.getApplicationVersion()
        String entrypoint = (apiName)?"${apiName}_v${apiVersion}":"v${apiVersion}"

        "/$entrypoint/$controller/$action?/$id?(.$format)?"{
            parseRequest = true
        }

        "404"(view:'/notFound')
    }
}
