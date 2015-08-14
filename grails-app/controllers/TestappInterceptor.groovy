import javax.servlet.http.HttpServletResponse

class TestappInterceptor{

    String entrypoint = "api_v0.1"

    // Test with URI; less common usage case
    def TestappInterceptor() {
        match uri: "/$entrypoint/**"
    }

	boolean before(){
            return true
	}

        // render full api return per request
	boolean after(){
            render(text:model, contentType:"text/json", encoding:"UTF-8")
	    return false
	}
}
