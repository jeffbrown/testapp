import grails.core.support.GrailsConfigurationAware
import grails.config.Config

class TestappInterceptor implements GrailsConfigurationAware {


    String apiName
    String apiVersion

    String apinameEntrypoint
    String versionEntrypoint
    String entryPoint

    void setConfiguration(Config cfg) {
        apiName = cfg.info.app.name
        apiVersion = cfg.info.app.version

        apinameEntrypoint = "${apiName}_v${apiVersion}"
        versionEntrypoint = "v${apiVersion}"
        entryPoint = (apiName)?apinameEntrypoint:versionEntrypoint
        println("")
        match(uri:"/${entryPoint}/**")
    }

    boolean before() {
        println("### BEFORE")
        // ignore requests to the message controller.
        // there are better ways to handle this in
        // a real app but I want to leave the interceptor
        // using the original uri match because changing
        // that has introduced other questions so I
        // want to eliminate that part of the confusion.
        if(params.controller == 'message') {
            return true
        }

        addMessage 'beforeInterceptor'
        true
    }

    boolean after() {
        println("### AFTER")
        // ignore requests to the message controller.
        // there are better ways to handle this in
        // a real app but I want to leave the interceptor
        // using the original uri match because changing
        // that has introduced other questions so I
        // want to eliminate that part of the confusion.
        if(params.controller == 'message') {
            false
        }

        addMessage 'afterInterceptor'
        if(params.id) {
            int idValue = params.int('id')
            addMessage("id is $idValue")
            if (idValue > 0 && idValue != null) {
                addMessage "model : ${model}"
                def newId = idValue - 1
                addMessage 'redirect'
                forward(controller: params.controller, action: params.action, id: newId)
                false
            } else {
                render 'rendered by TestappInterceptor.after'
                false
            }
        }
        false
    }

    // appends msg to session.message
    // NOTE: there is no good reason to do this sort of thing
    // in a real app, but it is 1 simple technique to track what is going
    // on in the interceptor
    private addMessage(String msg) {
        session.message = ((session.message ?: '') + " (${msg})").trim()
    }
}
