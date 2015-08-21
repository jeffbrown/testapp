
class PostController {

	def show(){
        [fname: 'Bob']
	}

    def sayHello(String message) {
        render "The sayHello action was invoked.  Message: $message"
    }
}
