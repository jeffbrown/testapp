import javax.servlet.http.HttpServletResponse

class TestappInterceptor{

    // Test with URI; less common usage case
    def TestappInterceptor() {

        String entrypoint = "api_v0.1"

        match uri: "/$entrypoint/**"
    }

	boolean before(){
        println("### BEFORE")
            return true
	}

	boolean after(){
        println("### AFTER")
            render(text:model, contentType:"text/json", encoding:"UTF-8")
	    return false
	}
}
