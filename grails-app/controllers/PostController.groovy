
class PostController {

	def show(){
        [fname: 'Bob']
	}

	def doit() {
	    render 'it worked'
	}
}
