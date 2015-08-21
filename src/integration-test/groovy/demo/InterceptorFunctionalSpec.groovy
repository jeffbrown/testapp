package demo

import geb.spock.GebSpec
import grails.test.mixin.integration.Integration

@Integration
class InterceptorFunctionalSpec extends GebSpec {

    void "test filter that redirects"() {
        when:
        go '/testapp_v0.1/post/show?interceptorShouldRedirect=true'

        then:
        $().text() == 'The sayHello action was invoked. Message: TestappInterceptor Redirected'
    }

    void "test filter that invokes render"() {
        when:
        go '/testapp_v0.1/post/show'

        then:
        $().text() == 'TestappInterceptor.after rendered this'
    }
}
