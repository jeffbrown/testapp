import javax.servlet.http.HttpServletResponse

class TestappInterceptor{

    def TestappInterceptor() {
        match uri: '/api_v0.1/**'
    }

	boolean after(){
        redirect controller: 'post', action: 'doit'
	    return false
	}
}
