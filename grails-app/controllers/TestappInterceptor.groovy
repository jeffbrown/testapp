import grails.util.Metadata

class TestappInterceptor {

    def TestappInterceptor() {
        Metadata metaData = Metadata.current

        String apiName = metaData.getApplicationName()
        String apiVersion = metaData.getApplicationVersion()

        String entrypoint = (apiName)?"${apiName}_v${apiVersion}":"v${apiVersion}"

        match uri: "/$entrypoint/**"
    }

    boolean after() {
        if(params.action == 'sayHello') {
            return true
        }

        if (params.interceptorShouldRedirect) {
            redirect controller: 'post', action: 'sayHello', params: [message: 'TestappInterceptor Redirected']
        } else {
            render 'TestappInterceptor.after rendered this'
        }
        false
    }
}
