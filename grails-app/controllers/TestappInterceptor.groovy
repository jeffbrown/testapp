import javax.servlet.http.HttpServletResponse

class TestappInterceptor{

    // Test with URI; less common usage case
    def TestappInterceptor() {

        String entrypoint = "api_v0.1"

        match uri: "/$entrypoint/**"
        //matchAll().excludes(controller:"login")
    }

	boolean before(){
        println("### BEFORE")
            return true
	}

	boolean after(){
        println("### AFTER")
        try {
            if(params.id>0){
                println("model : ${model}")
                def newId = params.id-1
                redirect(controller:params.controller,action:params.action,id:params.id)
            }else{
                render(text:model, contentType:"text/json", encoding:"UTF-8")
            }
        }catch(Exception e){
            println("#### AFTER exception : "+e)
        }
	    return false
	}
}
