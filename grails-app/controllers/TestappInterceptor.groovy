import grails.core.support.GrailsConfigurationAware
import grails.config.Config

class TestappInterceptor implements GrailsConfigurationAware {

    String apiName
    String apiVersion

    String apinameEntrypoint
    String versionEntrypoint
    String entryPoint

    void setConfiguration(Config cfg) {
        this.apiName = cfg.apitoolkit.apiName
        this.apiVersion = cfg.info.app.version

        this.apinameEntrypoint = "${this.apiName}_v${this.apiVersion}"
        this.versionEntrypoint = "v${this.apiVersion}"
        this.entryPoint = (this.apiName)?this.apinameEntrypoint:this.versionEntrypoint

        match(uri:"/${entryPoint}/**")
    }

    boolean before() {
        if(params.controller!='message') {
            addMessage 'beforeInterceptor'
        }

        true
    }

    boolean after() {
        if(params.controller!='message') {
            addMessage 'afterInterceptor'
            int idValue = params.int('id')
            addMessage("id is $idValue")
            if (idValue > 0) {
                addMessage "model : ${model}"
                def newId = idValue - 1
                addMessage 'redirect'
                redirect(controller: params.controller, action: params.action, id: newId)
            } else {
                render 'rendered by TestappInterceptor.after'
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
