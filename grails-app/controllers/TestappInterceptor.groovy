


import javax.servlet.http.HttpServletResponse

class TestappInterceptor{
	
	int order = HIGHEST_PRECEDENCE + 999

    String entrypoint = "api_v0.1"

    def TestappInterceptor() {

        match(uri:/^(\/${entrypoint}\/*(.+))$/)
	}

	boolean before(){
        return true
	}

	boolean after(){
        println(model.getClass())
		try{
			if(!model){
				render(status:HttpServletResponse.SC_BAD_REQUEST)
			}else{
                def newModel = (model)?apiResponseService.convertModel(model):model
                render(text:newModel, contentType:"text/json", encoding:"UTF-8")
           }
	   }catch(Exception e){
		   log.error("[ApiToolkitFilters :: apitoolkit.after] : Exception - full stack trace follows:", e);
		   return false
	   }

	   return false
	}
}

