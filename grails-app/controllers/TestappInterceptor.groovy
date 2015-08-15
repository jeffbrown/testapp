import org.springframework.beans.factory.annotation.Value

class TestappInterceptor{

    @Value('${info.app.name}')
    String apiName
    @Value('${info.app.version}')
    String apiVersion



    // Test with URI; less common usage case
    def TestappInterceptor() {
        String entrypoint = (apiName)?"${apiName}_v${apiVersion}":"v${apiVersion}"
        match(uri:"/${entrypoint}/**")
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
