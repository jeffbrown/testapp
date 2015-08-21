package testapp

import geb.spock.GebSpec
import grails.test.mixin.integration.Integration
import spock.lang.*
import groovy.util.AntBuilder
import groovy.json.*
import java.util.regex.*

import grails.test.runtime.FreshRuntime

@FreshRuntime
@Integration
class MappingConfigFunctionalSpec extends GebSpec {

    def grailsApplication

    void 'URLMapping Wildcard with Good Interception'() {

        when:
        String entrypoint = "testapp2_v0.1"

        // Start reading STDOUT
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        PrintStream ps = new PrintStream(baos)
        PrintStream old = System.out
        System.setOut(ps)

        def ant = new AntBuilder()
        ant.exec(outputProperty:"cmdOut",errorProperty:"cmdErr",resultProperty:"cmdExit",failonerror:"false",executable:"curl"){
            arg(line:"""--verbose --request GET --header "Content-Type: application/json" "http://localhost:8080/${entrypoint}/post/show/1" --cookie cookies.txt""")
        }
        LinkedHashMap errOutput = parseOutput(ant.project.properties.cmdErr)

        // FLUSH STDOUT AND RESET
        System.out.flush()
        System.setOut(old)
        String result = new String(baos.toByteArray());

        // PARSE STDOUT
        List flow = parseStdout(result.trim())

        then:
        assert flow[0][0].trim() == 'TestappInterceptor:Before'
        assert flow[0][1].trim() == 'PostController:show'
        assert flow[0][2].trim() == 'TestappInterceptor:After'
        assert flow[0][3].trim() == 'model : [fname:Bob]'
        assert errOutput.request.uri.uri == "/${entrypoint}/post/show/1"
        assert ['302','200'].contains(errOutput.response.code.code)

        //assert json.collect(){it.key} == ['Bob']
    }

    void 'URLMapping Wildcard with BAD Interception - is NOT MAPPED HERE'() {

        when:

        // Start reading STDOUT
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        PrintStream ps = new PrintStream(baos)
        PrintStream old = System.out
        System.setOut(ps)

        def ant = new AntBuilder()
        ant.exec(outputProperty: "cmdOut", errorProperty: "cmdErr", resultProperty: "cmdExit", failonerror: "false", executable: "curl") {
            arg(line: """--verbose --request GET --header "Content-Type: application/json" "http://localhost:8080/fred/post/show/1" --cookie cookies.txt""")
        }
        LinkedHashMap errOutput = parseOutput(ant.project.properties.cmdErr)

        // FLUSH STDOUT AND RESET
        System.out.flush()
        System.setOut(old)
        String result = new String(baos.toByteArray());

        // PARSE STDOUT
        List flow = parseStdout(result.trim())


        then:
        if(flow){
            if (flow[0].size() == 4) {
                assert flow[0][0].trim() != 'TestappInterceptor:Before'
                assert flow[0][1].trim() != 'PostController:show'
                assert flow[0][2].trim() != 'TestappInterceptor:After'
                assert flow[0][3].trim() != 'model : [fname:Bob]'
            }else{
                assert flow[0][0].trim() != 'PostController:show'
            }
            assert errOutput.request.uri.uri != "/${entrypoint}/post/show/1"
            assert !['302','200'].contains(errOutput.response.code.code)
        }else{
            assert true
        }

    }

    void 'Test Redirect for same url simulating an automated api batch'() {

        when:
        String entrypoint = "testapp2_v0.1"
        Integer redirectNum = 5

        // Start reading STDOUT
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        PrintStream ps = new PrintStream(baos)
        PrintStream old = System.out
        System.setOut(ps)

        def ant = new AntBuilder()
        ant.exec(outputProperty:"cmdOut",errorProperty:"cmdErr",resultProperty:"cmdExit",failonerror:"false",executable:"curl"){
            arg(line:"""--verbose --request GET --header "Content-Type: application/json" "http://localhost:8080/${entrypoint}/post/show/${redirectNum}" --cookie cookies.txt""")
        }
        LinkedHashMap errOutput = parseOutput(ant.project.properties.cmdErr)

        // FLUSH STDOUT AND RESET
        System.out.flush()
        System.setOut(old)
        String result = new String(baos.toByteArray());

        // PARSE STDOUT
        List flow = parseStdout(result.trim())

        then:
        assert true
        if(flow.size()==redirectNum){
            for(int i=0; int<redirectNum;i++){
                assert flow[i][0].trim() == 'TestappInterceptor:Before'
                assert flow[i][1].trim() == 'PostController:show'
                assert flow[i][2].trim() == 'TestappInterceptor:After'
                assert flow[i][3].trim() == 'model : [fname:Bob]'
            }
            assert errOutput.request.uri.uri == "/${entrypoint}/post/show/*"
            assert ['302','200'].contains(errOutput.response.code.code)
        }else{
            assert false
        }

    }

    List parseStdout(String stdout){
        List flowOutput = ['TestappInterceptor:Before','PostController:show','TestappInterceptor:After','model : [fname:Bob]']

        List output = stdout.split(/\[exec\]./)

        List loop = []
        List flow = []
         output.each() { it ->
             if(flowOutput.contains(it.trim())){
                if(it.trim()){
                    flow.add(it)
                }
             }
            if(it.startsWith("model")){
                List newFlow = flow
                loop.add(newFlow)
                flow=[]
            }
        }
        if(!loop && flow){
            List newFlow = flow
            loop.add(newFlow)
            flow=[]
        }
        return loop
    }

    LinkedHashMap parseOutput(String output){
        //println("return code : "+ant.project.properties.cmdExit)
        //println("stderr : "+ant.project.properties.cmdErr)
        //println("stdout : "+ant.project.properties.cmdOut)
        def req = [:]
        def resp = [:]
        output.splitEachLine("//"){ it ->
            //println(it)
            it.each(){ it2 ->
                if(it2 =~ />.+/){
                    if(it2.size()>3){
                        it2 = it2[2..-1]
                        if(it2.contains(":")){
                            def temp = it2.split(":")
                            req[temp[0]] = (temp[1])?temp[1]:[]
                        }else{
                            def temp = it2.split(" ")
                            req['uri'] = ['method':temp[0],'uri':temp[1],'protocol':temp[2]]
                        }
                    }
                }

                if(it2 =~ /<.+/){
                    if(it2.size()>3){
                        it2 = it2[2..-1]
                        if(it2.contains(":")){
                            def temp = it2.split(":")
                            resp[temp[0]] = (temp[1])?temp[1]:[]
                        }else{
                            def temp = it2.split(" ")
                            resp['code'] = ['protocol':temp[0],'code':temp[1],'message':temp[2]]
                        }
                    }
                }
            }
        }

        return ['request':req,'response':resp]
    }
}