class TestappInterceptor {

    def TestappInterceptor() {
        String entrypoint = "testapp_v0.1"

        match uri: "/$entrypoint/post/**"
    }

    boolean before() {
        addMessage 'beforeInterceptor'
        true
    }

    boolean after() {
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
