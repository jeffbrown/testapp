import javax.servlet.http.HttpServletResponse

class TestappInterceptor{

    // Test with URI; less common usage case
    def TestappInterceptor() {

        String entrypoint = "testapp2_v0.1"

        match uri: "/$entrypoint/**"

    }

	boolean before(){
        println("TestappInterceptor:Before")
            return true
	}

	boolean after(){
        println("TestappInterceptor:After")
        try {
            if(params.id>0){
                println("model : ${model}")
                def newId = params.id-1
                redirect(controller:params.controller,action:params.action,id:newId)
            }else{
                render(text:model, contentType:"text/json", encoding:"UTF-8")
            }
        }catch(Exception e){
            println("#### AFTER exception : "+e)
        }
	    return false
	}
}
