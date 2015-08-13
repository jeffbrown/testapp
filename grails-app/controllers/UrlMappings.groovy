class UrlMappings {

    static mappings = {
        "/api_v0.1/$controller/$action?/$id?(.$format)?"{
            controller = controller
            action = action
            parseRequest = true
        }
    }
}
