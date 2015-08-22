
class MessageController {

    def index() {
        render "Message: [${session.message ?: ''}]"
    }
}
