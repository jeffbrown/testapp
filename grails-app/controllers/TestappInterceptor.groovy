class TestappInterceptor{

    def TestappInterceptor() {
		match controller: 'post'
	}

	boolean after(){
		render 'test'
		false
	}
}
