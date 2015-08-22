package testapp

import geb.spock.GebSpec
import grails.test.mixin.integration.Integration

@Integration
class MappingConfigFunctionalSpec extends GebSpec {

    void 'test invoking a URL that results in an interceptor invoking a redirect'() {
        when:
        go '/testapp_v0.1/post/show/1'

        then:
        $().text() == 'rendered by TestappInterceptor.after'

        when:
        go '/testapp_v0.1/message'

        then:
        $().text() == 'Message: [(beforeInterceptor) (afterInterceptor) (id is 1) (model : [fname:Bob]) (redirect) (beforeInterceptor) (afterInterceptor) (id is 0)]'
    }

    void 'test invoking a URL that results in several round trips of interceptor redirects'() {
        when:
        go '/testapp_v0.1/post/show/2'

        then:
        $().text() == 'rendered by TestappInterceptor.after'

        when:
        go '/testapp_v0.1/message'

        then:
        $().text() == 'Message: [(beforeInterceptor) (afterInterceptor) (id is 2) (model : [fname:Bob]) (redirect) (beforeInterceptor) (afterInterceptor) (id is 1) (model : [fname:Bob]) (redirect) (beforeInterceptor) (afterInterceptor) (id is 0)]'
    }

    void 'test that interceptors are not run when a request is sent to a URL that does not match a URL mapping'() {
        when:
        go '/fred/post/show/1'

        then:
        $().text().contains 'Error: Page Not Found (404)'
        $().text().contains 'Path: /fred/post/show'

        when:
        go '/testapp_v0.1/message'

        then:
        $().text() == 'Message: []'
    }
}